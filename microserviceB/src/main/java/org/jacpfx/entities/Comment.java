package org.jacpfx.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

/**
 * Created by Andy Moncsek on 19.05.15.
 */
public class Comment implements Serializable {
    private @Id
    String id;

    private @Indexed String articleId;

    private @Indexed String user;

    private String comment;

    public Comment(String id, String articleId, String user, String comment) {
        this.id = id;
        this.articleId = articleId;
        this.user = user;
        this.comment = comment;
    }

    public Comment() {

    }

    public String getId() {
        return id;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }


    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", articleId='" + articleId + '\'' +
                ", user='" + user + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
