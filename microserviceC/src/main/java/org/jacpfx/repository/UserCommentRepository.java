package org.jacpfx.repository;

import org.jacpfx.configuration.MongoRepositoryConfiguration;
import org.jacpfx.entities.Comment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;

/**
 * Created by Andy Moncsek on 20.05.15.
 */
@Repository
@Import({MongoRepositoryConfiguration.class})
@Qualifier("UserCommentRepository")
public class UserCommentRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    private final Class<Comment> entityClass = Comment.class;


    public void bulkCreateComments(List<Comment> comments) {
        mongoTemplate.insert(comments, entityClass);
    }

    public void bulkDeleteComments(List<Comment> comments){
        comments.forEach(a->mongoTemplate.remove(a));
    }

    public Collection<Comment> getAllComments() {
        try {
            return mongoTemplate.findAll(entityClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Comment> findCommentsByArticleId(String id) {
        return mongoTemplate.find(new Query(Criteria.where("articleId").regex(id)), Comment.class);
    }
}
