package org.jacpfx;

import de.svenjacobs.loremipsum.LoremIpsum;
import org.fluttercode.datafactory.impl.DataFactory;
import org.jacpfx.configuration.MongoDataSourceConfiguration;
import org.jacpfx.entities.Comment;
import org.jacpfx.repository.UserCommentRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Andy Moncsek on 20.05.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class, classes={MongoDataSourceConfiguration.class,UserCommentRepository.class})
public class CreateCommentsDataTest {
    @Inject
    MongoDbFactory mongoDbFactory;

    @Inject
    UserCommentRepository repo;

    @Test
    public void testMongoDbFactoryConnection() {
        assertTrue(mongoDbFactory.getDb().getMongo().getConnector().isOpen());
    }

    @Test
    public void testReadCSV() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("articleId.csv");
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter(",");
        while(s.hasNext()) {
            System.out.println(s.next().trim());
        }

    }
    @Test
    @Ignore
    public void testBulkCreate() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("articleId.csv");
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter(",");
        DataFactory df = new DataFactory();
        LoremIpsum loremIpsum = new LoremIpsum();

        while(s.hasNext()) {
           String articleId = s.next().trim();
            Random randomGenerator = new Random();
           Integer amount = randomGenerator.nextInt(50);
            List<Comment> comments = new ArrayList<>();
            for(int i=0; i<amount;i++) {
                comments.add(new Comment(null,articleId,df.getName(),loremIpsum.getParagraphs(randomGenerator.nextInt(5))));
            }
            repo.bulkCreateComments(comments);
        }
    }
    @Test
    public void testGetAll() {
        final Collection<Comment> allComments = repo.getAllComments();
        assertNotNull(allComments);
        assertFalse(allComments.isEmpty());
        //allComments.forEach(c -> System.out.println(c));
        System.out.println("size: "+allComments.size());
    }

    @Test
    public void testReadByArticleId() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("articleId.csv");
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter(",");
        DataFactory df = new DataFactory();
        LoremIpsum loremIpsum = new LoremIpsum();
         int i=0;
        while(s.hasNext()) {
            if(i==10) break;
            String articleId = s.next().trim();
            final List<Comment> commentsByArticleId = repo.findCommentsByArticleId(articleId);
            assertNotNull(commentsByArticleId);
            assertFalse(commentsByArticleId.isEmpty());
            commentsByArticleId.forEach(c-> System.out.println(c));
            i++;
        }
    }
}
