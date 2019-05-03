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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        ResponseEntity<String> configMsg = this.restTemplate.getForEntity(
                this.base.toString() + "/configMsg", String.class);
        System.out.println(String.format("测试结果get configMsg为：%s", configMsg.getBody()+"|"+configMsg.getHeaders()));

//save customer with group
        CustomerGroup cg1 = new CustomerGroup();
        cg1.setGroupName("group1");

        ResponseEntity<CustomerGroup> responseCusg = this.restTemplate.postForEntity(
                this.base.toString() + "/customerGroups", cg1, CustomerGroup.class);
        System.out.println(String.format("测试结果post CustomerGroup 为：%s", responseCusg.getBody()+"|"+responseCusg.getHeaders()));
        Customer cus1 = new Customer();
        cus1.setFirstname("fn1");
        cus1.setLastname("ln1");
        cus1.setGroupId(responseCusg.getBody().getId());
        ResponseEntity<Customer> responseCus = this.restTemplate.postForEntity(
                this.base.toString() + "/customers", cus1, Customer.class);
        System.out.println(String.format("测试结果post cus 为：%s", responseCus.getBody()+"|"+responseCus.getHeaders()));
//update group for just post customer
        cus1 = responseCus.getBody();
        HttpEntity<Customer> requestEntity = new HttpEntity<>(cus1);
        ResponseEntity<Customer> responseUpdate = restTemplate.exchange(
                this.base.toString() + "/customers"+"/"+cus1.getId(),
                HttpMethod.PUT, requestEntity, Customer.class );
        System.out.println(String.format("测试结果update cus 为：%s", responseUpdate.getBody()));

        cus1 = responseUpdate.getBody();
        requestEntity = new HttpEntity<>(cus1);
        responseUpdate = restTemplate.exchange(
                this.base.toString() + "/customers"+"/"+cus1.getId(),
                HttpMethod.PUT, requestEntity, Customer.class );
        System.out.println(String.format("测试结果update cus 为：%s", responseUpdate.getBody()));


        restTemplate.delete(
                this.base.toString() + "/customerGroups/2");



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
