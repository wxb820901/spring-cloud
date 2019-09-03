package com.aws.demo.permids.services.s3;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.aws.demo.permids.util.WithErrorService;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.List;
@Deprecated
public class S3ServiceImpl implements S3Service<String, String> {

    @Override
    public void init(JSONObject json, Context context) {

    }

    @Override
    public List<String> getBatchPermId(String type, int batchSize, WithErrorService withErrorService) {
        return null;
    }

    @Override
    public List<String>  getPermId(String type, WithErrorService withErrorService) {
        return null;
    }




//below are demo for s3 access, give up and leave them here
    @Deprecated private static final String bucketName = "demo-id";
    @Deprecated private static final String key = "range1/ids.txt";
    @Deprecated
    public String getAndUpdateId() throws Exception {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
        S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, key));
        String content = getTextFromInputStream(object.getObjectContent());
        String[] idAndRange = content.split(",");
        if (idAndRange.length == 2) {
            s3Client.putObject(new PutObjectRequest(bucketName, key, getInputStreamFromText((Integer.parseInt(idAndRange[0]) + 1) + "," + idAndRange[1]), new ObjectMetadata()));
        } else {
            throw new Exception(content + " is not correct content");
        }
        return idAndRange[0];
    }
    @Deprecated
    private String getTextFromInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder temp = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            temp.append(line);
        }
        return temp.toString();
    }
    @Deprecated
    private InputStream getInputStreamFromText(String text) {
        return new ByteArrayInputStream(text.getBytes());
    }


}
