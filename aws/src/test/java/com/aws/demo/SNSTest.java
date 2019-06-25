package com.aws.demo;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class SNSTest {
    public static final AmazonSNS sns = AmazonSNSClient.builder()
                .withRegion("us-west-1")
                .build();
    public static String topicArn;
    public static final String topicName = "major-topic-" + UUID.randomUUID();
    public static final String mailAddress = "wxb820901awslearn@126.com";
    @BeforeClass
    public static void beforeClass(){
        clearSubscriber();
        clearTopic();
    }

    @Test
    public void testAddTopicPutMsgDeleteTopic(){

        CreateTopicRequest createTopicRequest = new CreateTopicRequest(topicName);
        CreateTopicResult createResult = sns.createTopic(createTopicRequest);
        topicArn = createResult.getTopicArn();
        assertNotNull("verify returned topic ARN", topicArn);
        // Print the topic ARN.
        System.out.println("TopicArn:" + createResult.getTopicArn());
        // Print the request ID for the CreateTopicRequest action.
        System.out.println("CreateTopicRequest: " + sns.getCachedResponseMetadata(createTopicRequest));

        ListTopicsResult listTopicResult = sns.listTopics();
        assertEquals("after insert topic list should contain 1 item",1,listTopicResult.getTopics().size());
        assertEquals("after insert topic list should contain before inserted topic arn", topicArn,
                listTopicResult.getTopics().get(0).getTopicArn());

        SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "email", mailAddress);
        sns.subscribe(subscribeRequest);
        System.out.println("SubscribeRequest: " + sns.getCachedResponseMetadata(subscribeRequest));
        System.out.println("To confirm the subscription, check your email.");

        String msg = "If you receive this message, publishing a message to an Amazon SNS topic works.";
        PublishRequest publishRequest = new PublishRequest(topicArn, msg);
        PublishResult publishResult = sns.publish(publishRequest);
        System.out.println("MessageId: " + publishResult.getMessageId());

        try {
            Thread.sleep(15*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        DeleteTopicResult deleteResult = sns.deleteTopic(topicArn);
//        assertNotNull("verify returned delete result", deleteResult);
//
//        ListTopicsResult listTopicsAfterDeletion = sns.listTopics();
//        assertEquals("topic list should contain zero items after deletion",0,listTopicsAfterDeletion.getTopics().size());
    }
    @AfterClass
    public static void afterClass(){
        clearSubscriber();
        clearTopic();
    }

    private static void clearTopic(){
        for(Topic topic : sns.listTopics().getTopics()) {
            DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topic.getTopicArn());
            sns.deleteTopic(deleteTopicRequest);
        }
    }
    private static void clearSubscriber(){
        for(Subscription subscription : sns.listSubscriptions().getSubscriptions()) {
            sns.unsubscribe(subscription.getSubscriptionArn());
        }
    }
}
