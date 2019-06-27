package com.aws.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import java.io.*;
import java.util.Calendar;

public class HelloSNS implements
        RequestHandler<SNSEvent, String> {

    @Override
    public String handleRequest(SNSEvent snsEvent, Context context) {
        LambdaLogger logger = context.getLogger();
        String message = snsEvent.getRecords().get(0).getSNS().getMessage();

        String dstBucket = "demo-output-bucket-1";

        DateTime dt = new DateTime();
        String dstKey = "SNS-"
                + dt.getYear()
                + dt.getMonthOfYear()
                + dt.getDayOfMonth()
                + dt.getHourOfDay()
                + dt.getMinuteOfHour()
                + dt.getSecondOfMinute()
                + dt.getMillisOfSecond();
        logger.log("dstKey ==> " + dstKey);

        AmazonS3 s3Client = new AmazonS3Client();
        try {
            s3Client.putObject(dstBucket, dstKey, createSampleFile(message));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File createSampleFile(String message) throws IOException {
        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();
        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("lambda begin \n");
        writer.write(message+"\n");
        writer.write("\nlambda end");
        writer.close();
        return file;
    }
}
