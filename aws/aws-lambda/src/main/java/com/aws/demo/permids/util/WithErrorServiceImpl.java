package com.aws.demo.permids.util;

import java.util.ArrayList;
import java.util.List;

public class WithErrorServiceImpl implements WithErrorService{

    private List<String> exceptions;

    public WithErrorServiceImpl(){
        exceptions = new ArrayList();
    }

    @Override
    public List<String> getExceptions() {
        return exceptions;
    }

    @Override
    public void withException(Exception e) {
        exceptions.add(e.getMessage());
    }
    @Override
    public void resetExceptions() {
        this.exceptions = new ArrayList();
    }
}
