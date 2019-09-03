package com.aws.demo.permids.services.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.aws.demo.permids.services.exception.InternalExceprion;
import com.aws.demo.permids.util.ValidateUtil;
import com.aws.demo.permids.util.WithErrorService;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DynamoDBServiceImpl implements DynamoDBService<String, String> {
    public static final String TABLE_NAME = "PermId";
    public static final List  sss =IntStream.rangeClosed(1 , 100).mapToObj(Integer::toString).collect(Collectors.toList());

    /**
     * aws dynamodb create-table \
     * --table-name PermId \
     * --attribute-definitions \
     * AttributeName=appId,AttributeType=S \
     * AttributeName=maxPermId,AttributeType=S \
     * AttributeName=currentPermId,AttributeType=S \
     * --key-schema \
     * AttributeName=appId,KeyType=HASH
     * <p>
     * <p>
     * aws dynamodb put-item \
     * --table-name PermId \
     * --item file://initItem.json
     * <p>
     * aws dynamodb update-item \
     * --table-name PermId \
     * --key {"appId":{"S":""}} \
     * --update-expression "SET currentPermId = :zero, maxPermId = :zero," \
     * --return-values ALL_NEW
     *
     * @param json
     * @param context
     */
    @Override
    public void init(JSONObject json, Context context) {

//        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
//        DynamoDB dynamoDB = new DynamoDB(client);
//        try {
//            System.out.println("Attempting to create table; please wait...");
//            CreateTableRequest request = new CreateTableRequest()
//                    .withTableName(TABLE_NAME)
//                    .withKeySchema(
//                            Arrays.asList(
//                                    new KeySchemaElement("appId", KeyType.HASH)
//                            )
//                    )
//                    .withAttributeDefinitions(
//                            Arrays.asList(
//                                    new AttributeDefinition("appId", ScalarAttributeType.S),
//                                    new AttributeDefinition("maxPermId", ScalarAttributeType.S),
//                                    new AttributeDefinition("currentPermId", ScalarAttributeType.S)
//                            )
//                    )
//                    .withProvisionedThroughput(
//                            new ProvisionedThroughput()
//                                    .withReadCapacityUnits(5L)
//                                    .withWriteCapacityUnits(6L)
//                    );
//            Table table = dynamoDB.createTable(request);
//            table.waitForActive();
//            List<JSONObject> items = (List<JSONObject>) json.get(ResetPermIdHandler.INIT_DATA);
//            for (JSONObject currentNode : items) {
//                String appId = currentNode.get("appId").toString();
//                String maxPermId = currentNode.get("maxPermId").toString();
//                String currentPermId = currentNode.get("currentPermId").toString();
//                try {
//                    table.putItem(new Item()
//                            .withPrimaryKey("appId", appId)
//                            .withString("maxPermId", maxPermId)
//                            .withString("currentPermId", currentPermId)
//                    );
//                    System.out.println("PutItem succeeded: " + appId + " " + maxPermId);
//                } catch (Exception e) {
//                    System.err.println("Unable to add movie: " + appId + " " + maxPermId);
//                    System.err.println(e.getMessage());
//                    break;
//                }
//            }
//            System.out.println("Success.Table status: " + table.getDescription().getTableStatus());
//        } catch (Exception e) {
//            System.err.println("Unable to create table: ");
//            System.err.println(e.getMessage());
//        }
    }

    @Override
    public List<String> getBatchPermId(String type, int batchSize, WithErrorService withErrorService) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(TABLE_NAME);
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("appId", type);
        List<String> result = null;
        try {
            Item outComeOfQuery = table.getItem(spec);
            if(!ValidateUtil.validateData(outComeOfQuery, withErrorService)){
                return result;
            }
            int currentPermId = outComeOfQuery.getInt("currentPermId");
            int maxPermId = outComeOfQuery.getInt("maxPermId");
            int minPermId = outComeOfQuery.getInt("minPermId");
            int newPermId = currentPermId + batchSize;
            if (newPermId <= maxPermId && newPermId >= minPermId) {
                UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey("appId", type)
                        .withUpdateExpression("SET currentPermId = :val")
                        .withValueMap(new ValueMap().withString(":val", Integer.toString(currentPermId + batchSize)))
                        .withReturnValues(ReturnValue.ALL_NEW);
                UpdateItemOutcome outComeOfUpdate = table.updateItem(updateItemSpec);
                int newCurrentPermId = outComeOfUpdate.getItem().getInt("currentPermId");
                result = IntStream.rangeClosed(currentPermId + 1, newCurrentPermId).mapToObj(Integer::toString).collect(Collectors.toList());
            } else {
                withErrorService.withException(new InternalExceprion(InternalExceprion.IS_OUT_OF_RANGE));
            }
        } catch (Exception e) {
            withErrorService.withException(e);
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<String> getPermId(String type, WithErrorService withErrorService) {
        return getBatchPermId(type, 1, withErrorService);
    }


}


