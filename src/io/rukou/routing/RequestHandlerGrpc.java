package io.rukou.routing;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.31.0)",
    comments = "Source: service.proto")
public final class RequestHandlerGrpc {

  private RequestHandlerGrpc() {}

  public static final String SERVICE_NAME = "io.rukou.routing.RequestHandler";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<io.rukou.routing.Service.Ack,
      io.rukou.routing.Service.Message> getGetRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getRequest",
      requestType = io.rukou.routing.Service.Ack.class,
      responseType = io.rukou.routing.Service.Message.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<io.rukou.routing.Service.Ack,
      io.rukou.routing.Service.Message> getGetRequestMethod() {
    io.grpc.MethodDescriptor<io.rukou.routing.Service.Ack, io.rukou.routing.Service.Message> getGetRequestMethod;
    if ((getGetRequestMethod = RequestHandlerGrpc.getGetRequestMethod) == null) {
      synchronized (RequestHandlerGrpc.class) {
        if ((getGetRequestMethod = RequestHandlerGrpc.getGetRequestMethod) == null) {
          RequestHandlerGrpc.getGetRequestMethod = getGetRequestMethod =
              io.grpc.MethodDescriptor.<io.rukou.routing.Service.Ack, io.rukou.routing.Service.Message>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.rukou.routing.Service.Ack.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.rukou.routing.Service.Message.getDefaultInstance()))
              .setSchemaDescriptor(new RequestHandlerMethodDescriptorSupplier("getRequest"))
              .build();
        }
      }
    }
    return getGetRequestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<io.rukou.routing.Service.Message,
      io.rukou.routing.Service.Ack> getRespondMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "respond",
      requestType = io.rukou.routing.Service.Message.class,
      responseType = io.rukou.routing.Service.Ack.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<io.rukou.routing.Service.Message,
      io.rukou.routing.Service.Ack> getRespondMethod() {
    io.grpc.MethodDescriptor<io.rukou.routing.Service.Message, io.rukou.routing.Service.Ack> getRespondMethod;
    if ((getRespondMethod = RequestHandlerGrpc.getRespondMethod) == null) {
      synchronized (RequestHandlerGrpc.class) {
        if ((getRespondMethod = RequestHandlerGrpc.getRespondMethod) == null) {
          RequestHandlerGrpc.getRespondMethod = getRespondMethod =
              io.grpc.MethodDescriptor.<io.rukou.routing.Service.Message, io.rukou.routing.Service.Ack>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "respond"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.rukou.routing.Service.Message.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  io.rukou.routing.Service.Ack.getDefaultInstance()))
              .setSchemaDescriptor(new RequestHandlerMethodDescriptorSupplier("respond"))
              .build();
        }
      }
    }
    return getRespondMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RequestHandlerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RequestHandlerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RequestHandlerStub>() {
        @Override
        public RequestHandlerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RequestHandlerStub(channel, callOptions);
        }
      };
    return RequestHandlerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RequestHandlerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RequestHandlerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RequestHandlerBlockingStub>() {
        @Override
        public RequestHandlerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RequestHandlerBlockingStub(channel, callOptions);
        }
      };
    return RequestHandlerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RequestHandlerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RequestHandlerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RequestHandlerFutureStub>() {
        @Override
        public RequestHandlerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RequestHandlerFutureStub(channel, callOptions);
        }
      };
    return RequestHandlerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class RequestHandlerImplBase implements io.grpc.BindableService {

    /**
     */
    public void getRequest(io.rukou.routing.Service.Ack request,
        io.grpc.stub.StreamObserver<io.rukou.routing.Service.Message> responseObserver) {
      asyncUnimplementedUnaryCall(getGetRequestMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<io.rukou.routing.Service.Message> respond(
        io.grpc.stub.StreamObserver<io.rukou.routing.Service.Ack> responseObserver) {
      return asyncUnimplementedStreamingCall(getRespondMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetRequestMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                io.rukou.routing.Service.Ack,
                io.rukou.routing.Service.Message>(
                  this, METHODID_GET_REQUEST)))
          .addMethod(
            getRespondMethod(),
            asyncClientStreamingCall(
              new MethodHandlers<
                io.rukou.routing.Service.Message,
                io.rukou.routing.Service.Ack>(
                  this, METHODID_RESPOND)))
          .build();
    }
  }

  /**
   */
  public static final class RequestHandlerStub extends io.grpc.stub.AbstractAsyncStub<RequestHandlerStub> {
    private RequestHandlerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RequestHandlerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RequestHandlerStub(channel, callOptions);
    }

    /**
     */
    public void getRequest(io.rukou.routing.Service.Ack request,
        io.grpc.stub.StreamObserver<io.rukou.routing.Service.Message> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<io.rukou.routing.Service.Message> respond(
        io.grpc.stub.StreamObserver<io.rukou.routing.Service.Ack> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(getRespondMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class RequestHandlerBlockingStub extends io.grpc.stub.AbstractBlockingStub<RequestHandlerBlockingStub> {
    private RequestHandlerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RequestHandlerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RequestHandlerBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<io.rukou.routing.Service.Message> getRequest(
        io.rukou.routing.Service.Ack request) {
      return blockingServerStreamingCall(
          getChannel(), getGetRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RequestHandlerFutureStub extends io.grpc.stub.AbstractFutureStub<RequestHandlerFutureStub> {
    private RequestHandlerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RequestHandlerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RequestHandlerFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_GET_REQUEST = 0;
  private static final int METHODID_RESPOND = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RequestHandlerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RequestHandlerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_REQUEST:
          serviceImpl.getRequest((io.rukou.routing.Service.Ack) request,
              (io.grpc.stub.StreamObserver<io.rukou.routing.Service.Message>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RESPOND:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.respond(
              (io.grpc.stub.StreamObserver<io.rukou.routing.Service.Ack>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RequestHandlerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RequestHandlerBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return io.rukou.routing.Service.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RequestHandler");
    }
  }

  private static final class RequestHandlerFileDescriptorSupplier
      extends RequestHandlerBaseDescriptorSupplier {
    RequestHandlerFileDescriptorSupplier() {}
  }

  private static final class RequestHandlerMethodDescriptorSupplier
      extends RequestHandlerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RequestHandlerMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RequestHandlerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RequestHandlerFileDescriptorSupplier())
              .addMethod(getGetRequestMethod())
              .addMethod(getRespondMethod())
              .build();
        }
      }
    }
    return result;
  }
}
