package com.aws.demo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class S3Test {
    public static AmazonS3 s3 = new AmazonS3Client();
    public static final String bucketName = "demo-s3-bucket-" + UUID.randomUUID();
    public static final String key = "MyObjectKey";
    @BeforeClass
    public static void beforeClass(){
        Region usWest2 = Region.getRegion(Regions.US_EAST_2);
        s3.setRegion(usWest2);
    }
    @AfterClass
    public static void afterClass(){
//        s3.deleteBucket(bucketName);
    }
    @Test
    public void testCreateS3PutObjectDeleteObject() throws IOException {
        assertTrue(isS3Exist("demo-bucket-20190625-1"));
//        //create s2
//        s3.createBucket(bucketName);
//        assertTrue(isS3Exist(bucketName));
//
//        //put object
//        s3.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));
//        S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
//        System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
//        assertTrue(isS3Exist(bucketName));
//        displayTextInputStream(object.getObjectContent());
//        assertTrue(isObjExist("My"));
//
//        //delete
//        s3.deleteObject(bucketName, key);
//        assertFalse(isObjExist(key));
    }

    //for real case
    @Test
    public void testConnectionWithSQS(){

    }
    @Test
    public void testConnectionWithSNS(){

    }
    @Test
    public void testConnectionWithLambda(){

    }

    private boolean isS3Exist(String bucketName){
        for (Bucket bucket : s3.listBuckets()) {
            System.out.println("==>"+bucket.getName());
            if(bucket.getName().equals(bucketName)){
                return true;
            }
        }
        return false;
    }

    private boolean isObjExist(String prefix){
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
            System.out.println("    " + line);
        }

        System.out.println();
    }
}
