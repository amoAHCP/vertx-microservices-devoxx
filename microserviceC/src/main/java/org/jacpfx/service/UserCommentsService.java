package org.jacpfx.service;

import com.google.gson.Gson;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.jacpfx.common.MessageReply;
import org.jacpfx.common.OperationType;
import org.jacpfx.common.Type;
import org.jacpfx.configuration.SpringConfiguration;
import org.jacpfx.repository.UserCommentRepository;
import org.jacpfx.vertx.services.ServiceVerticle;
import org.jacpfx.vertx.spring.SpringVerticle;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
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
    public void fetchAllUserCommentsWS(String message, MessageReply reply) {
        findAll((value)-> reply.reply(gson.toJson(value)));
    }

    @Path("/fetchAllREST")
    @OperationType(Type.REST_GET)
    public void fetchAllUserCommentsREST(Message message) {
        findAll((value)-> message.reply(gson.toJson(value)));
    }



    @Path("/fetchByArticleIdWS")
    @OperationType(Type.WEBSOCKET)
    public void fetchByArticleIdWS(String articleId, MessageReply reply) {
        findById((value)-> reply.reply(gson.toJson(value)),articleId);
    }

    @Path("/fetchByArticleIdREST/:articleId")
    @OperationType(Type.REST_GET)
    public void fetchByArticleIdREST(@PathParam("articleId") String articleId, Message message) {
        findById((value)-> message.reply(gson.toJson(value)),articleId);
    }

    private void findById(Consumer<Object> consumer,String articleId) {
        this.vertx.executeBlocking(future->
                future.complete(repository.findCommentsByArticleId(articleId)),
                (value)-> consumer.accept(value.result()));
    }

    private void findAll(Consumer<Object> consumer) {
        this.vertx.executeBlocking(future->
                future.complete(repository.getAllComments()),
                (value)-> consumer.accept(value.result()));
    }

    public static void main(String[] args) {
        DeploymentOptions options = new DeploymentOptions().setInstances(1);
        options.setConfig(new JsonObject().put("clustered", true));
        Vertx.vertx().deployVerticle(new UserCommentsService(),options);
    }
}
