package com.aws.demo;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Hello implements RequestHandler<Hello.RequestClass, Hello.ResponseClass> {
    public static class RequestClass {
        private String id;
        private String firstName;
        private String lastName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

    public static class ResponseClass {
        private String id;
        private String firstName;
        private String lastName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastname(String lastName) {
            this.lastName = lastName;
        }
        public String toString(){
            return this.id + "|" + this.lastName + "|" + this.firstName;
        }
    }
    @Override
    public ResponseClass handleRequest(RequestClass request, Context context) {
        LambdaLogger logger = context.getLogger();
        ResponseClass responseClass = new ResponseClass();
        responseClass.setFirstName(request.getFirstName() + "-handled");
        responseClass.setLastname(request.getLastName() + "-handled");
        logger.log(responseClass.toString());
        return responseClass;
    }
}