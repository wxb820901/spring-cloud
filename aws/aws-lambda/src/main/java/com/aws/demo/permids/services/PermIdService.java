package com.aws.demo.permids.services;

import com.aws.demo.permids.util.WithErrorService;

import java.util.List;

public interface PermIdService<T, A> {
    List<T> getPermId(A type, WithErrorService withErrorService);
}
