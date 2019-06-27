package com.aws.demo;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.org.apache.bcel.internal.util.ClassPath;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class AWSDynamoDBTest {
    AmazonDynamoDB client;
    public static final String tableName = "Movies";
    public static DynamoDB dynamoDB;
    @BeforeClass
    public static void before() throws IOException {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .build();

        dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(tableName);

        try {
            System.out.println("Attempting to create table; please wait...");

            table = dynamoDB.createTable(tableName,
                    Arrays.asList(new KeySchemaElement("year", KeyType.HASH), // Partition key
                            new KeySchemaElement("title", KeyType.RANGE)), // Sort key
                    Arrays.asList(new AttributeDefinition("year", ScalarAttributeType.N),
                            new AttributeDefinition("title", ScalarAttributeType.S)),
                    new ProvisionedThroughput(10L, 10L));
            table.waitForActive();
        } catch (Exception e) {
            System.err.println("Unable to create table: ");
            System.err.println(e.getMessage());
        }

    }
    @Test
    public void testLoadFromJson() throws IOException {
        //load data
        Table table = dynamoDB.getTable(tableName);
        JsonParser parser = new JsonFactory().createParser(new File("C:\\Users\\bill_wang\\spring-cloud\\aws\\dydb\\src\\test\\resources\\moviedata.json"));
        JsonNode rootNode = new ObjectMapper().readTree(parser);
        Iterator<JsonNode> iter = rootNode.iterator();
        JsonNode currentNode;

        currentNode = iter.next();
        int year = currentNode.asInt();
        currentNode = iter.next();
        String title = currentNode.asText();

        currentNode = iter.next();
        String info = currentNode.toString();
        try {
            table.putItem(new Item().withPrimaryKey("year", year, "title", title).withJSON("info",
                    info));
        } catch (Exception e) {
            System.err.println("Unable to add movie: " + year + " " + title);
            System.err.println(e.getMessage());

        }

        parser.close();
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("year", year, "title", title);
        Item result = queryByPK(spec);
        assertEquals(result.get("year"),new BigDecimal(2013));
        assertEquals(result.get("title"), "Turn It Down, Or Else!");
    }
    @Test
    public void testPutAndDelete(){
        int year = 2015;
        String title = "The Big New Movie";
        Table table = dynamoDB.getTable(tableName);
        final Map<String, Object> infoMap = new HashMap<String, Object>();
        infoMap.put("plot", "Nothing happens at all.");
        infoMap.put("rating", 0);
        try {
            PutItemOutcome outcome = table
                    .putItem(new Item().withPrimaryKey("year", year, "title", title).withMap("info", infoMap));
        } catch (Exception e) {
            System.err.println("Unable to add item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("year", year, "title", title);
        Item result = queryByPK(spec);

        assertEquals(result.get("year"),new BigDecimal(2015));
        assertEquals(result.get("title"), "The Big New Movie");
        DeleteItemSpec delSpec = new DeleteItemSpec().withPrimaryKey("year", year, "title", title);
        table.deleteItem(delSpec);
        result = queryByPK(spec);
        assertNull(result);

    }

    private Item queryByPK(GetItemSpec spec){
        Table table = dynamoDB.getTable(tableName);
        return table.getItem(spec);
    }
//    @AfterClass
//    public static void after() throws IOException {
//        Table table = dynamoDB.getTable(tableName);
//        table.delete();
//    }
}
