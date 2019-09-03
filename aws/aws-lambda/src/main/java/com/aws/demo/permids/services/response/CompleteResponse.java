package com.aws.demo.permids.services.response;

/**
 * wrapper response
 * copy form https://git.sami.int.thomsonreuters.com/cdf-mvp/orca-plus-search-wrapper/blob/develop/src/main/java/com/refinitiv/edp/cnm/orca/searchwrapper/response/CompleteResponse.java
 * for lib invoke
 */
public class CompleteResponse {

    public static final String RESPONSE_STATUS_SUCCESS = "SUCCESS";
    public static final String RESPONSE_STATUS_FAILED = "FAILED";

    private String status;
    private Object data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CompleteResponse{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
