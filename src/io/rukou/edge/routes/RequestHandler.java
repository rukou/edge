//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.rukou.edge.routes;

import io.grpc.stub.StreamObserver;
import io.rukou.routing.RequestHandlerGrpc.RequestHandlerImplBase;
import io.rukou.routing.Service.Ack;
import io.rukou.routing.Service.Message;

public class RequestHandler extends RequestHandlerImplBase {
  public RequestHandler() {
  }

  public void getRequest(Ack request, StreamObserver<Message> responseObserver) {
    super.getRequest(request, responseObserver);
  }

  public StreamObserver<Message> respond(StreamObserver<Ack> responseObserver) {
    return super.respond(responseObserver);
  }
}
