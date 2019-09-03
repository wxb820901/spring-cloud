package com.aws.demo.permids.services.dynamodb;

import com.aws.demo.permids.services.BatchPermIdService;
import com.aws.demo.permids.services.PermIdService;
import com.aws.demo.permids.services.ResetPermIdService;

public interface DynamoDBService<T, A> extends ResetPermIdService, PermIdService<T, A>, BatchPermIdService<T, A> {
}
