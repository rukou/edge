package io.rukou.edge;

import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.common.HttpObject;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.RequestHeaders;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class HttpRequestHandler implements HttpService {
  final List<Endpoint> endpoints;
  final ScriptEngine jsEngine;

  public HttpRequestHandler(List<Endpoint> endpoints) {
    this.endpoints = endpoints;
    ScriptEngineManager mgr = new ScriptEngineManager();
    this.jsEngine = mgr.getEngineByName("JavaScript");
    if (this.jsEngine == null) {
      System.err.println("no js engine found");
    }

  }

  public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
    System.out.println(req.method().toString() + " " + req.path());
    try {
      //extract message object
      Message msg = new Message();
      String requestId = UUID.randomUUID().toString();
      msg.header.put("X-REQUEST-ID", requestId);
      msg.header.put("X-MESSAGE-VERSION", "2020-05-22");
      msg.header.put("X-MESSAGE-TYPE", "http-request");
      msg.header.put("X-HTTP-METHOD", req.method().toString());
      msg.header.put("X-HTTP-PATH", req.path());
      msg.header.put("X-HTTP-TIMESTAMP", Instant.now().toString());
      msg.header.put("X-HTTP-HOST", req.headers().get("Host"));
      RequestHeaders headers = req.headers();
      headers.forEach((key, val) -> {
        msg.header.put(key.toString(), val);
      });

      System.out.println("get aggregate");
      System.out.println("waiting for future");
      req.subscribe(new Subscriber<HttpObject>() {
                      @Override
                      public void onSubscribe(Subscription subscription) {
                        System.out.println("onSubscribe");
                      }

                      @Override
                      public void onNext(HttpObject httpObject) {
                        System.out.println("eos: "+httpObject.isEndOfStream());
                        System.out.println("eos: "+httpObject.toString());
                      }

                      @Override
                      public void onError(Throwable throwable) {
                        System.out.println("error");
                        throwable.printStackTrace();
                      }

                      @Override
                      public void onComplete() {
                        System.out.println("request done");
                      }
                    }
      );
//      CompletableFuture<AggregatedHttpRequest> agg = req.aggregate();

//      AggregatedHttpRequest requestData = agg.get(2, TimeUnit.SECONDS);
      System.out.println("get content string");
//      msg.body = requestData.contentUtf8();
msg.body="";
      //determine endpoint
      try {
        jsEngine.put("header", msg.header);
        for (Endpoint endpoint : endpoints) {
          jsEngine.eval("result = " + endpoint.selector);
          Boolean selectorMatches = (Boolean) jsEngine.get("result");
          if (selectorMatches) {
            System.out.println("delivering to endpoint " + endpoint.alias + " (" + endpoint.type + ")");
            msg.header.put("X-ENDPOINT-TYPE", endpoint.type);
            msg.header.put("X-ENDPOINT-ID", String.valueOf(endpoint.id));
            msg.header.putAll(endpoint.header);
            CompletableFuture<Message> result = endpoint.route.invokeEdge2Local(msg);
            Message responseMsg = result.join();
            return HttpResponse.of(205);
          }
        }
      } catch (ScriptException ex) {
        ex.printStackTrace();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      return HttpResponse.of(500);
    }
    return HttpResponse.of(200);
  }
}
