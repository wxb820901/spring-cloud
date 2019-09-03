package com.aws.demo.permids.services.s3;


import com.aws.demo.permids.services.BatchPermIdService;
import com.aws.demo.permids.services.PermIdService;
import com.aws.demo.permids.services.ResetPermIdService;
@Deprecated
public interface S3Service<T, A> extends ResetPermIdService, PermIdService<T, A>, BatchPermIdService<T, A> {

}
