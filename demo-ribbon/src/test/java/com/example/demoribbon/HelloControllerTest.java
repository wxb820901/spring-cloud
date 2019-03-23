package com.example.demoribbon;

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
                    .withExposedService("rest-repo1", 59201, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(1200)))
                    .withExposedService("ribbon", 59601, Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(1200)))//Wait.forLogMessage(".*Started DemoRibbonApplication.*\\n", 1)
                    .withLocalCompose(true);

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void test1() {
        ResponseEntity<String> response = this.restTemplate.getForEntity(
                "http://localhost:59601/configMsg", String.class, "");
        System.out.println(String.format("测试结果1为：%s", response.getBody()));
    }
}
