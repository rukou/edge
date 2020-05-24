package io.rukou.edge.routes;

import com.google.api.core.ApiFuture;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.NotFoundException;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.v1.*;
import com.google.protobuf.ByteString;
import com.google.protobuf.Duration;
import com.google.pubsub.v1.*;
import com.sun.net.httpserver.HttpExchange;
import io.rukou.edge.Main;
import io.rukou.edge.Message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class PubSubRoute extends Route {

  private final String local2edgeTopic;
  private CredentialsProvider credentialsProvider = null;
  private Subscription subscription = null;
  Publisher publisher;
  private String type;
  private String id;

  public PubSubRoute(String id, String edge2localTopic, String local2edgeTopic, String serviceAccount) {
    this.type = "google-pubsub";
    this.id = id;
    this.local2edgeTopic = local2edgeTopic;
    //create account
    InputStream stream = new ByteArrayInputStream(serviceAccount.getBytes(StandardCharsets.UTF_8));
    try {
      ServiceAccountCredentials account = ServiceAccountCredentials.fromStream(stream);
      credentialsProvider = FixedCredentialsProvider.create(account);
    } catch (IOException e) {
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
      }

      //create subscription
      SubscriptionAdminSettings subscriptionAdminSettings =
          SubscriptionAdminSettings.newBuilder()
              .setCredentialsProvider(credentialsProvider)
              .build();
      SubscriptionAdminClient subscriptionAdminClient =
          SubscriptionAdminClient.create(subscriptionAdminSettings);
      //empty push config means pull
      PushConfig pushConfig = PushConfig.newBuilder().build();

      //topic sample string
      // projects/test-project/topics/edge2local
      String[] parts = local2edgeTopic.split("/");
      //subscription sample string
      //projects/test-project/subscriptions/local2edge-subscription
      String subscriptionName = parts[0] + "/" + parts[1] + "/subscriptions/" + parts[3] + "-subscription";
      try {
        subscription = subscriptionAdminClient.getSubscription(subscriptionName);
      } catch (NotFoundException notfound) {
        //create if not exists
        Subscription s = Subscription.newBuilder()
            .setAckDeadlineSeconds(30)
            .setMessageRetentionDuration(Duration.newBuilder().setSeconds(600))
            .setName(subscriptionName)
            .setTopic(local2edgeTopic)
            .setFilter("test=1")
            .build();
        subscription = subscriptionAdminClient.createSubscription(s);
      }

      //create publisher
      publisher = Publisher.newBuilder(edge2localTopic).setCredentialsProvider(credentialsProvider).build();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getType() {
    return "google-pubsub";
  }

  @Override
  public String getId() {
    return id;
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
              HttpExchange exchange = Main.openConnections.get(requestId);

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

                  byte[] out = msg.body.getBytes(StandardCharsets.UTF_8);
                  exchange.getResponseHeaders().add("Content-Type", "application/json");
                  exchange.sendResponseHeaders(statusCode, out.length);
                  OutputStream os = exchange.getResponseBody();
                  os.write(out);
                  os.close();
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
          ex.printStackTrace();
        } finally {
          // Stop receiving messages
          if (subscriber != null) {
            subscriber.stopAsync();
          }
        }
      }).start();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
