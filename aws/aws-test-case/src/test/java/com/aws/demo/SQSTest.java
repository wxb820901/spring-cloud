package com.aws.demo;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import com.amazonaws.services.sqs.AmazonSQS;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class SQSTest {
    @BeforeClass
    public static void beforeClass(){

    }

    //SQS GURD msg send/received
    @Test
    public void test(){
        final AmazonSQS sqs = AmazonSQSClientBuilder.standard().withRegion(Regions.US_WEST_2).build();

        try {
            // Create a queue.
            System.out.println("Creating a new SQS queue called MyQueue.\n");
            final CreateQueueRequest createQueueRequest =
                    new CreateQueueRequest("MyQueue");
            final String myQueueUrl = sqs.createQueue(createQueueRequest)
                    .getQueueUrl();

            // List all queues.
            System.out.println("Listing all queues in your account.\n");
            for (final String queueUrl : sqs.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();

            // Send a message.
            System.out.println("Sending a message to MyQueue.\n");
            sqs.sendMessage(new SendMessageRequest(myQueueUrl,
                    "This is my message text."));

            // Receive messages.
            System.out.println("Receiving messages from MyQueue.\n");
            final ReceiveMessageRequest receiveMessageRequest =
                    new ReceiveMessageRequest(myQueueUrl);
            final List<Message> messages = sqs.receiveMessage(receiveMessageRequest)
                    .getMessages();
            for (final Message message : messages) {
                System.out.println("Message");
                System.out.println("  MessageId:     " + message.getMessageId());
                System.out.println("  ReceiptHandle: " + message.getReceiptHandle());
                System.out.println("  MD5OfBody:     " + message.getMD5OfBody());
                System.out.println("  Body:          " + message.getBody());
                for (final Map.Entry<String, String> entry : message.getAttributes() .entrySet()) {
                    System.out.println("Attribute");
                    System.out.println("  Name:  " + entry.getKey());
                    System.out.println("  Value: " + entry.getValue());
                }
            }
            System.out.println();

            // Delete the message.
            System.out.println("Deleting a message.\n");
            final String messageReceiptHandle = messages.get(0).getReceiptHandle();
            sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl,
                    messageReceiptHandle));

            // Delete the queue.
            System.out.println("Deleting the test queue.\n");
            sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
        } catch (final AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (final AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means " +
                    "the client encountered a serious internal problem while " +
                    "trying to communicate with Amazon SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }
}
