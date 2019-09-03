package com.aws.demo.permids.services;

import com.amazonaws.services.lambda.runtime.Context;
import org.json.simple.JSONObject;

import java.io.IOException;

public interface ResetPermIdService {
    void init(JSONObject json, Context context)  throws IOException;
}
