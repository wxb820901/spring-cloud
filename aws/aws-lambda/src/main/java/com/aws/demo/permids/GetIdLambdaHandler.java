package com.aws.demo.permids;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.aws.demo.permids.util.InputParserService;
import com.aws.demo.permids.util.InputParserServiceImpl;
import com.aws.demo.permids.util.WithErrorService;
import com.aws.demo.permids.util.WithErrorServiceImpl;
import com.aws.demo.permids.services.s3.S3ServiceImpl;
import org.json.simple.JSONObject;

import java.io.*;
@Deprecated
public class GetIdLambdaHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        LambdaLogger lambdaLogger = context.getLogger();
        S3ServiceImpl idService = new S3ServiceImpl();
        String id = "";
        try {
            id = idService.getAndUpdateId();
            lambdaLogger.log("get and update id : " + id);
        } catch (Exception e) {
            lambdaLogger.log(e.getMessage());
        }
        buildOutputStreamWriter(inputStream, outputStream, context, id);

    }

    private void buildOutputStreamWriter(InputStream inputStream, OutputStream outputStream, Context context, String id) throws IOException {
        LambdaLogger lambdaLogger = context.getLogger();
        lambdaLogger.log("functionName: " + context.getFunctionName());
        lambdaLogger.log("InvokedFunctionArn: " + context.getInvokedFunctionArn());

        InputParserService inputParserService = new InputParserServiceImpl();
        WithErrorService withErrorService = new WithErrorServiceImpl();
        JSONObject json = (JSONObject) inputParserService.parse(inputStream, withErrorService);
        lambdaLogger.log("input: " + json.toJSONString());

        JSONObject headerJson = new JSONObject();
        headerJson.put("x-custom-header", "my custom header value");

        JSONObject responseBody = new JSONObject();
        responseBody.put("getAndUpdateId",id);

        JSONObject responseJson = new JSONObject();
        responseJson.put("statusCode", 200);
        responseJson.put("event",json.toString());
        responseJson.put("headers", headerJson);
        responseJson.put("body", responseBody.toString());

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }

}
