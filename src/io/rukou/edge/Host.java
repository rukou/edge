package io.rukou.edge;

import java.util.HashMap;

public class Host {
  public String domain;
  public String auth;
  public HashMap<String,String> apiKeys = new HashMap<>();

  public String toString(){
    return domain+" (authentication: "+auth+")";
  }
}
