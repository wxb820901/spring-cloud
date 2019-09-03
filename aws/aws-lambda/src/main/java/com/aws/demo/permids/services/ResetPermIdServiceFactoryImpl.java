package com.aws.demo.permids.services;

import com.aws.demo.permids.services.dynamodb.DynamoDBServiceImpl;
import com.aws.demo.permids.services.s3.S3ServiceImpl;
import com.aws.demo.permids.services.setting.DataSource;

public class ResetPermIdServiceFactoryImpl implements ResetPermIdServiceFactory<DataSource, ResetPermIdService> {
    @Override
    public ResetPermIdService getResetPermIdService(DataSource dataSource) {
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
