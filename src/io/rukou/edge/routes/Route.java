package io.rukou.edge.routes;

import io.rukou.edge.objects.Message;

public abstract class Route {
  protected String type;

  public abstract String invokeEdge2Local(Message msg);
  public abstract void initLocal2EdgeSubscription();
}
