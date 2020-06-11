package io.rukou.edge.routes;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import io.rukou.edge.Main;
import io.rukou.edge.Message;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class EventHubRoute extends Route {

  private final String eventhubNamespaceUrl;
  private final int id;
  EventHubProducerClient producer;

  public EventHubRoute(int id, String eventhubNamespaceUrl) {
    this.id = id;
    this.eventhubNamespaceUrl = eventhubNamespaceUrl;

    //create eventhubclient
    producer = new EventHubClientBuilder()
            .connectionString(eventhubNamespaceUrl + ";EntityPath=edge2local")
            .buildProducerClient();
  }

  @Override
  public String getType() {
    return "azure-eventhub";
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
    msg.header.put("X-LOCAL2EDGE-DESTINATION", "local2edge");

    EventData eventData = new EventData(msg.body);
    eventData.getProperties().putAll(msg.header);

    List<EventData> events = Collections.singletonList(eventData);
    producer.send(events);

    System.out.println("send " + msg.getRequestId());
    return "done.";
  }

  public void initLocal2EdgeSubscription() {
    EventHubConsumerAsyncClient consumer = new EventHubClientBuilder()
            .connectionString(eventhubNamespaceUrl + ";EntityPath=local2edge")
            .consumerGroup("$Default")
            .buildAsyncConsumerClient();

    consumer.receive(false)
            .subscribe(partitionEvent -> {
              EventData event = partitionEvent.getData();

              Message msg = new Message();
              for (Map.Entry<String, Object> entry : event.getProperties().entrySet()) {
                msg.header.put(entry.getKey(), entry.getValue().toString());
              }
              msg.body = event.getBodyAsString();

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
            }, error -> System.err.print(error.toString()));
  }

  public void shutdown() {
    System.out.println("shutting down route " + getId());
  }
}
