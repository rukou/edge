package io.rukou.edge;

import io.rukou.edge.routes.MemoryRoute;
import java.util.HashMap;
import java.util.Map;

public class Endpoint implements Comparable<Endpoint> {
  public int id;
  public String alias;
  public String type = "echo-on-edge-layer";
  public String selector = "";
  public MemoryRoute route;
  public Map<String, String> header = new HashMap();

  public Endpoint() {
  }

  public int compareTo(Endpoint ep) {
    return ep != null ? Integer.compare(this.id, ep.id) : 0;
  }
}
