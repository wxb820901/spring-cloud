package com.aws.demo.permids.util;

import org.json.simple.JSONObject;

import java.io.*;

public interface InputParserService<T> {
    T parse(InputStream inputStream, WithErrorService withErrorService);
}
