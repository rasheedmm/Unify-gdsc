package com.nexus.unify.ModelClasses;

import java.io.Serializable;

public class Hashtags implements Serializable {

    private String id;
    private String tag;




    public Hashtags(String id, String tag) {
        this.id = id;
        this.tag = tag;

    }
    public Hashtags(){

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }




}
