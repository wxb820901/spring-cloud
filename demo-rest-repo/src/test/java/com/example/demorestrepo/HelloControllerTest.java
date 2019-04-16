package com.example.demorestrepo;

import com.example.demorestrepo.entity.Customer;
import com.example.demorestrepo.entity.CustomerGroup;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoRestRepoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class HelloControllerTest {
    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        System.out.println(String.format("port is : [%d]", port));
        this.base = new URL(url);
    }

    @Test
    public void test1() throws Exception {
//save customer with group
        CustomerGroup cg1 = new CustomerGroup("group1");
        Customer cus1 = new Customer("fn1","ln1", cg1);
        ResponseEntity<Customer> responseCus = this.restTemplate.postForEntity(
                this.base.toString() + "/customers", cus1, Customer.class);
        System.out.println(String.format("测试结果post cus 为：%s", responseCus.getBody()+"|"+responseCus.getHeaders()));
//update group for just post customer
        cus1 = responseCus.getBody();
        cus1.getCustomerGroups().add(new CustomerGroup("group2"));
        HttpEntity<Customer> requestEntity = new HttpEntity<>(cus1);
        ResponseEntity<Customer> responseUpdate = restTemplate.exchange(
                this.base.toString() + "/customers"+"/"+cus1.getId(),
                HttpMethod.PUT, requestEntity, Customer.class );
        System.out.println(String.format("测试结果update cus 为：%s", responseUpdate.getBody()));

        cus1 = responseUpdate.getBody();
        cus1.setCustomerGroups(null);
        requestEntity = new HttpEntity<>(cus1);
        responseUpdate = restTemplate.exchange(
                this.base.toString() + "/customers"+"/"+cus1.getId(),
                HttpMethod.PUT, requestEntity, Customer.class );
        System.out.println(String.format("测试结果update cus 为：%s", responseUpdate.getBody()));


//delete group for just post customer
//if not delete customer first==>Referential integrity constraint violation: "FK8VKNDL4IX5U2985M6MRRKGEW1: PUBLIC.CUSTOMER_CUSTOMERGROUP FOREIGN KEY(CUSTOMERGROUPS_ID) REFERENCES PUBLIC.CUSTOMERGROUP(ID) (2)"; SQL statement:

        restTemplate.delete(
                this.base.toString() + "/customerGroups"+"/"+2);



        ResponseEntity<String> response4 = this.restTemplate.getForEntity(
                this.base.toString() + "/customerGroups", String.class);
        System.out.println(String.format("测试结果customerGroups为：%s", response4.getBody()));

//        ResponseEntity<CustomerGroup> response4cg = this.restTemplate.getForEntity(
//                this.base.toString() + "/customerGroups/1", CustomerGroup.class);
//        System.out.println(String.format("测试结果/customerGroups/1为：%s", response4cg.getBody()));

        ResponseEntity<String> responseGetBySearch = this.restTemplate.getForEntity(
                this.base.toString() + "/customers/search/findByLastname?name=Matthews", String.class);
        System.out.println(String.format("测试结果/customers/search/findByLastname?name=Matthews为：%s", responseGetBySearch.getBody()));

        responseGetBySearch = this.restTemplate.getForEntity(
                this.base.toString() + "/customers/search", String.class);
        System.out.println(String.format("测试结果/customers/search为：%s", responseGetBySearch.getBody()));

        responseGetBySearch = this.restTemplate.getForEntity(
                this.base.toString() + "/profile/customers", String.class);
        System.out.println(String.format("测试结果/profile/customers为：%s", responseGetBySearch.getBody()));


        responseGetBySearch = this.restTemplate.getForEntity(
                this.base.toString() + "/customerGroups/search/findByGroupName?name=group1", String.class);
        System.out.println(String.format("测试结果/customerGroups/search/findByGroupName?name=group1：%s", responseGetBySearch.getBody()));

        ResponseEntity<String> response = this.restTemplate.getForEntity(
                this.base.toString() + "/configMsg", String.class);
        System.out.println(String.format("测试结果configMsg为：%s", response.getBody()));

        ResponseEntity<String> response1 = this.restTemplate.getForEntity(
                this.base.toString() + "/customers", String.class);
        System.out.println(String.format("测试结果customers为：%s", response1.getBody()));

        ResponseEntity<String> response2 = this.restTemplate.getForEntity(
                this.base.toString() + "/customers/1", String.class);
        System.out.println(String.format("测试结果customers/1为：%s", response2.getBody()));
    }
}
