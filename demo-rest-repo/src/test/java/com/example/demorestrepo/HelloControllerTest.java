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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

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

        CustomerGroup cg1 = new CustomerGroup("group1");
        Customer cus1 = new Customer("fn1","ln1", cg1);
        ResponseEntity<Customer> responseCus = this.restTemplate.postForEntity(
                this.base.toString() + "/customers", cus1, Customer.class);
        System.out.println(String.format("测试结果post cus 为：%s", responseCus.getBody()));



        ResponseEntity<String> response4 = this.restTemplate.getForEntity(
                this.base.toString() + "/customerGroups", String.class);
        System.out.println(String.format("测试结果customerGroups为：%s", response4.getBody()));


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
