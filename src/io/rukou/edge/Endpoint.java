package io.rukou.edge;

import io.rukou.edge.routes.Route;

import java.util.HashMap;
import java.util.Map;

public class Endpoint implements Comparable<Endpoint>{

  public int id;
  public String alias;
  public String type="echo-on-edge-layer";
  public String selector="";
  public Route route;
  public Map<String, String> header=new HashMap<>();

  @Override
  public int compareTo(Endpoint ep) {
    if(ep != null){
      return Integer.compare(this.id, ep.id);
    }else{
      return 0;
    }
  }
}
