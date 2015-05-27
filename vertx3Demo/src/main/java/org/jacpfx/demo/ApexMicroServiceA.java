package org.jacpfx.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.apex.Router;
import io.vertx.ext.apex.RoutingContext;

/**
 * Created by Andy Moncsek on 27.05.15.
 */
public class ApexMicroServiceA extends AbstractVerticle {

    public void start(Future<Void> startFuture) throws Exception {


        HttpServer server = vertx. createHttpServer(new HttpServerOptions().setHost("localhost"));
        Router router = Router.router(vertx);

        router.route("/ok/:val").handler(routingContext -> validRequest(routingContext));

        router.route("/nok/:val").handler(routingContext -> invlaidRequest(routingContext));

        server.requestHandler(router::accept).listen(8080);

        deployServiceB(startFuture);
    }

    private void invlaidRequest(RoutingContext routingContext) {
        final HttpServerResponse httpResponse = routingContext.response();
        String val = routingContext.request().getParam("val");
        vertx.eventBus().send("simple.serviceBXYZ", val!=null?val:"", response -> {
            if(response.failed())httpResponse.end(response.cause().toString());

        });
    }

    private void validRequest(RoutingContext routingContext) {
        final HttpServerResponse httpResponse = routingContext.response();
        String val = routingContext.request().getParam("val");
        vertx.eventBus().send("simple.serviceB",  val!=null?val:"", response -> {
            httpResponse.end(response.result().body().toString());
        });
    }

    private void deployServiceB(Future<Void> startFuture) {
        vertx.deployVerticle(new SimpleMicroServiceB() , new DeploymentOptions(), finished -> {
            if (finished.succeeded())
                startFuture.complete();

        });
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new ApexMicroServiceA());
    }
}
