package com.aws.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.Charset;

public class HelloContext implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        System.out.println("getClass==>"+context.getClass());
        System.out.println("getClientContext==>"+context.getClientContext());

        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject event = null;

        try {
            event = (JSONObject) parser.parse(reader);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("inputStream==>"+event);

    }
}
