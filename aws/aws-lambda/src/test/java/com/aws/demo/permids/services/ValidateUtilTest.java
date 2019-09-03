package com.aws.demo.permids.services;

import com.aws.demo.permids.util.*;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ValidateUtilTest {
    WithErrorService withErrorService = new WithErrorServiceImpl();
    InputParserService inputParserService = new InputParserServiceImpl();
    @Test
    public void testBatchPermId() throws IOException {
        String jsonString = FileUtils.readFileToString(
                new File(
                        this.getClass().getResource("/lambda-batch-permid-input.json").getFile()
                ), "utf-8");
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());

        JSONObject json = (JSONObject) inputParserService.parse(inputStream, withErrorService);
        Assert.assertTrue(ValidateUtil.validateInputJson(json, withErrorService));

    }

    @Test
    public void testPermId() throws IOException {
        String jsonString = FileUtils.readFileToString(
                new File(
                        this.getClass().getResource("/lambda-permid-input.json").getFile()
                ), "utf-8");
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());

        JSONObject json = (JSONObject) inputParserService.parse(inputStream, withErrorService);
        Assert.assertTrue(ValidateUtil.validateInputJson(json, withErrorService));

    }
}
