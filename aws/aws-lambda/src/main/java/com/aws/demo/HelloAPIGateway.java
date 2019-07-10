package com.aws.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HelloAPIGateway implements RequestStreamHandler {
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        handleGetByParam(inputStream, outputStream, context);
    }

    static final Logger logger = LogManager.getLogger(HelloAPIGateway.class);

    public void handleGetByParam(
            InputStream inputStream, OutputStream outputStream, Context context)
            throws IOException {
        LambdaLogger lambdaLogger = context..getLogger();
        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject responseJson = new JSONObject();


        try {
            JSONObject event = (JSONObject) parser.parse(reader);
            JSONObject responseBody = new JSONObject();
            lambdaLogger.log("lambdaLogger=====>event=" + event);
            logger.info("log4j2Looger=====>event=" + event);
            StringBuilder content = new StringBuilder();
            if (event.get("pathParameters") != null) {
                JSONObject pps = (JSONObject) event.get("pathParameters");
                if (pps.get("somethingId") != null) {
                    int id = Integer.parseInt((String) pps.get("somethingId"));
                    lambdaLogger.log("lambdaLogger=====>somethingId=" + id);
                    logger.info("log4j2Looger=====>somethingId=" + id);
                    content.append(id+"|");
                }
            }




            responseBody.put("message", "hello world " + content.toString());
            responseJson.put("statusCode", 200);
            JSONObject headerJson = new JSONObject();
            headerJson.put("x-custom-header", "my custom header value");
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());

        } catch (ParseException pex) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", pex);
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }





}
