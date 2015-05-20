package org.jacpfx.demo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Created by Andy Moncsek on 12.05.15.
 */
public class PingVerticle extends AbstractVerticle {
    public void start(Future<Void> startFuture) throws Exception {



        startFuture.complete();
    }
}
