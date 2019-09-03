package com.aws.demo.permids;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.aws.demo.permids.services.BatchPermIdServiceFactoryImpl;
import com.aws.demo.permids.services.PermIdServiceFactoryImpl;
import com.aws.demo.permids.services.setting.Action;
import com.aws.demo.permids.services.setting.DataSource;
import com.aws.demo.permids.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import static com.aws.demo.permids.util.ValidateUtil.BATCH_SIZE;
import static com.aws.demo.permids.util.ValidateUtil.TYPE;
import static com.aws.demo.permids.util.ValidateUtil.ACTION;
/**
 * request payload as below
 * {
 * "type":"<app>",
 * "action":"<GET_PERM_ID or GET_BATCH_PERM_ID>"
 * "size":"<int>"
 * }
 * validation as below
 * type must be included by Type
 * action must be included by Action
 * size ??? before validate or after validate ???
 * <p>
 * response as below
 * {
 * ...
 * "batchParmIds":"[<int>,<int>,<int>]"
 * "permId":"<int>"
 * "errors":"<list all exception in withErrorService>"
 * ...
 * <p>
 * }
 */
public class PermIdHandler implements RequestStreamHandler {

    private static final DataSource DATA_SOURCE = DataSource.DYNAMODB;

    private WithErrorService withErrorService;
    private InputParserService inputParserService;
    private PermIdServiceFactoryImpl permIdServiceFactory;
    private BatchPermIdServiceFactoryImpl batchPermIdServiceFactory;

    public PermIdHandler() {
        this.withErrorService = new WithErrorServiceImpl();
        this.inputParserService = new InputParserServiceImpl();
        this.permIdServiceFactory = new PermIdServiceFactoryImpl();
        this.batchPermIdServiceFactory = new BatchPermIdServiceFactoryImpl();
    }

    public WithErrorService getWithErrorService() {
        return withErrorService;
    }

    public void setWithErrorService(WithErrorService withErrorService) {
        this.withErrorService = withErrorService;
    }

    public InputParserService getInputParserService() {
        return inputParserService;
    }

    public void setInputParserService(InputParserService inputParserService) {
        this.inputParserService = inputParserService;
    }

    public PermIdServiceFactoryImpl getPermIdServiceFactory() {
        return permIdServiceFactory;
    }

    public void setPermIdServiceFactory(PermIdServiceFactoryImpl permIdServiceFactory) {
        this.permIdServiceFactory = permIdServiceFactory;
    }

    public BatchPermIdServiceFactoryImpl getBatchPermIdServiceFactory() {
        return batchPermIdServiceFactory;
    }

    public void setBatchPermIdServiceFactory(BatchPermIdServiceFactoryImpl batchPermIdServiceFactory) {
        this.batchPermIdServiceFactory = batchPermIdServiceFactory;
    }

    /**
     * parse input get type, action and batch size
     * apply id ot ids as required
     * build response with id or ids
     *
     * @param inputStream
     * @param outputStream
     * @param context
     * @throws IOException
     */
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        long begin = System.currentTimeMillis();
        LambdaLogger lambdaLogger = context.getLogger();
        lambdaLogger.log("PermIdHandler handleRequest begin");
        withErrorService.resetExceptions();
        JSONObject json = (JSONObject) inputParserService.parse(inputStream, withErrorService);
        List<String> batchParmIds = null;
        if(ValidateUtil.validateInputJson(json, withErrorService)) {
            switch (Action.valueOf(json.get(ACTION).toString())) {
                case GET_BATCH_PERM_ID:
                    batchParmIds = batchPermIdServiceFactory.getBatchPermIdService(DATA_SOURCE).getBatchPermId(
                            json.get(TYPE).toString(),
                            Integer.parseInt(json.get(BATCH_SIZE).toString()),
                            withErrorService);
                    break;
                case GET_PERM_ID:
                    batchParmIds = permIdServiceFactory.getPermIdService(DATA_SOURCE).getPermId(
                            json.get(TYPE).toString(),
                            withErrorService);
                    break;
                default:
                    withErrorService.withException(new InterruptedException(json.get(ACTION).toString() + " is not supported "));
            }
        }
        buildOutputStream(outputStream, batchParmIds, withErrorService);
        lambdaLogger.log("PermIdHandler handleRequest end duration " + (System.currentTimeMillis() - begin));
    }

    /**
     * build response with id or ids
     *
     * @param outputStream
     * @param batchParmIds
     * @throws IOException
     */
    public static final String RESP_IDS = "permIds";
    public static final String RESP_ERRORS = "errors";
    private void buildOutputStream(OutputStream outputStream, List<String> batchParmIds, WithErrorService withErrorService) throws IOException {
        JSONObject responseJson = new JSONObject();

        if (batchParmIds != null) {
            JSONArray listOfId = new JSONArray();
            listOfId.addAll(batchParmIds);
            responseJson.put(RESP_IDS, listOfId);
        }

        if (!withErrorService.getExceptions().isEmpty()) {
            JSONArray listOfException = new JSONArray();
            listOfException.addAll(withErrorService.getExceptions());
            responseJson.put(RESP_ERRORS, listOfException);
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }


}
