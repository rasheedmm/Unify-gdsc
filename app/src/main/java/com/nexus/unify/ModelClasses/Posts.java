package com.nexus.unify.ModelClasses;


import java.io.Serializable;

public class Posts implements Serializable {

    private String postid;
    private String privacy;
    private String anms;
    private String url;
    private String text;
    private String type;
    private String publisher;
    private String price
            ;



    public Posts(String postid, String privacy, String anms, String url, String text,String type,String publisher,String price) {
        this.postid = postid;
        this.privacy = privacy;
        this.anms = anms;
        this.url = url;
        this.text = text;
        this.type = type;
        this.publisher = publisher;
        this.price = price;
    }
    public Posts(){

    }


    public String getPostid() {
        return postid;
    }

    public void setUid(String postid) {
        this.postid = postid;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getAnms() {
        return anms;
    }

    public void setAnms(String anms) {
        this.anms = anms;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher =publisher;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price =price;
    }




}
