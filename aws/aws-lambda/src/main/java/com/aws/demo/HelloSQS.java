package com.aws.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.joda.time.DateTime;

import java.io.*;

public class HelloSQS implements
        RequestHandler<SQSEvent, String> {

    @Override
    public String handleRequest(SQSEvent sqsEvent, Context context) {
        LambdaLogger logger = context.getLogger();
        String message = new String(sqsEvent.getRecords().get(0).getBody().getBytes());

        String dstBucket = "demo-output-bucket-1";

        DateTime dt = new DateTime();
        String dstKey = "SQS-"
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
