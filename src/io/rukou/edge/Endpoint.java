package io.rukou.edge;

import io.rukou.edge.routes.Route;

import java.util.HashMap;
import java.util.Map;

public class Endpoint {

  public String id;
  public String type="echo";
  public String selector="";
  public Route route;
  public Map<String, String> header=new HashMap<>();
}
