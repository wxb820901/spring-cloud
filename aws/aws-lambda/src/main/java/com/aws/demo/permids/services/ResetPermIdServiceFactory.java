package com.aws.demo.permids.services;

public interface ResetPermIdServiceFactory<T, S> {
    S getResetPermIdService(T condition);
}
