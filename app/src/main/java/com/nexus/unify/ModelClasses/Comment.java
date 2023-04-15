package com.nexus.unify.ModelClasses;

import java.io.Serializable;

public class Comment implements Serializable {
    private String comment;
    private String publisher;
    private  String type;




    public Comment(String comment, String publisher, String type) {
        this.comment = comment;
        this.publisher = publisher;
        this.type=type;

    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
