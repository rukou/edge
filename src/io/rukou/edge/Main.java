package io.rukou.edge;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.grpc.GrpcService;
import io.rukou.edge.routes.GrpcRequestHandler;
import io.rukou.edge.routes.MemoryRoute;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {
  public static int port = 8080;
  public static String podName = "";

  public Main() {
  }

  public static void main(String[] args) {
    try {
      Map<String, String> env = System.getenv();
      if (env.containsKey("port")) {
        port = Integer.parseInt((String)env.get("port"));
      }

      if (env.containsKey("PORT")) {
        port = Integer.parseInt((String)env.get("PORT"));
      }

      if (env.containsKey("POD_NAME")) {
        podName = (String)env.get("POD_NAME");
      } else {
        podName = InetAddress.getLocalHost().getHostName();
      }

      System.out.println("host configuration...");
      List<Host> hosts = new ArrayList();
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
                String finalEchoEdgeSelector = echoEdgeSelector;
                Endpoint echoEdgeEndpoint = new Endpoint() {{
                  id = endpointId;
                  type = "echo-on-edge-layer";
                  selector = finalEchoEdgeSelector;
                  route = MemoryRoute.INSTANCE;
                }};
                endpoints.add(echoEdgeEndpoint);
                System.out.println("adding endpoint " + endpointNumber);
                break;
              case "echo-on-local-layer":
                String echoSelector = env.get("ENDPOINTS_" + endpointNumber + "_SELECTOR");
                if (echoSelector == null || echoSelector.isEmpty()) {
                  echoSelector = "true";
                }
                String finalEchoSelector = echoSelector;
                Endpoint echoEndpoint = new Endpoint() {{
                  id = endpointId;
                  type = "echo-on-local-layer";
                  selector = finalEchoSelector;
                  route = MemoryRoute.INSTANCE;
                }};
                endpoints.add(echoEndpoint);
                System.out.println("adding endpoint " + endpointNumber);
                break;
              case "http":
                String httpSelector = env.get("ENDPOINTS_" + endpointNumber + "_SELECTOR");
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
                Endpoint httpEndpoint = new Endpoint() {{
                  id = endpointId;
                  type = endpointType;
                  selector = httpSelector;
                  route = MemoryRoute.INSTANCE;
                  header = httpHeader;
                }};
                endpoints.add(httpEndpoint);
                System.out.println("adding endpoint " + endpointNumber);
                break;
              case "jms":
                String jmsSelector = env.get("ENDPOINTS_" + endpointNumber + "_SELECTOR");
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
                Endpoint jmsEndpoint = new Endpoint() {{
                  id = endpointId;
                  type = endpointType;
                  selector = jmsSelector;
                  route = MemoryRoute.INSTANCE;
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
      ServerBuilder sb = Server.builder();
      sb.http(new InetSocketAddress(port));
      sb.serviceUnder("/", new HttpRequestHandler(endpoints));
      sb.service(GrpcService.builder().addService(new GrpcRequestHandler()).build());
      Server server = sb.build();
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        System.out.println("shutdown initiated");
        try {
          server.stop().get(30, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
          //ignore
        } catch (Exception e) {
          e.printStackTrace();
        }
      }));
      System.out.println("Rùkǒu edge is running.");
      System.out.println("http://localhost:" + port + "/");
      server.start();

    } catch (Exception var34) {
      var34.printStackTrace();
      System.exit(1);
    }
  }
}
