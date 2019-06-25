package com.aws.demo;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.identitymanagement.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class IAMTest {
    public static final String username = "user-" + UUID.randomUUID();
    public static final String rolename = "role-" + UUID.randomUUID();
    public static final String policyname = "policy-" + UUID.randomUUID();
    public static final String POLICY_DOCUMENT =
            "{" +
                    "  \"Version\": \"2012-10-17\"," +
                    "  \"Statement\": [" +
                    "    {" +
                    "        \"Effect\": \"Allow\"," +
                    "        \"Action\": \"logs:CreateLogGroup\"," +
                    "        \"Resource\": \"%s\"" +
                    "    }," +
                    "    {" +
                    "        \"Effect\": \"Allow\"," +
                    "        \"Action\": [" +
                    "            \"dynamodb:DeleteItem\"," +
                    "            \"dynamodb:GetItem\"," +
                    "            \"dynamodb:PutItem\"," +
                    "            \"dynamodb:Scan\"," +
                    "            \"dynamodb:UpdateItem\"" +
                    "       ]," +
                    "       \"Resource\": \"RESOURCE_ARN\"" +
                    "    }" +
                    "   ]" +
                    "}";
    @Test
    public void test(){

        //create user
        final AmazonIdentityManagement iam =
                AmazonIdentityManagementClientBuilder.standard().withRegion(Regions.US_WEST_1).build();
        CreateUserRequest request = new CreateUserRequest().withUserName(username);
        CreateUserResult response = iam.createUser(request);

        //list user
        boolean done = false;
        ListUsersRequest listRequest = new ListUsersRequest();
        while(!done) {
            ListUsersResult listResponse = iam.listUsers(listRequest);
            for(User user : listResponse.getUsers()) {
                System.out.format("Retrieved user %s", user.getUserName());
            }
            listRequest.setMarker(listResponse.getMarker());
            if(!listResponse.getIsTruncated()) {
                done = true;
            }
        }

        //update user
        UpdateUserRequest updateRequest = new UpdateUserRequest()
                .withUserName(username)
                .withNewUserName(username+"_updated");
        UpdateUserResult updateResponse = iam.updateUser(updateRequest);

        //create a policy
        CreatePolicyRequest policyRequest = new CreatePolicyRequest()
                .withPolicyName(policyname)
                .withPolicyDocument(POLICY_DOCUMENT);
        CreatePolicyResult policyResponse = iam.createPolicy(policyRequest);

        //list policy
        GetPolicyRequest listPolicyRequest = new GetPolicyRequest()
                .withPolicyArn(policyResponse.getPolicy().getArn());
        GetPolicyResult listPolicyResponse = iam.getPolicy(listPolicyRequest);

        //create role
        AttachRolePolicyRequest attach_request =
                new AttachRolePolicyRequest()
                        .withRoleName(rolename)
                        .withPolicyArn(policyResponse.getPolicy().getArn());
        iam.attachRolePolicy(attach_request);

        //list role attached policy
        ListAttachedRolePoliciesRequest listAttachedRolePoliciesRequest =
                new ListAttachedRolePoliciesRequest()
                        .withRoleName(rolename);
        List<AttachedPolicy> matching_policies = new ArrayList<>();
        boolean allDone = false;
        while(!allDone) {
            ListAttachedRolePoliciesResult listAttachedRolePoliciesResult =
                    iam.listAttachedRolePolicies(listAttachedRolePoliciesRequest);
            matching_policies.addAll(
                    listAttachedRolePoliciesResult.getAttachedPolicies()
                            .stream()
                            .filter(p -> p.getPolicyName().equals(rolename))
                            .collect(Collectors.toList()));
            if(!listAttachedRolePoliciesResult.getIsTruncated()) {
                allDone = true;
            }
            listAttachedRolePoliciesRequest.setMarker(listAttachedRolePoliciesResult.getMarker());
        }
        
        //detach role
        DetachRolePolicyRequest detachRolePolicyRequest = new DetachRolePolicyRequest()
                .withRoleName(rolename)
                .withPolicyArn(policyResponse.getPolicy().getArn());

        DetachRolePolicyResult detachRolePolicyResult = iam.detachRolePolicy(detachRolePolicyRequest);
    }

}
