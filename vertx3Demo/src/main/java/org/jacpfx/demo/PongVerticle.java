package org.jacpfx.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Created by Andy Moncsek on 12.05.15.
 */
public class PongVerticle extends AbstractVerticle {
    public void start(Future<Void> startFuture) throws Exception {

        vertx.eventBus().consumer("pong",eventHandler -> {
            System.out.println(Thread.currentThread()+" got: " + eventHandler.body());
            vertx.eventBus().send("ping", "pong");
        });

        startFuture.complete();
    }
}
