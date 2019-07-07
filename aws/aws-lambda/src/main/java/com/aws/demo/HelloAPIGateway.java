package com.aws.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.*;

public class HelloAPIGateway implements RequestStreamHandler {
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String str = null;
        while((str = reader.readLine()) != null){
            sb.append(str);
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write("hello api gateway request begin");
        writer.write(sb.toString());
        writer.write("hello api gateway request end");
        writer.close();
    }
}
