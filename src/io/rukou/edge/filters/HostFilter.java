package io.rukou.edge.filters;

import com.google.gson.Gson;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import io.rukou.edge.Host;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HostFilter extends Filter {
  public static List<Host> hosts;
  Gson g = new Gson();

  public HostFilter(List<Host> hosts){
    this.hosts = hosts;
  }

  @Override
  public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
    Headers h = exchange.getRequestHeaders();

    if(h.containsKey("Host")){
      String hostname = h.getFirst("Host").toLowerCase();
      boolean found = false;
      for(Host host: hosts){
        if(host.domain.equals(hostname)){
          found = true;
          break;
        }
      }
      if(found){
        chain.doFilter(exchange);
      }else{
        System.out.println(exchange.getRequestMethod()+" "+exchange.getRequestURI().getPath());
        System.out.println("not processing unknown host "+hostname);
        System.out.println("headers "+g.toJson(exchange.getRequestHeaders()));
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().close();
      }
    }else{
      System.out.println("not processing unknown host");
      exchange.sendResponseHeaders(200, 0);
      exchange.getResponseBody().close();
    }
  }

  @Override
  public String description() {
    return "filtering the Google health check";
  }
}
