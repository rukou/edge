package io.rukou.edge.routes;

import io.rukou.edge.Main;
import io.rukou.edge.Message;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.util.Enumeration;

public class EchoRoute extends Route {

  private final int id;

  public EchoRoute(int id) {
    this.id = id;
  }

  @Override
  public String getType() {
    return "rukou-echo";
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
        exchange.setStatusCode(200);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send(msg.body);
        exchange.getResponseSender().close();
        exchange.endExchange();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return "";
  }

  public void initLocal2EdgeSubscription() {
  }

  public void shutdown() {
  }
}
