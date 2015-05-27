package org.jacpfx.demo;

import io.vertx.core.*;

/**
 * Created by Andy Moncsek on 27.05.15.
 */
public class ComplexMicroServiceB extends AbstractVerticle {

    public void start(Future<Void> startFuture) throws Exception {

        vertx.eventBus().consumer("simple.serviceB",handler -> {
            handler.reply("hello world: "+handler.body());
        }) ;

        startFuture.complete();
    }

    public static void main(String[] args) {
        VertxOptions vOpts = new VertxOptions();
        DeploymentOptions options = new DeploymentOptions().setInstances(1);
        vOpts.setClustered(true);
        Vertx.clusteredVertx(vOpts, cluster -> {
            if (cluster.succeeded()) {
                final Vertx result = cluster.result();
                result.deployVerticle("org.jacpfx.demo.ComplexMicroServiceB", options, handle -> {

                });
            }
        });
    }
}
