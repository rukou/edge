package io.rukou.edge.routes;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.rukou.edge.Message;

import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public enum MemoryRoute {
  INSTANCE;

  public Cache<String, CompletableFuture<Message>> futureCache;
  public Stack<Message> stack = new Stack<>();

  MemoryRoute() {
    this.futureCache = CacheBuilder.newBuilder().expireAfterAccess(30L, TimeUnit.SECONDS).removalListener((removal) -> {
      System.out.println("request timeout: " + ((Message)removal.getValue()).toString());
    }).build();
  }

  public void shutdown() {
  }

  public Future<Message> invokeEdge2Local(Message msg) {
    CompletableFuture<Message> completableFuture
            = new CompletableFuture<>();

    this.stack.push(msg);
    this.futureCache.put(msg.getRequestId(), completableFuture);

    return completableFuture;
  }

  public void initLocal2EdgeSubscription() {
  }
}
