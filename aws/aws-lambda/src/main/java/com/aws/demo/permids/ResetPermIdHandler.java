package com.aws.demo.permids;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.aws.demo.permids.services.dynamodb.DynamoDBServiceImpl;
import com.aws.demo.permids.util.InputParserService;
import com.aws.demo.permids.util.InputParserServiceImpl;
import com.aws.demo.permids.util.WithErrorService;
import com.aws.demo.permids.util.WithErrorServiceImpl;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ResetPermIdHandler implements RequestStreamHandler {

    public static final String INIT_DATA = "initData";

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        long begin = System.currentTimeMillis();
        LambdaLogger lambdaLogger = context.getLogger();
        lambdaLogger.log("ResetPermIdHandler handleRequest begin");
        WithErrorService withErrorService = new WithErrorServiceImpl();
        InputParserService inputParserService = new InputParserServiceImpl();
        JSONObject json = (JSONObject) inputParserService.parse(inputStream, withErrorService);
        applyResetPermId(json, context, withErrorService);
        buildOutputStream(outputStream, withErrorService);
        lambdaLogger.log("ResetPermIdHandler handleRequest end duration " + (System.currentTimeMillis() - begin));
    }

    /**
     * apply id ot ids as required
     *
     * @param json
     * @param withErrorService
     */
    private void applyResetPermId(JSONObject json, Context context, WithErrorService withErrorService) {
        DynamoDBServiceImpl permIDService = new DynamoDBServiceImpl();
        permIDService.init(json, context);
    }

    /**
     * build response with id or ids
     *
     * @param outputStream
     * @param withErrorService
     * @throws IOException
     */
    private void buildOutputStream(OutputStream outputStream, WithErrorService withErrorService) throws IOException {
        JSONObject headerJson = new JSONObject();
        JSONObject responseBody = new JSONObject();

        JSONObject responseJson = new JSONObject();
        responseJson.put("statusCode", 200);
        responseJson.put("headers", headerJson);
        responseJson.put("body", responseBody.toString());

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }
}
