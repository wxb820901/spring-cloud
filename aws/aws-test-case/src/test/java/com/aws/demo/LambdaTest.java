package com.aws.demo;

import cloud.localstack.LocalstackTestRunner;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.nio.charset.Charset;

import static com.aws.demo.LocalStackTest.localstack;

//@RunWith(LocalstackTestRunner.class)
public class LambdaTest {

//    public static Logger logger = LoggerFactory.getLogger(LocalStackTest.class);

//    @ClassRule
//    public static DockerComposeContainer compose = new DockerComposeContainer(
//            new File("src/test/resources/dc-macos.yml"))
//            .withLogConsumer("localstack", new Slf4jLogConsumer(logger))
//            .waitingFor("localstack", Wait.forLogMessage(".*Ready\\.\n", 1))
//            .withLocalCompose(true);
    @Test
    public void test() {
        String functionName = "api-hello-context";
        AWSLambda lambdaClient = AWSLambdaClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration("http://localhost:4574", Regions.US_EAST_1.getName()))
                .build();
        String returnDetails = null;

        try {
            InvokeRequest invokeRequest = new InvokeRequest();
            invokeRequest.setFunctionName(functionName);
            invokeRequest.setPayload("");
            returnDetails = new String(
                    lambdaClient.invoke(invokeRequest).getPayload().array(),
                    Charset.forName("UTF-8"));
            System.out.println(returnDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
