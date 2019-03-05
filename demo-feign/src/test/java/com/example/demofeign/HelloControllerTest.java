package com.example.demofeign;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.net.URL;
import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoFeignApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application.properties")
public class HelloControllerTest {


    @ClassRule
    public static DockerComposeContainer compose =
            new DockerComposeContainer(
                    new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("eureka", 8762, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(3000)))
                    .withExposedService("config", 59001, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(3000)))
                    .withExposedService("rest-repo1", 59201, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(3000)))
//                    .withLocalCompose(true)
                    ;


    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        System.out.println(String.format("测试结果0为：%s", url));
        this.base = new URL(url);

    }

    @Test
    public void test1(){
        ResponseEntity<String> response = this.restTemplate.getForEntity(
                this.base.toString() + "/f", String.class, "");
        System.out.println(String.format("测试结果1为：%s", response.getBody()));
    }
}
