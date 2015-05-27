package org.jacpfx.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;

/**
 * Created by Andy Moncsek on 27.05.15.
 */
public class SimpleMicroServiceA extends AbstractVerticle {

    public void start(Future<Void> startFuture) throws Exception {

        vertx.
                createHttpServer(new HttpServerOptions().setHost("localhost")).
                requestHandler(request -> {
                    if (request.path().contains("ok")) {
                        validRequest(request);
                    } else {
                        invlaidRequest(request);
                    }

                })
                .listen(8080);

        deployServiceB(startFuture);
    }

    private void invlaidRequest(HttpServerRequest request) {
        final String value = request.getParam("val");
        vertx.eventBus().send("simple.serviceBXYZ", value!=null?value:"", response -> {
            if(response.failed())request.response().end(response.cause().toString());

        });
    }

    private void validRequest(HttpServerRequest request) {
        final String value = request.getParam("val");
        vertx.eventBus().send("simple.serviceB",  value!=null?value:"", response -> {
            request.response().end(response.result().body().toString());
        });
    }

    private void deployServiceB(Future<Void> startFuture) {
        vertx.deployVerticle(new SimpleMicroServiceB() , new DeploymentOptions(), finished -> {
            if (finished.succeeded())
                startFuture.complete();

        });
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new SimpleMicroServiceA());
    }
}
