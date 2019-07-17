$JAVA_HANDLER="com.aws.demo.HelloContext::handleRequest"
#$LOCAL_JAR_PATH="~/.m2/repository/com/example/aws-lambda/0.0.1-SNAPSHOT/aws-lambda-0.0.1-SNAPSHOT.jar"

$API_NAME="api-hello-context"
$REGION="us-east-1"
$STAGE="test"


awslocal lambda create-function --region ${REGION} --function-name ${API_NAME} --runtime java8 --handler $JAVA_HANDLER --memory-size 128 --zip-file fileb://$LOCAL_JAR_PATH --role arn:aws:iam::123456:role/irrelevant
$result=awslocal lambda list-functions |ConvertFrom-Json
$LAMBDA_ARN=$result.Functions.FunctionArn

if($?) {
    Write-Host "=============>awslocal lambda create-function successfully"
} else {
    Write-Host "=============>awslocal lambda create-function failed"
}


awslocal apigateway create-rest-api --region ${REGION} --name ${API_NAME}
$result=awslocal apigateway get-rest-apis  --region ${REGION}|ConvertFrom-Json
$API_ID=$result.items[0].id
$result=awslocal apigateway get-resources --rest-api-id ${API_ID} --region ${REGION} |ConvertFrom-Json
$PARENT_RESOURCE_ID=$result.items[0].id

if($?) {
    Write-Host "=============>awslocal apigateway create-rest-api successfully"
} else {
    Write-Host "=============>awslocal apigateway create-rest-api failed"
}


awslocal apigateway create-resource  --region ${REGION} --rest-api-id ${API_ID} --parent-id ${PARENT_RESOURCE_ID}  --path-part "{somethingId}"
$result=awslocal apigateway get-resources --rest-api-id ${API_ID} --query 'items[?path==`/{somethingId}`]'  --region ${REGION} |ConvertFrom-Json
$RESOURCE_ID=$result[0].id

if($?) {
    Write-Host "=============>awslocal apigateway create-resource successfully"
} else {
    Write-Host "=============>awslocal apigateway create-resource failed"
}


awslocal apigateway put-method --region ${REGION} --rest-api-id ${API_ID} --resource-id ${RESOURCE_ID} --http-method GET --request-parameters "method.request.path.somethingId=true" --authorization-type "NONE"

if($?) {
    Write-Host "=============>awslocal apigateway put-method successfully"
} else {
    Write-Host "=============>awslocal apigateway put-method failed"
}


awslocal apigateway put-integration --region ${REGION} --rest-api-id ${API_ID} --resource-id ${RESOURCE_ID} --http-method GET --type AWS_PROXY --integration-http-method POST --uri arn:aws:apigateway:${REGION}:lambda:path/2015-03-31/functions/${LAMBDA_ARN}/invocations --passthrough-behavior WHEN_NO_MATCH


if($?) {
    Write-Host "=============>awslocal apigateway put-integration successfully"
} else {
    Write-Host "=============>awslocal apigateway put-integration failed"
}


awslocal apigateway create-deployment --region ${REGION} --rest-api-id ${API_ID} --stage-name ${STAGE}
$ENDPOINT="http://localhost:4567/restapis/${API_ID}/${STAGE}/_user_request_/HowMuchIsTheFish"

if($?) {
    Write-Host "=============>awslocal apigateway create-deployment successfully"
} else {
    Write-Host "=============>awslocal apigateway create-deployment failed"
}


echo "API available at: ${ENDPOINT}"

