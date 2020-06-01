package io.rukou.edge;

import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

public class EchoUtils {
  public static void respond(String requestId, Message msg){
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
        int statusCode = 200;
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
  }
}
