package com.aws.demo.permids.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputParserServiceImpl implements InputParserService{

    public JSONObject parse(InputStream inputStream, WithErrorService withErrorService){
        JSONParser parser = new JSONParser();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(reader);
        } catch (ParseException e) {
            withErrorService.withException(e);
        } catch (IOException e) {
            withErrorService.withException(e);
        }
        return json;
    }
}
