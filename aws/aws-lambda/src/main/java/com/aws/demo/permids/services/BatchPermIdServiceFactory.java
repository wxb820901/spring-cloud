package com.aws.demo.permids.services;

public interface BatchPermIdServiceFactory<T, S> {
    S getBatchPermIdService(T condition);
}
