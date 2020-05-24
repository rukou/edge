package io.rukou.edge;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import software.amazon.awssdk.utils.IoUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RequestHandler implements HttpHandler {

  final List<Endpoint> endpoints;
  final ScriptEngine jsEngine;

  public RequestHandler(List<Endpoint> endpoints) {
    this.endpoints = endpoints;
    ScriptEngineManager mgr = new ScriptEngineManager();
    jsEngine = mgr.getEngineByName("JavaScript");
    if(jsEngine == null){
      System.err.println("no js engine found");
    }
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    try {
      //extract message object
      Message msg = new Message();
      String requestId = UUID.randomUUID().toString();
      msg.header.put("X-REQUEST-ID", requestId);
      msg.header.put("X-MESSAGE-VERSION", "2020-05-22");
      msg.header.put("X-MESSAGE-TYPE", "http-request");
      msg.header.put("X-HTTP-METHOD", exchange.getRequestMethod());
      msg.header.put("X-HTTP-PATH", exchange.getRequestURI().getPath());
      msg.header.put("X-HTTP-TIMESTAMP", Instant.now().toString());
      msg.header.put("X-HTTP-HOST", exchange.getRequestHeaders().getFirst("Host"));
      Headers headers = exchange.getRequestHeaders();
      for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
        String keyName = entry.getKey().toUpperCase();
        msg.header.put(keyName, String.join(", ", entry.getValue()));
      }
      msg.body = IoUtils.toUtf8String(exchange.getRequestBody());

      //put into open connections
      Main.openConnections.put(requestId, exchange);

      //determine endpoint
      try {
        jsEngine.put("header", msg.header);
        for (Endpoint endpoint : endpoints) {
          jsEngine.eval("result = " + endpoint.selector);
          Boolean selectorMatches = (Boolean) jsEngine.get("result");
          if (selectorMatches) {
            System.out.println("delivering to endpoint " + endpoint.id);
            msg.header.put("X-ENDPOINT-TYPE", endpoint.type);
            msg.header.put("X-ENDPOINT-ID", endpoint.id);
            msg.header.putAll(endpoint.header);
            endpoint.route.invokeEdge2Local(msg);
          }
        }
      } catch (ScriptException ex) {
        ex.printStackTrace();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      exchange.sendResponseHeaders(500, 0);
      exchange.getResponseBody().close();
    }
  }
}
