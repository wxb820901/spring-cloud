package com.aws.demo;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class SNSTest {
    public static final AmazonSNS sns = AmazonSNSClient.builder()
                .withRegion("us-west-1")
                .build();
    public static String topicArn;
    public static final String topicName = "major-topic";
    @BeforeClass
    public static void beforeClass(){

    }

    @Test
    public void testAddTopicPutMsgDeleteTopic(){


        CreateTopicRequest create = new CreateTopicRequest()
                .withName("major-topic");
        CreateTopicResult createResult = sns.createTopic(create);
        topicArn = createResult.getTopicArn();
        assertNotNull("verify returned topic ARN", topicArn);

        ListTopicsResult listTopicResult = sns.listTopics();
        assertEquals("after insert topic list should contain 1 item",1,listTopicResult.getTopics().size());
        assertEquals("after insert topic list should contain before inserted topic arn", topicArn,
                listTopicResult.getTopics().get(0).getTopicArn());

        SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, "email", "bill_wang@epam.com");
        sns.subscribe(subscribeRequest);

        String msg = "If you receive this message, publishing a message to an Amazon SNS topic works.";
        PublishRequest publishRequest = new PublishRequest(topicArn,"email", msg);
        PublishResult publishResult = sns.publish(publishRequest);

//
//
//        DeleteTopicResult deleteResult = sns.deleteTopic(topicArn);
//        assertNotNull("verify returned delete result", deleteResult);

//        ListTopicsResult listTopicsAfterDeletion = sns.listTopics();
//        assertEquals("topic list should contain zero items after deletion",0,listTopicsAfterDeletion.getTopics().size());
    }
    @AfterClass
    public static void afterClass(){
        for(Topic topic : sns.listTopics().getTopics()) {
            DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(topic.getTopicArn());
            sns.deleteTopic(deleteTopicRequest);
        }
    }
}
