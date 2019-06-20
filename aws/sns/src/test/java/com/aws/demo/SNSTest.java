package com.aws.demo;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class SNSTest {

    //SNS create add subscribe and publish msg
    @Test
    public void test(){
        AmazonSNS snsClient = AmazonSNSClient.builder()
                .withRegion("us-west-1")
                .build();
        // Create an Amazon SNS topic.
        final CreateTopicRequest createTopicRequest = new CreateTopicRequest("MyTopic");
        final CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);

        // Print the topic ARN.
        System.out.println("TopicArn:" + createTopicResult.getTopicArn());

        // Print the request ID for the CreateTopicRequest action.
        System.out.println("CreateTopicRequest: " + snsClient.getCachedResponseMetadata(createTopicRequest));


        //list SNS
        final ListTagsForResourceRequest listTagsForResourceRequest = new ListTagsForResourceRequest();
        listTagsForResourceRequest.setResourceArn(createTopicResult.getTopicArn());
        final ListTagsForResourceResult listTagsForResourceResult = snsClient.listTagsForResource(listTagsForResourceRequest);
        System.out.println(String.format("ListTagsForResource: \tTags for topic %s are %s.\n",
                createTopicResult.getTopicArn(), listTagsForResourceResult.getTags()));


//        //tag
//        final Tag tagTeam = new Tag();
//        tagTeam.setKey("Team");
//        tagTeam.setValue("Development");
//        final Tag tagEnvironment = new Tag();
//        tagEnvironment.setKey("Environment");
//        tagEnvironment.setValue("Gamma");
//
//        final List<Tag> tagList = new ArrayList<>();
//        tagList.add(tagTeam);
//        tagList.add(tagEnvironment);
//
//        final TagResourceRequest tagResourceRequest = new TagResourceRequest();
//        tagResourceRequest.setResourceArn(createTopicResult.getTopicArn());
//        tagResourceRequest.setTags(tagList);
//        final TagResourceResult tagResourceResult = snsClient.tagResource(tagResourceRequest);
//
//        System.out.println("tagResourceResult ==> " + tagResourceResult);
//
//        //untag
//        final UntagResourceRequest untagResourceRequest = new UntagResourceRequest();
//        untagResourceRequest.setResourceArn(createTopicResult.getTopicArn());
//        final List<String> tagKeyList = new ArrayList<>();
//        tagKeyList.add("Team");
//        untagResourceRequest.setTagKeys(tagKeyList);
//        final UntagResourceResult untagResourceResult = snsClient.untagResource(untagResourceRequest);
//
//        System.out.println("untagResourceResult ==> " + untagResourceResult);



        // Subscribe an email endpoint to an Amazon SNS topic.
        SubscribeRequest subscribeRequest = new SubscribeRequest(createTopicResult.getTopicArn(), "email", "bill_wang@epam.com");
        snsClient.subscribe(subscribeRequest);

//        // Print the request ID for the SubscribeRequest action.
//        System.out.println("SubscribeRequest: " + snsClient.getCachedResponseMetadata(subscribeRequest));
//        System.out.println("To confirm the subscription, check your email.");

        // Publish a message to an Amazon SNS topic.
        String msg = "If you receive this message, publishing a message to an Amazon SNS topic works.";
        PublishRequest publishRequest = new PublishRequest(createTopicResult.getTopicArn(),"email", msg);
        PublishResult publishResponse = snsClient.publish(publishRequest);

//        // Print the MessageId of the message.
//        System.out.println("MessageId: " + publishResponse.getMessageId());
//        System.out.println("SdkResponseMetadata: " + publishResponse.getSdkResponseMetadata());


        // Delete an Amazon SNS topic.
        final DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest(createTopicResult.getTopicArn());
        snsClient.deleteTopic(deleteTopicRequest);
//        // Print the request ID for the DeleteTopicRequest action.
//        System.out.println("DeleteTopicRequest: " + snsClient.getCachedResponseMetadata(deleteTopicRequest));



    }
}
