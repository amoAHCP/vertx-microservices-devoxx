package org.jacpfx.demo;

import io.vertx.core.*;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerRequest;

/**
 * Created by Andy Moncsek on 27.05.15.
 */
public class ComplexMicroServiceA extends AbstractVerticle {

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

      startFuture.complete();
    }

    private void invlaidRequest(HttpServerRequest request) {
        vertx.eventBus().send("simple.serviceBXYZ", "", response -> {
            if(response.failed())request.response().end(response.cause().toString());

        });
    }

    private void validRequest(HttpServerRequest request) {
        final String value = request.getParam("val");
        vertx.eventBus().send("simple.serviceB", value!=null?value:"", response -> {
            request.response().end(response.result().body().toString());
        });
    }

    public static void main(String[] args) {
        VertxOptions vOpts = new VertxOptions();
        DeploymentOptions options = new DeploymentOptions().setInstances(1);
        vOpts.setClustered(true);
        Vertx.clusteredVertx(vOpts, cluster-> {
            if(cluster.succeeded()){
                final Vertx result = cluster.result();
                result.deployVerticle("org.jacpfx.demo.ComplexMicroServiceA",options, handle -> {

                });
            }
        });
    }

}
