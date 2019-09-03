package com.aws.demo.permids.services;

import com.aws.demo.permids.services.dynamodb.DynamoDBServiceImpl;
import com.aws.demo.permids.services.s3.S3ServiceImpl;
import com.aws.demo.permids.services.setting.DataSource;

public class BatchPermIdServiceFactoryImpl implements BatchPermIdServiceFactory<DataSource, BatchPermIdService> {
    @Override
    public BatchPermIdService getBatchPermIdService(DataSource dataSource) {
        switch(dataSource){
            case S3:
                return new S3ServiceImpl();
            case DYNAMODB:
                return new DynamoDBServiceImpl();
            default:
                throw new UnsupportedOperationException(dataSource.name() + " is not supported ");
        }
    }


}
