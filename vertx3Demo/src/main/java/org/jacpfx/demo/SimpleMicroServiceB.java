package org.jacpfx.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Created by Andy Moncsek on 27.05.15.
 */
public class SimpleMicroServiceB extends AbstractVerticle {

    public void start(Future<Void> startFuture) throws Exception {

        vertx.eventBus().consumer("simple.serviceB",handler -> {
            handler.reply("hello world: "+handler.body());
        }) ;

        startFuture.complete();
    }
}
