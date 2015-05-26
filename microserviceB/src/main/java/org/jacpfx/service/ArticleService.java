package org.jacpfx.service;

import com.google.gson.Gson;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import org.jacpfx.common.Operation;
import org.jacpfx.common.OperationType;
import org.jacpfx.common.ServiceInfo;
import org.jacpfx.common.Type;
import org.jacpfx.configuration.SpringConfiguration;
import org.jacpfx.entities.Article;
import org.jacpfx.repository.ArticleRepository;
import org.jacpfx.vertx.services.ServiceVerticle;
import org.jacpfx.vertx.spring.SpringVerticle;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.jacpfx.common.OperationResult.onSuccessOp;
import static org.jacpfx.common.ServiceInfoResult.onSuccessService;


/**
 * Created by Andy Moncsek on 12.05.15.
 */
@ApplicationPath("/articleService")
@Component(value = "ArticleService")
@SpringVerticle(springConfig = SpringConfiguration.class)
public class ArticleService extends ServiceVerticle {

    private Gson gson = new Gson();
    private
    @Inject
    ArticleRepository repository;
    // http://.../findAll
    // http:// .../find/{magazine}
    // http://.../findAllWithComments
    // http://.../comments/{articleId}


    @Path("/findAll")
    @OperationType(Type.REST_GET)
    public void findAll(final Message message) {
        dicovery.getService("/userCommentsService", onSuccessService(si ->
                        si.getOperation("/fetchByArticleIdREST/:articleId",
                                onSuccessOp(op ->
                                        getArticlesAndUpdateCommentURL(op, message), fail ->
                                        message.reply(gson.toJson(repository.getAllArticles())))),
                fail -> message.reply(gson.toJson(repository.getAllArticles()))));

    }

    @Path("/findMAX/:amount")
    @OperationType(Type.REST_GET)
    public void findMax(@PathParam("amount") final String amount, final Message message) {
        dicovery.getService("/userCommentsService", onSuccessService(si -> si.getOperation("/fetchByArticleIdWS", onSuccessOp(op -> {
            final String url = op.getUrl();
            Collection<Article> articles = repository.getAllArticles(Integer.valueOf(amount));

            List<Article> mappedArticles = articles.stream().map(a -> new Article(a, url)).collect(Collectors.toList());
            message.reply(gson.toJson(mappedArticles));
        }, fail -> message.reply(gson.toJson(repository.getAllArticles(Integer.valueOf(amount)))))),
                fail -> message.reply(gson.toJson(repository.getAllArticles(Integer.valueOf(amount))))));

    }

    @Path("/findPage/:page")
    @OperationType(Type.REST_GET)
    public void findPage(@PathParam("page") final String page, final Message message) {
        dicovery.getService("/userCommentsService", onSuccessService(si -> si.getOperation("/fetchByArticleIdWS", onSuccessOp(op -> {
                    final String url = op.getUrl();
                    Collection<Article> articles = repository.getAllArticlesPages(Integer.valueOf(page));

                    List<Article> mappedArticles = articles.stream().map(a -> new Article(a, url)).collect(Collectors.toList());
                    message.reply(gson.toJson(mappedArticles));
                }, fail -> message.reply(gson.toJson(repository.getAllArticlesPages(Integer.valueOf(page)))))),
                fail -> message.reply(gson.toJson(repository.getAllArticlesPages(Integer.valueOf(page))))));

    }


    private void getArticlesAndUpdateCommentURL(Operation op, final Message message) {
        final String url = op.getUrl();
        List<Article> articles = repository.getAllArticles().stream().map(article -> new Article(article, url.replace(":articleId", article.getId()))).collect(Collectors.toList());
        message.reply(gson.toJson(articles));
    }

    @Path("/comments/:articleId")
    @OperationType(Type.REST_GET)
    public void findComments(@PathParam("articleId") final String articleId, Message message) {

        dicovery.getService("/userCommentsService", onSuccessService(si ->
                        getOperation(articleId, message, si),
                fail -> message.reply("/userCommentsService not available: " + fail.getMessage())));

    }

    private ServiceInfo getOperation(String articleId, Message message, ServiceInfo si) {
        return si.getOperation("/fetchByArticleIdWS", onSuccessOp(op ->
                getWSConnection(articleId, message, op), fail -> message.reply("/fetchByArticleIdWS method not available: " + fail.getMessage())));
    }

    private Operation getWSConnection(String articleId, Message message, Operation op) {
        return op.websocketConnection(ws -> {
            ws.handler(data -> {
                // reply to rest-request
                message.reply(new String(data.getBytes()));
                ws.close();
            });
            // send message
            ws.writeMessage(Buffer.buffer(articleId));
        });
    }

    public static void main(String[] args) {
        VertxOptions vOpts = new VertxOptions();
        DeploymentOptions options = new DeploymentOptions().setInstances(4);
        vOpts.setClustered(true);
        Vertx.clusteredVertx(vOpts, cluster-> {
            if(cluster.succeeded()){
                final Vertx result = cluster.result();
                result.deployVerticle("java-spring:org.jacpfx.service.ArticleService",options, handle -> {

                });
            }
        });
    }
}



