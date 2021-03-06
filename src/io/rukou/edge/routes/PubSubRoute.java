package io.rukou.edge.routes;

import com.google.api.core.ApiFuture;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.NotFoundException;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.protobuf.ByteString;
import com.google.protobuf.Duration;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.Subscription;
import io.rukou.edge.Main;
import io.rukou.edge.Message;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

public class PubSubRoute extends Route {

  private final String local2edgeTopic;
  private CredentialsProvider credentialsProvider = null;
  private Subscription subscription = null;
  Publisher publisher;
  private final int id;

  public PubSubRoute(int id, String edge2localTopic, String local2edgeTopic, String serviceAccount) {
    this.id = id;
    this.local2edgeTopic = local2edgeTopic;
    //create account
    InputStream stream = new ByteArrayInputStream(serviceAccount.getBytes(StandardCharsets.UTF_8));
    try {
      ServiceAccountCredentials account = ServiceAccountCredentials.fromStream(stream);
      credentialsProvider = FixedCredentialsProvider.create(account);
    } catch (IOException e) {
      System.err.println("error while parsing service account credentials");
      e.printStackTrace();
    }
    //create topic
    try {
      TopicAdminSettings topicAdminSettings = TopicAdminSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
      try (TopicAdminClient topicAdminClient = TopicAdminClient.create(topicAdminSettings)) {
        try {
          topicAdminClient.getTopic(this.local2edgeTopic);
        } catch (NotFoundException notfound) {
          //create if not exists
          topicAdminClient.createTopic(this.local2edgeTopic);
        }
        topicAdminClient.shutdown();
        topicAdminClient.awaitTermination(1, TimeUnit.SECONDS);
      }

      //create subscription
      SubscriptionAdminSettings subscriptionAdminSettings =
              SubscriptionAdminSettings.newBuilder()
                      .setCredentialsProvider(credentialsProvider)
                      .build();
      SubscriptionAdminClient subscriptionAdminClient =
              SubscriptionAdminClient.create(subscriptionAdminSettings);

      //topic sample string
      // projects/test-project/topics/edge2local
      String[] parts = local2edgeTopic.split("/");
      //subscription sample string
      //projects/test-project/subscriptions/local2edge-subscription
      String subscriptionName = parts[0] + "/" + parts[1] + "/subscriptions/" + parts[3] + "-subscription";
      try {
        System.out.println("getting subscription: " + subscriptionName);
        subscription = subscriptionAdminClient.getSubscription(subscriptionName);
      } catch (NotFoundException notfound) {
        //create if not exists
        Subscription s = Subscription.newBuilder()
                .setAckDeadlineSeconds(30)
                .setMessageRetentionDuration(Duration.newBuilder().setSeconds(600))
                .setName(subscriptionName)
                .setTopic(local2edgeTopic)
                .build();
        System.out.println("creating subscription: " + subscriptionName);
        subscription = subscriptionAdminClient.createSubscription(s);
      }
      subscriptionAdminClient.shutdown();
      subscriptionAdminClient.awaitTermination(1, TimeUnit.SECONDS);

      //create publisher
      try {
        publisher = Publisher.newBuilder(edge2localTopic).setCredentialsProvider(credentialsProvider).build();
      } catch (Exception ex) {
        System.out.println("edge2local topic does not exist");
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create(topicAdminSettings)) {
          topicAdminClient.createTopic(edge2localTopic);
          publisher = Publisher.newBuilder(edge2localTopic).setCredentialsProvider(credentialsProvider).build();
          topicAdminClient.shutdown();
          topicAdminClient.awaitTermination(1, TimeUnit.SECONDS);
        }
      }
    } catch (Exception e) {
      System.err.println("error while creating route "+id);
      e.printStackTrace();
    }
  }

  private String getSubscriptionFromTopic(String topicName) {
    //subscription sample string
    //projects/test-project/subscriptions/local2edge-subscription
    String[] parts = topicName.split("/");
    return parts[0] + "/" + parts[1] + "/subscriptions/" + parts[3] + "-subscription";
  }

  @Override
  public String getType() {
    return "google-pubsub";
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public String getAlias() {
    return String.valueOf(id);
  }

  @Override
  public String invokeEdge2Local(Message msg) {
    //add reply topic
    msg.header.put("X-LOCAL2EDGE-DESTINATION", local2edgeTopic);
    PubsubMessage pubsubMessage =
            PubsubMessage.newBuilder().setData(ByteString
                    .copyFromUtf8(msg.body)).putAllAttributes(msg.header).build();

    ApiFuture<String> result = publisher.publish(pubsubMessage);
    System.out.println("send " + msg.getRequestId());
    try {
      return result.get();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public void initLocal2EdgeSubscription() {
    try {
      new Thread(() -> {
        MessageReceiver receiver =
                (message, consumer) -> {
                  Message msg = new Message();
                  msg.header = message.getAttributesMap();
                  msg.body = message.getData().toStringUtf8();

                  String requestId = msg.getRequestId();
                  System.out.println("received " + requestId);
                  HttpServerExchange exchange = Main.openConnections.get(requestId);

                  if (exchange == null) {
                    System.out.println("no client to send response to");
                    System.out.println(Main.openConnections.size() + " pending connections");
                    System.out.println("looking for " + requestId);
                    Enumeration<String> keys = Main.openConnections.keys();
                    while (keys.hasMoreElements()) {
                      String kk = keys.nextElement();
                      System.out.println(kk + " " + kk.equals(requestId));
                    }
                  } else {
                    Main.openConnections.remove(requestId);
                    try {
                      String statusCodeString = msg.header.get("X-HTTP-STATUSCODE");
                      int statusCode = Integer.parseInt(statusCodeString);

                      exchange.setStatusCode(statusCode);
                      exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
                      exchange.getResponseSender().send(msg.body);
                      exchange.getResponseSender().close();
                      exchange.endExchange();
                    } catch (Exception ex) {
                      ex.printStackTrace();
                    }
                  }

                  consumer.ack();
                };

        Subscriber subscriber = null;
        try {
          subscriber = Subscriber.newBuilder(subscription.getName(), receiver).setCredentialsProvider(credentialsProvider).build();
          subscriber.startAsync().awaitRunning();
          // Allow the subscriber to run indefinitely unless an unrecoverable error occurs
          subscriber.awaitTerminated();
        } catch (Exception ex) {
          System.err.println("subscription was terminated");
          ex.printStackTrace();
        } finally {
          // Stop receiving messages
          if (subscriber != null) {
            subscriber.stopAsync();
          }
        }
      }).start();

    } catch (Exception ex) {
      System.err.println("error initializing local2edge subscription");
      ex.printStackTrace();
    }
  }

  public void shutdown() {
    System.out.println("shutting down route " + getId());
    String subscriptionName = getSubscriptionFromTopic(local2edgeTopic);
    System.out.println("deleting subscription " + subscriptionName);
    //delete subscription
    try {
      SubscriptionAdminSettings subscriptionAdminSettings =
              SubscriptionAdminSettings.newBuilder()
                      .setCredentialsProvider(credentialsProvider)
                      .build();
      SubscriptionAdminClient subscriptionAdminClient =
              SubscriptionAdminClient.create(subscriptionAdminSettings);
      subscriptionAdminClient.deleteSubscription(subscriptionName);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    System.out.println("deleting topic " + this.local2edgeTopic);
    //delete topic
    try {
      TopicAdminSettings topicAdminSettings = TopicAdminSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();
      try (TopicAdminClient topicAdminClient = TopicAdminClient.create(topicAdminSettings)) {
        topicAdminClient.deleteTopic(this.local2edgeTopic);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
