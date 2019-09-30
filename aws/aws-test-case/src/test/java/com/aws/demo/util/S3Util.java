package com.aws.demo.util;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.util.function.Supplier;

public class S3Util {
    public static String getSetting(String envKey, String systemKey, String defaultValue) {
        //env
        String value = System.getenv(envKey);
        //system properties
        if (value == null) {
            value = System.getProperty(systemKey);
        }
        //default
        if (value == null) {
            value = defaultValue;
        }
        if (value == null) {
            throw new UnsupportedOperationException("setting is not exist");
        }

        return value;
    }


    enum AmazonS3Supplier implements Supplier<AmazonS3> {
        INSTANCE,
        INSTANCE_US_EAST_1(Regions.US_EAST_1),
        INSTANCE_US_EAST_2(Regions.US_EAST_2),
        INSTANCE_US_WEST_1(Regions.US_WEST_1),
        INSTANCE_US_WEST_2(Regions.US_WEST_2);

        private final AmazonS3 instance;
        private Regions getRegions(String envKey, String systemKey, String defaultValue) {
            return Regions.valueOf(getSetting(envKey, systemKey, defaultValue));
        }

        AmazonS3Supplier() {//build AmazonS3 by env, properties and default
            instance = AmazonS3ClientBuilder.standard()
                    .withRegion(getRegions("REGION", "REGION", null))
                    .withCredentials(new AWSCredentialsProviderImpl())
                    .withClientConfiguration(getClientConfiguration())
                    .build();
        }

        AmazonS3Supplier(Regions regions){
            if (regions == null) {
                throw new UnsupportedOperationException("region is null");
            }
            instance = AmazonS3ClientBuilder.standard()
                    .withRegion(regions)
                    .withCredentials(new AWSCredentialsProviderImpl())
                    .withClientConfiguration(getClientConfiguration())
                    .build();
        }

        private ClientConfiguration getClientConfiguration() {
            return new ClientConfiguration()
                    .withMaxConnections(Runtime.getRuntime().availableProcessors() * 4)
                    .withConnectionTimeout(10_000)
                    .withMaxErrorRetry(10)
                    .withThrottledRetries(false)
                    .withRequestTimeout(-1)
                    ;
        }

        @Override
        public AmazonS3 get() {
            return instance;
        }
    }
}
