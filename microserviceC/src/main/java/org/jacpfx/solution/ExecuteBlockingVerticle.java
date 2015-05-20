package org.jacpfx.solution;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

/**
 * Created by Andy Moncsek on 12.05.15.
 */
public class ExecuteBlockingVerticle extends AbstractVerticle {
    public void start(Future<Void> startFuture) throws Exception {


        startFuture.complete();

        vertx.executeBlocking(future -> {
            // Imagine this was a call to a blocking API to get the result
            try {
                Thread.sleep(500);
            } catch (Exception ignore) {
            }
            System.out.println(Thread.currentThread());

            future.complete("myValue");
        }, (value) -> System.out.println(value.result() + "  " + Thread.currentThread()));
    }


    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new ExecuteBlockingVerticle());
    }
}
