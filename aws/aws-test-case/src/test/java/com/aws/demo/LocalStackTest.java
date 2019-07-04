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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;

public class LocalStackTest {

    public static Logger logger = LoggerFactory.getLogger(LocalStackTest.class);

//
//    public static DockerComposeContainer compose = new DockerComposeContainer(
//            new File("src/test/resources/docker-compose.yml"))
//            .withLogConsumer("localstack", new Slf4jLogConsumer(logger))
//            .waitingFor("localstack", Wait.forLogMessage(".*Ready\\.\n", 1))
//            .withLocalCompose(true);;
//


    public static LocalStackContainer localstack = new LocalStackContainer();
    public static AmazonS3 s3;
    public static AmazonSQS sqs;
    public static AmazonSNS sns;
    public static AWSLambda lambda;
    @BeforeClass
    public static void prepareEnv(){

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
    /*
aws s3api create-bucket --bucket 'demo-input-bucket-1' --endpoint-url 'http://localhost:4572'
aws s3api create-bucket --bucket 'demo-output-bucket-1' --endpoint-url 'http://localhost:4572'
aws s3api put-object --bucket 'demo-input-bucket-1' --key 'b.txt' --body 'C:/Users/bill_wang/b.txt' --endpoint-url 'http://localhost:4572'
aws sns create-topic --name 'demo-sns-created-by-cli-1130' --endpoint-url 'http://localhost:4575'
aws lambda create-function --region us-east-1 --function-name api-test-handler --runtime java8 --handler com.aws.demo.HelloS3::handleRequest --memory-size 128 --zip-file fileb://C:/Users/bill_wang/.m2/repository/com/example/aws-lambda/0.0.1-SNAPSHOT/aws-lambda-0.0.1-SNAPSHOT.jar --role arn:aws:iam::123456:role/role-name --endpoint-url=http://localhost:4574
aws lambda create-event-source-mapping  --event-source-arn 'arn:aws:sns:us-east-1:123456789012:demo-sns-created-by-cli-1130' --function-name 'api-test-handler'       --endpoint-url 'http://localhost:4574'
aws sns publish --message 'hello123' --topic-arn 'arn:aws:sns:us-east-1:123456789012:demo-sns-created-by-cli-1130' --endpoint-url 'http://localhost:4575'



    * */
    public static final String COMMAND_AWS = "C:/Program Files/Amazon/AWSCLI/bin/aws.exe";
    public static final String COMMAND_S3 = "s3api";
    public static final String COMMAND_SNS = "sns";
    public static final String COMMAND_LAMBDA = "lambda";
    public static final String COMMAND_CREATE_BUCKET = "create-bucket";
    public static final String COMMAND_BUCKET_NAME = "--bucket";
    public static final String COMMAND_PUT_OBJECT = "put-object";
    public static final String COMMAND_CREATE_TOPIC = "create-topic";
    public static final String COMMAND_CREATE_FUNCTION = "create-function";
    public static final String COMMAND_CREATE_EVENT_SOURCE_MAPPING = "create-event-source-mapping";
    public static final String COMMAND_PUBLISH = "publish ";
    public static final String COMMAND_KEY = "--key";
    public static final String COMMAND_ENDPOINT_URL = "--endpoint-url";
    public static final String COMMAND_NAME = "--name";
    public static final String COMMAND_BODY = "--body";
    public static final String COMMAND_REGION = "--region";
    public static final String COMMAND_FUNCTION_NAME = "--function-name";
    public static final String COMMAND_RUN_TIME = "--runtime";
    public static final String COMMAND_HANDLER ="--handler ";
    public static final String COMMAND_MEMORY_SIZE="--memory-size ";
    public static final String COMMAND_ZIP_FILE = "--zip-file";
    public static final String COMMAND_ROLE_ARN = "--role arn";
    public static final String COMMAND_EVENT_SRC_ARN = " --event-source-arn";
    public static final String COMMAND_MESSAGE = "--message";
    public static final String COMMAND_TOPIC_ARN = "--topic-arn";

    public static final String BUCKET_INPUT_NAME = "demo-input-bucket-1";
    public static final String BUCKET_OUTPUT_NAME = "demo-output-bucket-1";
    public static final String BUCKET_OBJECT_KEY = "b.txt";
    public static final String BUCKET_OBJECT_PATH = "C:/Users/bill_wang/b.txt";
    public static final String ENDPOINT_URL_S3 = "http://localhost:4572";
    public static final String ENDPOINT_URL_SNS = "http://localhost:4575";
    public static final String ENDPOINT_URL_LAMBDA = "http://localhost:4574";
    public static final String TOPIC_NAME = "demo-sns-created-by-cli-1130";
    public static final String REGION = "us-east-1";
    public static final String FUNCTION_NAME = "api-test-handler";
    public static final String RUN_TIME = "java8";
    public static final String HANDLER = "com.aws.demo.HelloS3::handleRequest";
    public static final String MEMORY_SIZE ="128";
    public static final String ZIP_FILE_PATH = "fileb://C:/Users/bill_wang/.m2/repository/com/example/aws-lambda/0.0.1-SNAPSHOT/aws-lambda-0.0.1-SNAPSHOT.jar";
    public static final String ROLE_ARN = "arn:aws:iam::123456:role/role-name";
    public static final String EVENT_SRC_ARN = "arn:aws:sns:us-east-1:123456789012:demo-sns-created-by-cli-1130";
    public static final String MESSAGE = "hello123";
    public static final String TOPIC_ARN = "arn:aws:sns:us-east-1:123456789012:demo-sns-created-by-cli-1130";
    @Test
    public void testLambdaConnection() throws IOException {
        waitForProcess(COMMAND_AWS,COMMAND_ENDPOINT_URL,ENDPOINT_URL_S3,    COMMAND_S3,    COMMAND_CREATE_BUCKET,              COMMAND_BUCKET_NAME, BUCKET_INPUT_NAME);
        waitForProcess(COMMAND_AWS,COMMAND_ENDPOINT_URL,ENDPOINT_URL_S3,    COMMAND_S3,    COMMAND_CREATE_BUCKET,              COMMAND_BUCKET_NAME, BUCKET_OUTPUT_NAME);
        waitForProcess(COMMAND_AWS,COMMAND_ENDPOINT_URL,ENDPOINT_URL_S3,    COMMAND_S3,    COMMAND_PUT_OBJECT,                 COMMAND_BUCKET_NAME, BUCKET_INPUT_NAME,COMMAND_KEY,BUCKET_OBJECT_KEY,COMMAND_BODY, BUCKET_OBJECT_PATH );
        assertTrue(isObjExist(BUCKET_INPUT_NAME, "b.txt"));
        waitForProcess(COMMAND_AWS,COMMAND_ENDPOINT_URL,ENDPOINT_URL_SNS,   COMMAND_SNS,   COMMAND_CREATE_TOPIC,               COMMAND_NAME, TOPIC_NAME);
        waitForProcess(COMMAND_AWS,COMMAND_ENDPOINT_URL,ENDPOINT_URL_LAMBDA,COMMAND_LAMBDA,COMMAND_CREATE_FUNCTION,            COMMAND_REGION,REGION, COMMAND_FUNCTION_NAME, FUNCTION_NAME ,COMMAND_RUN_TIME,RUN_TIME ,COMMAND_HANDLER, HANDLER ,COMMAND_MEMORY_SIZE,MEMORY_SIZE,COMMAND_ZIP_FILE, ZIP_FILE_PATH, COMMAND_ROLE_ARN, ROLE_ARN );
        waitForProcess(COMMAND_AWS,COMMAND_ENDPOINT_URL,ENDPOINT_URL_LAMBDA,COMMAND_LAMBDA,COMMAND_CREATE_EVENT_SOURCE_MAPPING,COMMAND_EVENT_SRC_ARN,EVENT_SRC_ARN, COMMAND_FUNCTION_NAME,FUNCTION_NAME       );
        waitForProcess(COMMAND_AWS,COMMAND_ENDPOINT_URL,ENDPOINT_URL_SNS,   COMMAND_SNS,   COMMAND_PUBLISH,                    COMMAND_MESSAGE,MESSAGE ,COMMAND_TOPIC_ARN,TOPIC_ARN );



    }






    private boolean isObjExist(String bucketName, String prefix){
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
                .withBucketName(bucketName)
                .withPrefix(prefix));
        return objectListing.getObjectSummaries().size() != 0;
    }


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



    public static void waitForProcess(String ... commands) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = pb.start();
        while(p.isAlive()) {
            try {
                new Thread().join(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(String letter : commands){
            System.out.print(letter+" ");
        }
        System.out.println("over");

    }
}
