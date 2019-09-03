package com.aws.demo.permids.services;

import com.aws.demo.permids.util.WithErrorService;

import java.util.List;

public interface BatchPermIdService<T, A> {
    List<T> getBatchPermId(A type, int batchSize, WithErrorService withErrorService);
}
