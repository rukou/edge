package io.rukou.edge.routes;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.azure.messaging.eventhubs.models.EventPosition;
import com.azure.messaging.eventhubs.models.PartitionContext;
import com.sun.net.httpserver.HttpExchange;
import io.grpc.netty.shaded.io.netty.util.internal.logging.Slf4JLoggerFactory;
import io.rukou.edge.Main;
import io.rukou.edge.Message;
import reactor.core.Disposable;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class EventHubRoute extends Route {

  private final String eventhubNamespaceUrl;
  private String id;
  EventHubProducerClient producer;

  public EventHubRoute(String id, String eventhubNamespaceUrl) {
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
  public String getId() {
    return id;
  }

  @Override
  public String invokeEdge2Local(Message msg) {
    //add reply topic
    msg.header.put("X-LOCAL2EDGE-DESTINATION", "local2edge");

    EventData eventData = new EventData(msg.body);
    eventData.getProperties().putAll(msg.header);

    List<EventData> events = Arrays.asList(eventData);
    producer.send(events);

    System.out.println("send " + msg.getRequestId());
    return "done.";
  }

  public void initLocal2EdgeSubscription() {
    EventHubConsumerAsyncClient consumer = new EventHubClientBuilder()
        .connectionString(eventhubNamespaceUrl + ";EntityPath=local2edge")
        .consumerGroup("$Default")
        .buildAsyncConsumerClient();

    Disposable subscription = consumer.receive(false)
        .subscribe(partitionEvent -> {
          PartitionContext partitionContext = partitionEvent.getPartitionContext();
          EventData event = partitionEvent.getData();

          Message msg = new Message();
          for (Map.Entry<String, Object> entry : event.getProperties().entrySet()) {
            msg.header.put(entry.getKey(), entry.getValue().toString());
          }
          msg.body = event.getBodyAsString();

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
        }, error -> System.err.print(error.toString()));
  }

  public void shutdown() {
    System.out.println("shutting down route " + getId());
  }
}
