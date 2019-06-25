package com.aws.demo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.*;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class KinesisTest {
    /*
    current test case PUT message to kinesis data firehose and check s3
     */
    @Test
    public void testPutMsgToKinesisDataFirehose(){

        //--------------------
        AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();

        clientBuilder.setRegion(Regions.US_WEST_1.getName());
//        clientBuilder.setCredentials(credentialsProvider);
//        clientBuilder.setClientConfiguration(config);

        AmazonKinesis kinesisClient = clientBuilder.build();

        PutRecordsRequest putRecordsRequest  = new PutRecordsRequest();
        putRecordsRequest.setStreamName("demo-delivery-stream-1");
        List<PutRecordsRequestEntry> putRecordsRequestEntryList  = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PutRecordsRequestEntry putRecordsRequestEntry  = new PutRecordsRequestEntry();
            putRecordsRequestEntry.setData(ByteBuffer.wrap(String.valueOf(i).getBytes()));
            putRecordsRequestEntry.setPartitionKey(String.format("partitionKey-%d", i));
            putRecordsRequestEntryList.add(putRecordsRequestEntry);
        }

        putRecordsRequest.setRecords(putRecordsRequestEntryList);
        PutRecordsResult putRecordsResult  = kinesisClient.putRecords(putRecordsRequest);
        System.out.println("Put Result" + putRecordsResult);

    }
}
