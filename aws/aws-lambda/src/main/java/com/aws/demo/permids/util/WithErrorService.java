package com.aws.demo.permids.util;

import java.util.List;

public interface WithErrorService {
    List<String> getExceptions();
    void withException(Exception e);
    void resetExceptions();
}
