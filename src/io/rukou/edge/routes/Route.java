package io.rukou.edge.routes;

import io.rukou.edge.Message;

public abstract class Route {

  public abstract String getType();
  public abstract String getId();

  public abstract void shutdown();

  public abstract String invokeEdge2Local(Message msg);
  public abstract void initLocal2EdgeSubscription();
}
