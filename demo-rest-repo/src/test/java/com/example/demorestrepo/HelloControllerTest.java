package com.example.demorestrepo;

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

    /**
     * 向"/test"地址发送请求，并打印返回结果
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {

        ResponseEntity<String> response = this.restTemplate.getForEntity(
                this.base.toString() + "/configMsg", String.class, "");
        System.out.println(String.format("测试结果1为：%s", response.getBody()));

        ResponseEntity<String> response1 = this.restTemplate.getForEntity(
                this.base.toString() + "/customers", String.class, "");
        System.out.println(String.format("测试结果2为：%s", response1.getBody()));

        ResponseEntity<String> response2 = this.restTemplate.getForEntity(
                this.base.toString() + "/customers/1", String.class, "");
        System.out.println(String.format("测试结果3为：%s", response2.getBody()));
    }
}
