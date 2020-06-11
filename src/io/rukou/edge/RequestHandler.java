package io.rukou.edge;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderMap;
import io.undertow.util.HttpString;
import software.amazon.awssdk.utils.IoUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class RequestHandler implements HttpHandler {

  final List<Endpoint> endpoints;
  final ScriptEngine jsEngine;

  public RequestHandler(List<Endpoint> endpoints) {
    this.endpoints = endpoints;
    ScriptEngineManager mgr = new ScriptEngineManager();
    jsEngine = mgr.getEngineByName("JavaScript");
    if (jsEngine == null) {
      System.err.println("no js engine found");
    }
  }

  @Override
  public void handleRequest(HttpServerExchange exchange) {
    System.out.println(exchange.getRequestMethod().toString() + " " + exchange.getRequestPath());
    try {
      //extract message object
      Message msg = new Message();
      String requestId = UUID.randomUUID().toString();
      msg.header.put("X-REQUEST-ID", requestId);
      msg.header.put("X-MESSAGE-VERSION", "2020-05-22");
      msg.header.put("X-MESSAGE-TYPE", "http-request");
      msg.header.put("X-HTTP-METHOD", exchange.getRequestMethod().toString());
      msg.header.put("X-HTTP-PATH", exchange.getRequestPath());
      msg.header.put("X-HTTP-TIMESTAMP", Instant.now().toString());
      msg.header.put("X-HTTP-HOST", exchange.getRequestHeaders().getFirst("Host"));
      HeaderMap headers = exchange.getRequestHeaders();
      Collection<HttpString> headerEntries = headers.getHeaderNames();
      for (HttpString headerKey : headerEntries) {
        String keyName = headerKey.toString().toUpperCase();
        msg.header.put(keyName, String.join(", ", headers.get(headerKey).toArray()));
      }
      exchange.startBlocking();
      msg.body = IoUtils.toUtf8String(exchange.getInputStream());

      //put into open connections
      Main.openConnections.put(requestId, exchange);

      //determine endpoint
      try {
        jsEngine.put("header", msg.header);
        for (Endpoint endpoint : endpoints) {
          jsEngine.eval("result = " + endpoint.selector);
          Boolean selectorMatches = (Boolean) jsEngine.get("result");
          if (selectorMatches) {
            System.out.println("delivering to endpoint " + endpoint.alias + " (" + endpoint.type + ")");
            msg.header.put("X-ENDPOINT-TYPE", endpoint.type);
            msg.header.put("X-ENDPOINT-ID", String.valueOf(endpoint.id));
            msg.header.putAll(endpoint.header);
            endpoint.route.invokeEdge2Local(msg);
          }
        }
      } catch (ScriptException ex) {
        ex.printStackTrace();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      exchange.setStatusCode(500);
      exchange.endExchange();
    }
  }
}
