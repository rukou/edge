//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.rukou.edge;

import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.server.HttpService;
import com.linecorp.armeria.server.ServiceRequestContext;
import java.util.List;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class RequestHandler implements HttpService {
  final List<Endpoint> endpoints;
  final ScriptEngine jsEngine;

  public RequestHandler(List<Endpoint> endpoints) {
    this.endpoints = endpoints;
    ScriptEngineManager mgr = new ScriptEngineManager();
    this.jsEngine = mgr.getEngineByName("JavaScript");
    if (this.jsEngine == null) {
      System.err.println("no js engine found");
    }

  }

  public HttpResponse serve(ServiceRequestContext ctx, HttpRequest req) throws Exception {
    return HttpResponse.of(200);
  }
}
