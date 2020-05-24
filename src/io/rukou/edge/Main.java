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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
  public static int port = 8080;
  public static ConcurrentHashMap<String, HttpExchange> openConnections = new ConcurrentHashMap<>();
  public static String podName = "";

  public static void main(String[] args) {
    System.out.println("initializing ...");
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
      System.out.println("loading host configuration");
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
      System.out.println("loading route configuration");
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
          //topic sample string
          // projects/test-project/topics/edge2local
          String[] parts = edge2localTopic.split("/");
          String l2ePrefix = env.get("ROUTES_" + routeName + "_LOCAL2EDGEPREFIX");
          String local2edgeTopic;
          if (podName.length() > 0) {
            local2edgeTopic = parts[0] + "/" + parts[1] + "/" + parts[2] + "/" + l2ePrefix + "-" + podName;
          } else {
            local2edgeTopic = parts[0] + "/" + parts[1] + "/" + parts[2] + "/" + l2ePrefix;
          }
          String serviceAccount = env.get("ROUTES_" + routeName + "_SERVICEACCOUNT");
          if (serviceAccount == null) {
            System.out.println("service account missing for route " + routeName);
            continue;
          }
          try {
            PubSubRoute pubsub = new PubSubRoute(routeName, edge2localTopic, local2edgeTopic, serviceAccount);
            pubsub.initLocal2EdgeSubscription();
            routes.add(pubsub);
            System.out.println("attaching route " + routeName);
          } catch (Exception ex) {
            System.out.println("initializing route " + routeName + " failed");
            ex.printStackTrace();
          }
        }
      }

      System.out.println("loading endpoint configuration");
      List<Endpoint> endpoints = new ArrayList<>();
      //look for env variables
      for (Map.Entry<String, String> entry : env.entrySet()) {
        String key = entry.getKey();
        String val = entry.getValue();
        if (key.toUpperCase().startsWith("ENDPOINTS_") &&
            key.toUpperCase().endsWith("_TYPE")) {
          String endpointName = key.toUpperCase().replace("ENDPOINTS_", "")
              .replace("_TYPE", "");
          String endpointType = val.toLowerCase();
          switch (endpointType) {
            case "echo":
              String echoSelector = env.get("ENDPOINTS_" + endpointName + "_SELECTOR");
              String echoRouteName = env.get("ENDPOINTS_" + endpointName + "_ROUTE");
              Route echoRoute = null;
              if (echoRouteName == null) {
                echoRoute = routes.get(0);
              } else {
                for (Route r : routes) {
                  if (r.getId().equalsIgnoreCase(echoRouteName)) {
                    echoRoute = r;
                  }
                }
              }
              if (echoRoute == null) {
                //skip endpoint if config cannot be determined
                System.out.println("route for endpoint " + endpointName + " not found.");
                continue;
              }
              Route finalEchoRoute = echoRoute;
              Endpoint echoEndpoint = new Endpoint() {{
                id = endpointName;
                selector = echoSelector;
                route = finalEchoRoute;
              }};
              endpoints.add(echoEndpoint);
              System.out.println("adding endpoint " + endpointName);
              break;
            case "http":
              String httpSelector = env.get("ENDPOINTS_" + endpointName + "_SELECTOR");
              String httpRouteName = env.get("ENDPOINTS_" + endpointName + "_ROUTE");
              Route httpRoute = null;
              if (httpRouteName == null) {
                httpRoute = routes.get(0);
              } else {
                for (Route r : routes) {
                  if (r.getId().equalsIgnoreCase(httpRouteName)) {
                    httpRoute = r;
                  }
                }
              }
              if (httpRoute == null) {
                //skip endpoint if config cannot be determined
                System.out.println("route for endpoint " + endpointName + " not found.");
                continue;
              }
              Map<String, String> httpHeader = new HashMap<>();
              String httpUrl = env.get("ENDPOINTS_" + endpointName + "_URL");
              httpHeader.put("X-HTTP-ENDPOINT", httpUrl);
              for (Map.Entry<String, String> headerEntry : env.entrySet()) {
                String headerEntryKey = headerEntry.getKey();
                if (headerEntryKey.toUpperCase().startsWith("ENDPOINTS_" + endpointName + "_HEADER")) {
                  String headerValue = headerEntry.getValue();
                  int idx = headerValue.indexOf(":");
                  httpHeader.put(headerValue.substring(0, idx), headerValue.substring(idx + 1));
                }
              }
              Route finalHttpRoute = httpRoute;
              Endpoint httpEndpoint = new Endpoint() {{
                id = endpointName;
                type = endpointType;
                selector = httpSelector;
                route = finalHttpRoute;
                header= httpHeader;
              }};
              endpoints.add(httpEndpoint);
              System.out.println("adding endpoint " + endpointName);
              break;
            case "jms":
              String jmsSelector = env.get("ENDPOINTS_" + endpointName + "_SELECTOR");
              String jmsRouteName = env.get("ENDPOINTS_" + endpointName + "_ROUTE");
              Route jmsRoute = null;
              if (jmsRouteName == null) {
                jmsRoute = routes.get(0);
              } else {
                for (Route r : routes) {
                  if (r.getId().equalsIgnoreCase(jmsRouteName)) {
                    jmsRoute = r;
                  }
                }
              }
              if (jmsRoute == null) {
                //skip endpoint if config cannot be determined
                System.out.println("route for endpoint " + endpointName + " not found.");
                continue;
              }
              Map<String, String> jmsHeader = new HashMap<>();
              String jmsInitialFactory = env.get("ENDPOINTS_" + endpointName + "_INITIALFACTORY");
              String jmsProviderUrl = env.get("ENDPOINTS_" + endpointName + "_PROVIDERURL");
              String jmsDestination = env.get("ENDPOINTS_" + endpointName + "_DESTINATION");
              String jmsUser = env.get("ENDPOINTS_" + endpointName + "_USER");
              String jmsPassword = env.get("ENDPOINTS_" + endpointName + "_PASSWORD");
              jmsHeader.put("X-JMS-INITIALFACTORY", jmsInitialFactory);
              jmsHeader.put("X-JMS-PROVIDERURL", jmsProviderUrl);
              jmsHeader.put("X-JMS-DESTINATION", jmsDestination);
              jmsHeader.put("X-JMS-USER", jmsUser);
              jmsHeader.put("X-JMS-PASSWORD", jmsPassword);
              Route finalJmsRoute = jmsRoute;
              Endpoint jmsEndpoint = new Endpoint() {{
                id = endpointName;
                type = endpointType;
                selector = jmsSelector;
                route = finalJmsRoute;
                header= jmsHeader;
              }};
              endpoints.add(jmsEndpoint);
              System.out.println("adding endpoint " + endpointName);
              break;
            default:
              System.out.println("endpoint type cannot be determined for " + endpointName + " with type " + endpointType);
              break;
          }
        }
      }

      //start server
      HttpServer server = HttpServer.create(new InetSocketAddress(port), 100);
      HttpContext all = server.createContext("/", new RequestHandler(endpoints));
      all.getFilters().add(new HealthCheckFilter());
      if (hosts.size() > 0) {
        all.getFilters().add(new HostFilter(hosts));
      }

      //shutdown hook
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        System.out.println("shutting down");
        server.stop(1);
      }));

      System.out.println("Rùkǒu edge is running.");
      System.out.println("http://localhost:" + port + "/");
      server.start();
    } catch (Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }
}
