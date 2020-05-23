package io.rukou.edge;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.rukou.edge.objects.Message;
import io.rukou.edge.routes.Route;
import software.amazon.awssdk.utils.IoUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RequestHandler implements HttpHandler {

  List<Route> routes;

  public RequestHandler(List<Route> routes) {
    this.routes = routes;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    try {
      //extract message object
      Message msg = new Message();
      String requestId = UUID.randomUUID().toString();
      msg.header.put("X-REQUEST-ID", requestId);
      msg.header.put("X-MESSAGE-VERSION", "2020-05-22");
      msg.header.put("X-MESSAGE-TYPE","http-request");
      msg.header.put("X-ENDPOINT-TYPE","echo");
      msg.header.put("X-HTTP-METHOD", exchange.getRequestMethod());
      msg.header.put("X-HTTP-PATH", exchange.getRequestURI().getPath());
      msg.header.put("X-HTTP-TIMESTAMP", Instant.now().toString());
      Headers headers = exchange.getRequestHeaders();
      for(Map.Entry<String, List<String>> entry : headers.entrySet()){
        String keyName = entry.getKey().toUpperCase();
        msg.header.put(keyName,String.join(", ",entry.getValue()));
      }
      msg.body = IoUtils.toUtf8String(exchange.getRequestBody());

      if(msg.body.isEmpty()){
        msg.header.put("X-CONTENT-ISNULL","true");
//        msg.body="$$empty$$";
      }

      //put into open connections
      Main.openConnections.put(requestId, exchange);

      //forward to route
      for (Route r : routes) {
        r.invokeEdge2Local(msg);
      }


    } catch (Exception ex) {
      ex.printStackTrace();
      exchange.sendResponseHeaders(500, 0);
      exchange.getResponseBody().close();
    }
  }
}
