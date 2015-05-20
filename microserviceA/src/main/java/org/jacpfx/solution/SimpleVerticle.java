package org.jacpfx.solution;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;

/**
 * Created by Andy Moncsek on 12.05.15.
 */
public class SimpleVerticle extends AbstractVerticle {

    public void start(Future<Void> startFuture) throws Exception {

        vertx.
                createHttpServer(new HttpServerOptions().setHost("localhost")).
                requestHandler(request -> request.response().end("Hello World"))
                .listen(8080);

        startFuture.complete();
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new SimpleVerticle());
    }
}
