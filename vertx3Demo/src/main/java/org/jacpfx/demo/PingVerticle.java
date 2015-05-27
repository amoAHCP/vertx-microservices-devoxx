package org.jacpfx.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * Created by Andy Moncsek on 12.05.15.
 */
public class PingVerticle extends AbstractVerticle {
    public void start(Future<Void> startFuture) throws Exception {

        vertx.eventBus().consumer("ping", eventHandler -> {
            System.out.println(Thread.currentThread()+" got: " + eventHandler.body());
            vertx.eventBus().send("pong", "ping");
        });

        vertx.deployVerticle(new org.jacpfx.solution.PongVerticle(), new DeploymentOptions(), finished -> {
            if (finished.succeeded()) {
                startFuture.complete();
                vertx.eventBus().send("pong", "ping");
            }
        });


    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new PingVerticle());
    }
}
