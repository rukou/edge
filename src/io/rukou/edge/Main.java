package io.rukou.edge;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.rukou.edge.filters.HealthCheckFilter;
import io.rukou.edge.filters.HostFilter;
import io.rukou.edge.routes.EventHubRoute;
import io.rukou.edge.routes.PubSubRoute;
import io.rukou.edge.routes.Route;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.*;
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
      } else {
        podName = InetAddress.getLocalHost().getHostName();
      }

      System.out.println("host configuration...");
      List<Host> hosts = new ArrayList<>();
      //look for env variables
      for (Map.Entry<String, String> entry : env.entrySet()) {
        String key = entry.getKey();
        if (key.toUpperCase().startsWith("HOSTS_")
            && key.toUpperCase().endsWith("_DOMAIN")) {
          String val = entry.getValue().toLowerCase();
          String id = key.toUpperCase().replace("HOSTS_", "")
              .replace("_DOMAIN", "");
          Host host = new Host();
          host.domain = val;
          if (env.containsKey("HOSTS_" + id + "_AUTH")) {
            host.auth = env.get("HOSTS_" + id + "_AUTH").toLowerCase();
          } else {
            host.auth = "none";
          }
          if (host.auth.equals("apikey")) {
            //look for api keys
            for (Map.Entry<String, String> headerEntry : env.entrySet()) {
              String headerEntryKey = headerEntry.getKey();
              if (headerEntryKey.toUpperCase().startsWith("HOSTS_" + id + "_APIKEY_")) {
                String keyName = headerEntryKey.toUpperCase().replace("HOSTS_" + id + "_APIKEY_", "");
                String keyValue = headerEntry.getValue();
                host.apiKeys.put(keyValue, keyName);
              }
            }
          }
          hosts.add(host);
          System.out.println("allow host: " + host.toString());
        }
      }
      if (hosts.size() == 0) {
        System.out.println("no hosts added");
      }

      //ROUTES configuration
      System.out.println("route configuration...");
      List<Route> routes = new ArrayList<>();
      //look for env variables
      for (Map.Entry<String, String> entry : env.entrySet()) {
        String key = entry.getKey();
        String val = entry.getValue();
        if (key.toUpperCase().startsWith("ROUTES_") &&
            key.toUpperCase().endsWith("_TYPE")) {
          String routeNumber = key.toUpperCase().replace("ROUTES_", "")
              .replace("_TYPE", "");
          int routeNumberInt = Integer.parseInt(routeNumber);
          String type = val;
          switch (type) {
            case "google-pubsub":
              String edge2localTopic = env.get("ROUTES_" + routeNumber + "_EDGE2LOCALTOPIC");
              //topic sample string
              // projects/test-project/topics/edge2local
              String[] parts = edge2localTopic.split("/");
              String l2ePrefix = env.get("ROUTES_" + routeNumber + "_LOCAL2EDGEPREFIX");
              String local2edgeTopic;
              if (podName.length() > 0) {
                local2edgeTopic = parts[0] + "/" + parts[1] + "/" + parts[2] + "/" + l2ePrefix + "-" + podName;
              } else {
                local2edgeTopic = parts[0] + "/" + parts[1] + "/" + parts[2] + "/" + l2ePrefix;
              }
              String serviceAccount = env.get("ROUTES_" + routeNumber + "_SERVICEACCOUNT");
              if (serviceAccount == null) {
                System.out.println("service account missing for route " + routeNumber);
                continue;
              }
              try {
                PubSubRoute pubsub = new PubSubRoute(routeNumberInt, edge2localTopic, local2edgeTopic, serviceAccount);
                pubsub.initLocal2EdgeSubscription();
                routes.add(pubsub);
                System.out.println("attaching route " + routeNumber);
              } catch (Exception ex) {
                System.out.println("initializing route " + routeNumber + " failed");
                ex.printStackTrace();
              }
              break;
            case "azure-eventhub":
              String edge2localEventhub = env.get("ROUTES_" + routeNumber + "_EDGE2LOCALEVENTHUB");
              EventHubRoute eventhub = new EventHubRoute(routeNumberInt, edge2localEventhub);
              eventhub.initLocal2EdgeSubscription();
              routes.add(eventhub);
              System.out.println("attaching route " + routeNumber);
              break;
          }

        }
      }

      System.out.println("endpoint configuration...");
      List<Endpoint> endpoints = new ArrayList<>();
      //look for env variables
      for (Map.Entry<String, String> entry : env.entrySet()) {
        String key = entry.getKey();
        String val = entry.getValue();
        if (key.toUpperCase().startsWith("ENDPOINTS_") &&
            key.toUpperCase().endsWith("_TYPE")) {
          String endpointNumber = key.toUpperCase().replace("ENDPOINTS_", "")
              .replace("_TYPE", "");

          final int endpointId;
          try {
            endpointId = Integer.parseInt(endpointNumber);
          } catch (Exception ex) {
            System.err.println("skipping endpoint, because id could not be determined or is not numeric: " + key);
            continue;
          }
          String endpointType = val.toLowerCase();
          switch (endpointType) {
            case "echo-on-edge-layer":
              String echoEdgeSelector = env.get("ENDPOINTS_" + endpointNumber + "_SELECTOR");
              if (echoEdgeSelector == null || echoEdgeSelector.isEmpty()) {
                echoEdgeSelector = "true";
              }
              String echoEdgeRouteName = env.get("ENDPOINTS_" + endpointNumber + "_ROUTE");
              Route echoEdgeRoute = null;
              if (echoEdgeRouteName == null) {
                if (routes.size() > 0) {
                  echoEdgeRoute = routes.get(0);
                } else {
                  System.err.println("no route defined");
                  System.exit(1);
                }
              } else {
                //look for numeric router identifier
                echoEdgeRoute = getRouteFromList(routes, echoEdgeRouteName);
              }

              if (echoEdgeRoute == null) {
                //skip endpoint if config cannot be determined
                System.out.println("route for endpoint " + endpointNumber + " not found.");
                continue;
              }
              Route finalEchoEdgeRoute = echoEdgeRoute;
              String finalEchoEdgeSelector = echoEdgeSelector;
              Endpoint echoEdgeEndpoint = new Endpoint() {{
                id = endpointId;
                type = "echo-on-edge-layer";
                selector = finalEchoEdgeSelector;
                route = finalEchoEdgeRoute;
              }};
              endpoints.add(echoEdgeEndpoint);
              System.out.println("adding endpoint " + endpointNumber);
              break;
            case "echo-on-local-layer":
              String echoSelector = env.get("ENDPOINTS_" + endpointNumber + "_SELECTOR");
              if (echoSelector == null || echoSelector.isEmpty()) {
                echoSelector = "true";
              }
              String echoRouteName = env.get("ENDPOINTS_" + endpointNumber + "_ROUTE");
              Route echoRoute = null;
              if (echoRouteName == null) {
                if (routes.size() > 0) {
                  echoRoute = routes.get(0);
                } else {
                  System.err.println("no route defined");
                  System.exit(1);
                }
              } else {
                //look for numeric router identifier
                echoRoute = getRouteFromList(routes, echoRouteName);
              }
              if (echoRoute == null) {
                //skip endpoint if config cannot be determined
                System.out.println("route for endpoint " + endpointNumber + " not found.");
                continue;
              }
              Route finalEchoRoute = echoRoute;
              String finalEchoSelector = echoSelector;
              Endpoint echoEndpoint = new Endpoint() {{
                id = endpointId;
                type = "echo-on-local-layer";
                selector = finalEchoSelector;
                route = finalEchoRoute;
              }};
              endpoints.add(echoEndpoint);
              System.out.println("adding endpoint " + endpointNumber);
              break;
            case "http":
              String httpSelector = env.get("ENDPOINTS_" + endpointNumber + "_SELECTOR");
              String httpRouteName = env.get("ENDPOINTS_" + endpointNumber + "_ROUTE");
              Route httpRoute = null;
              if (httpRouteName == null) {
                httpRoute = routes.get(0);
              } else {
                httpRoute = getRouteFromList(routes, httpRouteName);
              }
              if (httpRoute == null) {
                //skip endpoint if config cannot be determined
                System.out.println("route for endpoint " + endpointNumber + " not found.");
                continue;
              }
              Map<String, String> httpHeader = new HashMap<>();
              String httpUrl = env.get("ENDPOINTS_" + endpointNumber + "_URL");
              httpHeader.put("X-HTTP-ENDPOINT", httpUrl);
              for (Map.Entry<String, String> headerEntry : env.entrySet()) {
                String headerEntryKey = headerEntry.getKey();
                if (headerEntryKey.toUpperCase().startsWith("ENDPOINTS_" + endpointNumber + "_HEADER")) {
                  String headerValue = headerEntry.getValue();
                  int idx = headerValue.indexOf(":");
                  httpHeader.put(headerValue.substring(0, idx), headerValue.substring(idx + 1));
                }
              }
              Route finalHttpRoute = httpRoute;
              Endpoint httpEndpoint = new Endpoint() {{
                id = endpointId;
                type = endpointType;
                selector = httpSelector;
                route = finalHttpRoute;
                header = httpHeader;
              }};
              endpoints.add(httpEndpoint);
              System.out.println("adding endpoint " + endpointNumber);
              break;
            case "jms":
              String jmsSelector = env.get("ENDPOINTS_" + endpointNumber + "_SELECTOR");
              String jmsRouteName = env.get("ENDPOINTS_" + endpointNumber + "_ROUTE");
              Route jmsRoute = null;
              if (jmsRouteName == null) {
                jmsRoute = routes.get(0);
              } else {
                jmsRoute = getRouteFromList(routes, jmsRouteName);
              }
              if (jmsRoute == null) {
                //skip endpoint if config cannot be determined
                System.out.println("route for endpoint " + endpointNumber + " not found.");
                continue;
              }
              Map<String, String> jmsHeader = new HashMap<>();
              String jmsInitialFactory = env.get("ENDPOINTS_" + endpointNumber + "_INITIALFACTORY");
              String jmsProviderUrl = env.get("ENDPOINTS_" + endpointNumber + "_PROVIDERURL");
              String jmsDestination = env.get("ENDPOINTS_" + endpointNumber + "_DESTINATION");
              String jmsUser = env.get("ENDPOINTS_" + endpointNumber + "_USER");
              String jmsPassword = env.get("ENDPOINTS_" + endpointNumber + "_PASSWORD");
              String jmsFactory = env.getOrDefault("ENDPOINTS_" + endpointNumber + "_CONNECTIONFACTORY", "ConnectionFactory");
              jmsHeader.put("X-JMS-INITIALFACTORY", jmsInitialFactory);
              jmsHeader.put("X-JMS-CONNECTIONFACTORY", jmsFactory);
              jmsHeader.put("X-JMS-PROVIDERURL", jmsProviderUrl);
              jmsHeader.put("X-JMS-DESTINATION", jmsDestination);
              jmsHeader.put("X-JMS-USER", jmsUser);
              jmsHeader.put("X-JMS-PASSWORD", jmsPassword);
              Route finalJmsRoute = jmsRoute;
              Endpoint jmsEndpoint = new Endpoint() {{
                id = endpointId;
                type = endpointType;
                selector = jmsSelector;
                route = finalJmsRoute;
                header = jmsHeader;
              }};
              endpoints.add(jmsEndpoint);
              System.out.println("adding endpoint " + endpointNumber);
              break;
            default:
              System.out.println("endpoint type cannot be determined for " + endpointNumber + " with type " + endpointType);
              break;
          }
        }
      }
      Collections.sort(endpoints);

      //start server
      HttpServer server = HttpServer.create(new InetSocketAddress(port), 100);
      HttpContext all = server.createContext("/", new RequestHandler(endpoints));
      all.getFilters().add(new HealthCheckFilter());
      if (hosts.size() > 0) {
        all.getFilters().add(new HostFilter(hosts));
      }

      //shutdown hook
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        System.out.println("shutdown initiated");
        server.stop(1);
        //shutdown all routes
        routes.parallelStream().forEach(Route::shutdown);
      }));

      System.out.println("Rùkǒu edge is running.");
      System.out.println("http://localhost:" + port + "/");
      server.start();
    } catch (
        Exception ex) {
      ex.printStackTrace();
      System.exit(1);
    }
  }

  private static Route getRouteFromList(List<Route> routes, String echoRouteName) {
    try {
      int routeId = Integer.parseInt(echoRouteName);
      for (Route r : routes) {
        if (r.getId() == routeId || echoRouteName.equalsIgnoreCase(r.getAlias())) {
          return r;
        }
      }
    } catch (NumberFormatException ex) {
      for (Route r : routes) {
        if (r.getAlias().equalsIgnoreCase(echoRouteName)) {
          return r;
        }
      }
    }
    return null;
  }
}
