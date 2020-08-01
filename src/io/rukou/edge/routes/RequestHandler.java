package io.rukou.edge.routes;

import io.grpc.stub.StreamObserver;
import io.rukou.routing.v1.RequestHandlerGrpc.RequestHandlerImplBase;
import io.rukou.routing.v1.Service;
import io.rukou.routing.v1.Service.Ack;
import io.rukou.routing.v1.Service.Message;

import java.util.concurrent.CompletableFuture;

public class RequestHandler extends RequestHandlerImplBase {
  public RequestHandler() {
  }

  @Override
  public void edgetolocal(Service.Capacity capacity, StreamObserver<Message> responseObserver) {
    int cap = capacity.getRequests();
    if(cap<1 || cap >99) {
      cap=1;
    }
    for(int idx=0;idx<cap;idx++) {
      if(!MemoryRoute.INSTANCE.stack.empty()) {
        io.rukou.edge.Message msg = MemoryRoute.INSTANCE.stack.pop();
        Message m = Message.newBuilder().putAllHeader(msg.header)
                .setBody(msg.body).build();
        responseObserver.onNext(m);
      }
    }
    responseObserver.onCompleted();
  }

  @Override
  public void localtoedge(Message responseMsg, StreamObserver<Ack> responseObserver) {
    io.rukou.edge.Message msg = new io.rukou.edge.Message();
    msg.header = responseMsg.getHeaderMap();
    msg.body = responseMsg.getBody();
    responseObserver.onNext(Ack.newBuilder().setStatus("ok").build());
    responseObserver.onCompleted();
    String requestId = msg.getRequestId();
    CompletableFuture<io.rukou.edge.Message> future = MemoryRoute.INSTANCE.futureCache.getIfPresent(requestId);
    future.complete(msg);
  }
}
