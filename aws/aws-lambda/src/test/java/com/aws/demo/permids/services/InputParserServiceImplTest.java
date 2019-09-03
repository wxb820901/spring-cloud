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




public class InputParserServiceImplTest {
    WithErrorService withErrorService = new WithErrorServiceImpl();
    InputParserService inputParserService = new InputParserServiceImpl();
    @Test
    public void testParse() throws IOException {
        String jsonString = FileUtils.readFileToString(
                new File(
                        this.getClass().getResource("/lambda-batch-permid-input.json").getFile()
                ), "utf-8");
        InputStream inputStream = new ByteArrayInputStream(jsonString.getBytes());

        JSONObject json = (JSONObject) inputParserService.parse(inputStream, withErrorService);
        Assert.assertEquals("TYPE1",json.get(ValidateUtil.TYPE).toString());
        Assert.assertEquals("GET_BATCH_PERM_ID",json.get(ValidateUtil.ACTION).toString());
        Assert.assertEquals("100",json.get(ValidateUtil.BATCH_SIZE).toString());
    }


}
