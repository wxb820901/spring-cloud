package com.example.demoredis;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;


public class HelloControllerTest {

    @Rule
    public DockerComposeContainer compose =
            new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("demo-redis", 59401, Wait.forLogMessage(".*Started DemoRedisApplication.*\\n", 1))
                    .withLocalCompose(true);

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void test1() throws Exception {
        ResponseEntity<String> response = this.restTemplate.postForEntity(
                "http://localhost:59401/person/bill", null, String.class);
        System.out.println(String.format("测试结果1为：%s", response.getBody()));

        response = this.restTemplate.getForEntity(
                "http://localhost:59401/persons", String.class, "");
        System.out.println(String.format("测试结果2为：%s", response.getBody()));
    }
}
