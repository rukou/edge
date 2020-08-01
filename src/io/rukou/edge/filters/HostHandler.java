//package io.rukou.edge.filters;
//
//import io.rukou.edge.Host;
//import io.undertow.server.HttpHandler;
//import io.undertow.server.HttpServerExchange;
//
//import java.util.List;
//
//public class HostHandler implements HttpHandler {
//  public static List<Host> hosts;
//  HttpHandler success;
//
//  public HostHandler(List<Host> hosts, HttpHandler success) {
//    this.hosts = hosts;
//    this.success = success;
//  }
//
//  @Override
//  public void handleRequest(HttpServerExchange exchange) throws Exception {
//    String hostHeader = exchange.getRequestHeaders().getFirst(io.undertow.util.Headers.HOST);
//    if (hostHeader != null) {
//      String hostname;
//      if (hostHeader.contains(":")) {
//        hostname = hostHeader.substring(0, hostHeader.lastIndexOf(":"));
//      } else {
//        hostname = hostHeader;
//      }
//      boolean found = false;
//      for (Host host : hosts) {
//        if (host.domain.equals(hostname)) {
//          found = true;
//          break;
//        }
//      }
//      if (found) {
//        exchange.dispatch(success);
//      } else {
//        System.out.println(exchange.getRequestMethod() + " " + exchange.getRequestPath());
//        System.out.println("not processing unknown host " + hostname);
//        System.out.println("headers " + exchange.getRequestHeaders().toString());
//        exchange.setStatusCode(200);
//        exchange.endExchange();
//      }
//    } else {
//      System.out.println("not processing unknown host");
//      exchange.setStatusCode(200);
//      exchange.endExchange();
//    }
//  }
//}
