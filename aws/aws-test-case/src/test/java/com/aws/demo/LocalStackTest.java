package com.aws.demo;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.CreateEventSourceMappingRequest;
import com.amazonaws.services.lambda.model.CreateFunctionRequest;
import com.amazonaws.services.lambda.model.FunctionCode;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.sun.deploy.ref.DeployConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import org.testcontainers.shaded.org.glassfish.jersey.internal.util.Base64;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.codehaus.groovy.runtime.DefaultGroovyMethods.any;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;

public class LocalStackTest {

    public static Logger logger = LoggerFactory.getLogger(LocalStackTest.class);

    @ClassRule
    public static DockerComposeContainer compose = new DockerComposeContainer(
            new File("src/test/resources/docker-compose.yml"))
            .withLogConsumer("localstack", new Slf4jLogConsumer(logger))
            .waitingFor("localstack", Wait.forLogMessage(".*Ready\\.\n", 1))
            .withLocalCompose(true);;
//
//    @ClassRule
//    public static



    public static LocalStackContainer localstack;
    public static AmazonS3 s3;
    public static AmazonSQS sqs;
    public static AmazonSNS sns;
    public static AWSLambda lambda;
    @BeforeClass
    public static void prepareEnv(){

        localstack = new LocalStackContainer().withServices(SQS,S3,SNS,LAMBDA);
        s3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4572", Regions.US_EAST_1.getName()))
                .withPathStyleAccessEnabled(true)
                .withCredentials(localstack.getDefaultCredentialsProvider())
                .build();
        sqs = AmazonSQSClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4576", Regions.US_EAST_1.getName()))
                .withCredentials(localstack.getDefaultCredentialsProvider())
                .build();
        sns = AmazonSNSClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4575", Regions.US_EAST_1.getName()))
                .withCredentials(localstack.getDefaultCredentialsProvider())
                .build();
        lambda = AWSLambdaClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4574", Regions.US_EAST_1.getName()))
                .withCredentials(localstack.getDefaultCredentialsProvider())
                .build();

        Assert.assertNotNull(s3);
        Assert.assertNotNull(sqs);
        Assert.assertNotNull(sns);
        Assert.assertNotNull(lambda);
    }


    public static final String bucketName = "demo-s3-bucket-" + UUID.randomUUID();
    public static final String key = "MyObjectKey";
    public static String topicArn;
    public static final String topicName = "major-topic-" + UUID.randomUUID();
    public static final String mailAddress = "wxb820901awslearn@126.com";


    @Test
    public void testS3Connection() throws IOException {
        Bucket bucket = s3.createBucket(bucketName);
        putOnS3();
    }
    @Test
    public void testLambdaConnection() throws IOException {
        Bucket bucketInput = s3.createBucket("demo-input-bucket-1"+ UUID.randomUUID());
        Bucket bucketOutput = s3.createBucket("demo-output-bucket-1"+ UUID.randomUUID());
        CreateQueueRequest createQueueRequest = new CreateQueueRequest("trigger-lambda");


        DeployConfig deployConfig = new DeployConfig("location", "description", "function", "handler", 1024, "role", "runtime", 30, "full", false, null, false, new ArrayList<String>(), new ArrayList<String>());
        File file = new File("path");

        when(zipper.getZip(any(String.class))).thenReturn(file);
        when(service.deployLambda(any(DeployConfig.class), any(FunctionCode.class), any(UpdateModeValue.class)))
                .thenReturn(false);

        LambdaUploader uploader = new LambdaUploader(service, zipper, logger);
        Boolean uploaded = uploader.upload(deployConfig);

        verify(logger, times(1)).log("%nStarting lambda deployment procedure");
        verify(service, times(1)).deployLambda(eq(deployConfig), any(FunctionCode.class), eq(UpdateModeValue.Full));
        assertFalse(uploaded);


        CreateFunctionRequest createFunctionRequest = new CreateFunctionRequest();
        createFunctionRequest.setHandler("com.aws.demo::HelloSQS");
        createFunctionRequest.set
        lambda.createFunction(createFunctionRequest);
        CreateEventSourceMappingRequest createEventSourceMappingRequest = new CreateEventSourceMappingRequest();
        lambda.createEventSourceMapping(createEventSourceMappingRequest);

    }


    @Test
    public void testSQSConnection() throws IOException {

        // Create a queue
        CreateQueueRequest createQueueRequest =
                new CreateQueueRequest("MyQueue");
        String myQueueUrl = sqs.createQueue(createQueueRequest)
                .getQueueUrl();

        sqs.sendMessage(new SendMessageRequest(myQueueUrl,
                "This is my message text."));


        ReceiveMessageRequest receiveMessageRequest =
                new ReceiveMessageRequest(myQueueUrl);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest)
                .getMessages();
        Assert.assertNotNull(messages);
        assertFalse(messages.isEmpty());

    }

    @Test
    public void testSQSTriggerLambdaToGetPutS3(){
        sqs.
    }

    public String putOnS3() throws IOException {
        //put and get
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, Base64.encodeAsString(FileUtils.readFileToByteArray(createSampleFile())));
        System.out.println(" Base64.encodeAsString: "  + Base64.encodeAsString(FileUtils.readFileToByteArray(createSampleFile())));
        s3.putObject(putObjectRequest);
        S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
        Assert.assertNotNull(object);
//        displayTextInputStream(object.getObjectContent().getDelegateStream());
        return key;
    }



//    private boolean isObjExist(String prefix){
//        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
//                .withBucketName(bucketName)
//                .withPrefix(prefix));
//        return objectListing.getObjectSummaries().size() != 0;
//    }


    private File createSampleFile() throws IOException {

        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("01234567890112345678901234\n");
        writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
        writer.write("01234567890112345678901234\n");
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.close();
        return file;

    }



    private void displayTextInputStream(InputStream input) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            logger.info("    " + line);
        }

        System.out.println();
    }

//    private boolean isObjExist(String prefix){
//        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
//                .withBucketName(bucketName)
//                .withPrefix(prefix));
//        return objectListing.getObjectSummaries().size() != 0;
//    }
}
