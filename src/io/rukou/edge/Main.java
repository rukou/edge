package io.rukou.edge;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.rukou.edge.filters.HealthCheckFilter;
import io.rukou.edge.filters.HostFilter;
import io.rukou.edge.routes.PubSubRoute;
import io.rukou.edge.routes.Route;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
  public static int port = 8080;
  public static ConcurrentHashMap<String, HttpExchange> openConnections = new ConcurrentHashMap<>();
  public static String podName = "";

  public static void main(String[] args) {
    try {
      Map<String, String> env = System.getenv();

      if (env.containsKey("port")) {
        port = Integer.parseInt(env.get("port"));
      }
      if (env.containsKey("PORT")) {
        port = Integer.parseInt(env.get("PORT"));
      }
      if (env.containsKey("POD_NAME")) {
        podName = env.get("POD_NAME");
      }

      //EDGE configuration
      List<String> hosts = new ArrayList<>();
      //look for env variables
      for (Map.Entry<String, String> entry : env.entrySet()) {
        String key = entry.getKey();
        String val = entry.getValue();
        if (key.toUpperCase().startsWith("EDGE_HOSTS_")) {
          hosts.add(env.get(key));
        }
      }

      //ROUTES configuration
      List<Route> routes = new ArrayList<>();
      //look for env variables
      for (Map.Entry<String, String> entry : env.entrySet()) {
        String key = entry.getKey();
        String val = entry.getValue();
        if (key.toUpperCase().startsWith("ROUTES_") &&
            key.toUpperCase().endsWith("_TYPE")) {
          String routeName = key.toUpperCase().replace("ROUTES_", "")
              .replace("_TYPE", "");
          String type = val;
          String edge2localTopic = env.get("ROUTES_" + routeName + "_EDGE2LOCALTOPIC");
          String l2ePrefix = env.get("ROUTES_" + routeName + "_LOCAL2EDGETOPIC");
          String local2edgeTopic;
          if (podName.length() > 0) {
            local2edgeTopic = l2ePrefix + "-" + podName;
          } else {
            local2edgeTopic = l2ePrefix;
          }
          String serviceAccount = env.get("ROUTES_" + routeName + "_SERVICEACCOUNT");
          PubSubRoute pubsub = new PubSubRoute(edge2localTopic, local2edgeTopic, serviceAccount);
          pubsub.initLocal2EdgeSubscription();
          routes.add(pubsub);
          System.out.println("attaching route " + routeName);
        }
      }

      //start server
      HttpServer server = HttpServer.create(new InetSocketAddress(port), 100);
      HttpContext all = server.createContext("/", new RequestHandler(routes));
      all.getFilters().add(new HealthCheckFilter());
      if (hosts.size() > 0) {
        all.getFilters().add(new HostFilter(hosts));
      }

      //shutdown hook
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        System.out.println("shutting down");
        server.stop(1);
      }));

      System.out.println("rukou edge is running.");
      System.out.println("http://localhost:" + port + "/");
      server.start();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }
}
