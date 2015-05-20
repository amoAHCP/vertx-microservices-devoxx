package org.jacpfx.repository;

import org.jacpfx.configuration.MongoRepositoryConfiguration;
import org.jacpfx.entities.Article;
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
 * Created by Andy Moncsek on 19.05.15.
 */
@Repository
@Import({MongoRepositoryConfiguration.class})
@Qualifier("ArticleRepository")
public class ArticleRepository {

    @Inject
    private MongoTemplate mongoTemplate;

    private final Class<Article> entityClass = Article.class;


    public void bulkCreateArticles(List<Article> articles) {
        mongoTemplate.insert(articles, entityClass);
    }

    public void bulkDeleteArticles(List<Article> articles){
        articles.forEach(a->mongoTemplate.remove(a));
    }

    public Collection<Article> getAllArticles() {
        try {
            return mongoTemplate.findAll(entityClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Article> findArticlesByBlog(String blog) {
        return mongoTemplate.find(new Query(Criteria.where("blog").regex(blog)), Article.class);
    }
}
