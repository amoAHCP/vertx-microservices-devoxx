package org.jacpfx.service;

import com.google.gson.Gson;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.Message;
import org.jacpfx.common.OperationType;
import org.jacpfx.common.Type;
import org.jacpfx.common.WSMessageReply;
import org.jacpfx.configuration.SpringConfiguration;
import org.jacpfx.repository.UserCommentRepository;
import org.jacpfx.vertx.services.ServiceVerticle;
import org.jacpfx.vertx.spring.SpringVerticle;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.function.Consumer;

/**
 * Created by Andy Moncsek on 13.05.15.
 */
@ApplicationPath("/userCommentsService")
@Component(value = "userCommentsService")
@SpringVerticle(springConfig = SpringConfiguration.class)
public class UserCommentsService extends ServiceVerticle {
    private Gson gson = new Gson();
    private  @Inject UserCommentRepository repository;

    @Path("/fetchAllWS")
    @OperationType(Type.WEBSOCKET)
    @Produces("application/json")
    public void fetchAllUserCommentsWS(String message, WSMessageReply reply) {
        findAll((value)-> reply.reply(gson.toJson(value)));
    }

    @Path("/fetchAllREST")
    @OperationType(Type.REST_GET)
    public void fetchAllUserCommentsREST(Message message) {
        findAll((value)-> message.reply(gson.toJson(value)));
    }



    @Path("/fetchByArticleIdWS")
    @OperationType(Type.WEBSOCKET)
    public void fetchByArticleIdWS(String articleId, WSMessageReply reply) {
        findById((value)-> reply.reply(gson.toJson(value)),articleId);
    }

    @Path("/fetchByArticleIdREST/:articleId")
    @OperationType(Type.REST_GET)
    public void fetchByArticleIdREST(@PathParam("articleId") String articleId, Message message) {
        findById((value)-> message.reply(gson.toJson(value)),articleId);
    }

    private void findById(Consumer<Object> consumer,String articleId) {
        vertx.executeBlocking(future->
                future.complete(repository.findCommentsByArticleId(articleId)),
                (value)-> consumer.accept(value.result()));
    }

    private void findAll(Consumer<Object> consumer) {
        vertx.executeBlocking(future->
                future.complete(repository.getAllComments()),
                (value)-> consumer.accept(value.result()));
    }

    /**
     * easy development mode
     * @param args
     */
    public static void main(String[] args) {
        VertxOptions vOpts = new VertxOptions();
        DeploymentOptions options = new DeploymentOptions().setInstances(4);
        vOpts.setClustered(true);
        Vertx.clusteredVertx(vOpts, cluster-> {
            if(cluster.succeeded()){
                final Vertx result = cluster.result();
                result.deployVerticle("java-spring:org.jacpfx.service.UserCommentsService",options, handle -> {

                });
            }
        });
    }

}
