package com.aws.demo;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;


import java.io.*;
import java.net.URLDecoder;

public class HelloS3 implements
        RequestHandler<S3Event, String> {

    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        LambdaLogger logger = context.getLogger();
        try {
            S3EventNotificationRecord record = s3Event.getRecords().get(0);
            String srcBucket = "demo-input-bucket-1";
            String srcKey = record.getS3().getObject().getKey();
            logger.log("srcKey ==> "+srcKey);
            String dstBucket = "demo-output-bucket-1";
            String dstKey = srcKey+"-backup";
            logger.log("dstKey ==> "+dstKey);

            AmazonS3 s3Client = new AmazonS3Client();
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(
                    srcBucket, srcKey));

            // Uploading to S3 destination bucket
            System.out.println("Writing to: " + dstBucket + "/" + dstKey);
            s3Client.putObject(dstBucket, dstKey, createSampleFile(s3Object));

            System.out.println("Successfully resized " + srcBucket + "/"
                    + srcKey + " and uploaded to " + dstBucket + "/" + dstKey);
            return "Ok";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private File createSampleFile(S3Object s3Object) throws IOException {


        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("lambda begin \n");
        writer.write(IOUtils.toString( s3Object.getObjectContent()));
        writer.write("\nlambda end");
        writer.close();
        return file;

    }
}
