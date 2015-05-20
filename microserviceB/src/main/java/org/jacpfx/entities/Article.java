package org.jacpfx.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Andy Moncsek on 19.05.15.
 */
@Document
public class Article implements Serializable{
    private @Id String id;

    private @Indexed String title;

    private @Indexed String topic;

    private @Indexed String blog;

    private @Indexed String author;

    private  String content;


    private @Transient
    List<Comment> comments;

    public Article(String id, String title, String topic, String blog, String content, String author, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.topic = topic;
        this.blog = blog;
        this.content = content;
        this.comments = comments;
        this.author = author;
    }

    public Article(String id, String title, String topic, String blog, String content, String author) {
        this(id,title,topic,blog,content,author,null);
    }
    public Article(){
        this(null,null,null,null,null,null,null);
    }


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTopic() {
        return topic;
    }

    public String getBlog() {
        return blog;
    }

    public String getContent() {
        return content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", topic='" + topic + '\'' +
                ", blog='" + blog + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", comments=" + comments +
                '}';
    }
}
