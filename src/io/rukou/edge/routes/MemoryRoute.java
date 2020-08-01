//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.rukou.edge.routes;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.rukou.edge.Message;
import java.util.concurrent.TimeUnit;

public enum MemoryRoute {
  INSTANCE;

  private Cache<String, Message> cache;

  private MemoryRoute() {
    this.cache = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.SECONDS).removalListener((removal) -> {
      System.out.println("request timeout: " + ((Message)removal.getValue()).toString());
    }).build();
  }

  public void shutdown() {
  }

  public void invokeEdge2Local(Message msg) {
    this.cache.put(msg.getRequestId(), msg);
  }

  public void initLocal2EdgeSubscription() {
  }
}
