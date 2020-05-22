package io.rukou.edge.routes;

import io.rukou.edge.objects.Message;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.HashMap;
import java.util.Map;

public class SQSRoute extends Route {
  public String accessKey;
  public String secretKey;
  String requestQueueUrl;
  String responseQueueUrl;
  String requestRegion;
  String responseRegion;

  public SQSRoute() {
    this.type = "aws-sqs";

  }

  public String getResponseQueueUrl() {
    return responseQueueUrl;
  }

  public void setResponseQueueUrl(String url) {
    this.responseQueueUrl = url;
    try {
      //extract region
      responseRegion = responseQueueUrl.replace("https://sqs.", "").replaceAll("[.].*", "");
    } catch (Exception ex) {
      //ignore
      ex.printStackTrace();
    }
  }

  public String getRequestQueueUrl() {
    return requestQueueUrl;
  }

  public void setRequestQueueUrl(String url) {
    this.requestQueueUrl = url;
    try {
      //extract region
      requestRegion = requestQueueUrl.replace("https://sqs.", "").replaceAll("[.].*", "");
    } catch (Exception ex) {
      //ignore
      ex.printStackTrace();
    }
  }

  @Override
  public String invoke(Message msg) {
    SqsClient sqsClient = SqsClient.builder()
        .region(Region.of(requestRegion))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                new AwsCredentials(){
                  @Override
                  public String accessKeyId() {
                    return accessKey;
                  }
                  @Override
                  public String secretAccessKey() {
                    return secretKey;
                  }
                }))
        .build();
    Map<String, MessageAttributeValue> sqsHeader = new HashMap<>();
    msg.header.forEach((key, val) -> {
      sqsHeader.put(key, MessageAttributeValue.builder().stringValue(val).dataType("String").build());
    });
    SendMessageResponse response = sqsClient.sendMessage(SendMessageRequest.builder()
        .queueUrl(requestQueueUrl)
        .messageAttributes(sqsHeader)
        .messageBody(msg.body)
        .build());

    return response.messageId();
  }
}
