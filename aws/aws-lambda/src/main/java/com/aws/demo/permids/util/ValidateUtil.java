package com.aws.demo.permids.util;

import com.amazonaws.services.dynamodbv2.document.Item;
import com.aws.demo.permids.services.exception.InternalExceprion;
import com.aws.demo.permids.services.setting.Action;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ValidateUtil {
    public static final String TYPE = "type";
    public static final String ACTION = "action";
    public static final String BATCH_SIZE = "size";
    public static boolean validateInputJson(JSONObject json, WithErrorService withErrorService){
        String action = (String) json.get(ACTION);
        String type = (String) json.get(TYPE);
        List<String> actions = Arrays.stream(Action.values()).map(n -> n.toString()).collect(Collectors.toList());
        if(type == null){//type required
            withErrorService.withException(new InternalExceprion(InternalExceprion.TYPE_REQUIRED));
            return false;
        }
        if(action == null){//action required
            withErrorService.withException(new InternalExceprion(InternalExceprion.ACTION_REQUIRED));
            return false;
        }else if(!actions.contains(action)){//action should be according to Action
            withErrorService.withException(new InternalExceprion(InternalExceprion.ACTION_SHOULD_BE));
            return false;
        }else if(Action.valueOf(action) == Action.GET_BATCH_PERM_ID
                && json.get(BATCH_SIZE) == null){//if GET_BATCH_PERM_ID, size required
            withErrorService.withException(new InternalExceprion(InternalExceprion.BATCH_SIZE_REQUIRED));
            return false;
        }else if(Action.valueOf(action) == Action.GET_BATCH_PERM_ID
                && json.get(BATCH_SIZE) != null){
            Integer size = null;
            try{
                size = Integer.parseInt(json.get(BATCH_SIZE).toString());
            }catch(NumberFormatException e){
                withErrorService.withException(new InternalExceprion(InternalExceprion.SIZE_IS_NOT_NUMBER + e.getMessage()));
                return false;
            }
        }

        return true;
    }


    public static boolean validateData(Item outComeOfQuery, WithErrorService withErrorService){
        if(outComeOfQuery == null){
            withErrorService.withException(new InternalExceprion(InternalExceprion.TYPE_NOT_EXIST));
            return false;
        }else if(outComeOfQuery.get("currentPermId") == null
                || outComeOfQuery.get("maxPermId") == null
                || outComeOfQuery.get("minPermId") == null){
            withErrorService.withException(new InternalExceprion(InternalExceprion.DATA_EXCEPTION_CURRENT_MAX_MIN_ID));
            return false;
        }

        return true;
    }
}
