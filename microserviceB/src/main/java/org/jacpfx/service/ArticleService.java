package org.jacpfx.service;

import org.jacpfx.common.MessageReply;
import org.jacpfx.common.OperationType;
import org.jacpfx.common.Type;
import org.jacpfx.vertx.services.ServiceVerticle;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by Andy Moncsek on 12.05.15.
 */
@ApplicationPath("/articleService")
public class ArticleService extends ServiceVerticle {


    // http://.../findAll
    // http:// .../find/{magazine}
    // http://.../findAllWithComments
    // http://.../comments/{articleId}


    @Path("/find")
    @OperationType(Type.WEBSOCKET)
    @Consumes("application/json")
    @Produces("application/json")
    public void find(String filter,MessageReply reply) {


    }
}
