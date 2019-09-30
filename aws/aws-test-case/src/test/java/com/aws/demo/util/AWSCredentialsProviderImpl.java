package com.aws.demo.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;

public class AWSCredentialsProviderImpl implements AWSCredentialsProvider {

    public static final String AWS_ACCESS_KEY_ID = "AWS_ACCESS_KEY_ID";
    public static final String AWS_SECRET_KEY = "AWS_SECRET_KEY";

    private AWSCredentials credentials;

    public AWSCredentialsProviderImpl() {
        super();
        refresh();
    }


    @Override
    public AWSCredentials getCredentials() {
        return credentials;
    }

    @Override
    public void refresh() {
        credentials = new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return S3Util.getSetting(AWS_ACCESS_KEY_ID, AWS_ACCESS_KEY_ID, null);
            }

            @Override
            public String getAWSSecretKey() {
                return S3Util.getSetting(AWS_SECRET_KEY, AWS_SECRET_KEY, null);
            }
        };
    }


}
