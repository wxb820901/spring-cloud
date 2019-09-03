package com.aws.demo.permids.services;

public interface PermIdServiceFactory<T, S> {
    S getPermIdService(T condition);
}
