package org.jacpfx.spring;

import org.fluttercode.datafactory.impl.DataFactory;
import org.jacpfx.configuration.MongoDataSourceConfiguration;
import org.jacpfx.entities.Article;
import org.jacpfx.repository.ArticleRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Andy Moncsek on 19.05.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes={MongoDataSourceConfiguration.class,ArticleRepository.class})
public class CreateArticlesTest {

    @Inject
    MongoDbFactory mongoDbFactory;

    @Inject
    ArticleRepository repo;

    @Test
    public void testMongoDbFactoryConnection() {
        assertTrue(mongoDbFactory.getDb().getMongo().getConnector().isOpen());
    }
    @Test
    @Ignore
    public void createBulkData() {
        DataFactory df = new DataFactory();
        List<Article> all = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {

            all.add(new Article(null,df.getName(),df.getEmailAddress(),df.getCity(),df.getRandomText(200),df.getLastName()));
        }
        repo.bulkCreateArticles(all);

    }
    @Test
    @Ignore
    public void deleteBulkData() {
        Collection<Article> all = repo.getAllArticles();
        assertNotNull(all);
        repo.bulkDeleteArticles(new ArrayList<>(all));

    }

    @Test
    //@Ignore
    public void getAllDataTest() {
        Collection<Article> all = repo.getAllArticles();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertTrue(all.size() >= 1000);
        all.forEach(article-> System.out.println(article.getId()+","));
    }

    @Test
    //@Ignore
    public void getAllDataLimitTest() {
        Collection<Article> all = repo.getAllArticles(100);
        assertNotNull(all);
        assertFalse(all.isEmpty());
        assertTrue(all.size() == 100);
        all.forEach(article-> System.out.println(article.getId()+","));
    }
}
