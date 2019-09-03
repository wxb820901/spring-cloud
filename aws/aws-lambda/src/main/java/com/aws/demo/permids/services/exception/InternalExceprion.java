package com.aws.demo.permids.services.exception;

import com.aws.demo.permids.services.setting.Action;

public class InternalExceprion extends Exception {
    public static final String prefix = "[Internal] ";
    public static final String TYPE_REQUIRED = "type required";
    public static final String ACTION_REQUIRED = "action required";
    public static final String ACTION_SHOULD_BE = "action should be " + Action.values();
    public static final String BATCH_SIZE_REQUIRED = "batch size required";
    public static final String SIZE_IS_NOT_NUMBER = "batch size is not a integer or too big. ";
    public static final String IS_OUT_OF_RANGE = "out of range";
    public static final String TYPE_NOT_EXIST = "type not exist";
    public static final String DATA_EXCEPTION_CURRENT_MAX_MIN_ID = "current id, max id or min id not exist";
    public InternalExceprion(String msg) {
        super(prefix + msg);
    }

    public InternalExceprion() {
        super();
    }
}
