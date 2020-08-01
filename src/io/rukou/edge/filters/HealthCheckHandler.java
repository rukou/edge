//package io.rukou.edge.filters;
//
//import io.undertow.server.HttpHandler;
//import io.undertow.server.HttpServerExchange;
//import io.undertow.util.HeaderValues;
//import io.undertow.util.Headers;
//
//public class HealthCheckHandler implements HttpHandler {
//  HttpHandler success;
//
//  public HealthCheckHandler(HttpHandler success) {
//    this.success = success;
//  }
//
//  final String googleHC = "GoogleHC/1.0";
//  final String kubeHC = "kube-probe/1.16+";
//
//  @Override
//  public void handleRequest(HttpServerExchange exchange) throws Exception {
//    HeaderValues values = exchange.getRequestHeaders().get(Headers.USER_AGENT);
//    if(values.getFirst().equals(googleHC) || values.getFirst().equals(kubeHC)){
//      exchange.setStatusCode(200);
//      exchange.endExchange();
//    }else{
//      exchange.dispatch(success);
//    }
//  }
//}
