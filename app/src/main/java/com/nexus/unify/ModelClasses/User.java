package com.nexus.unify.ModelClasses;

import java.io.Serializable;

public class User implements Serializable {

    private String uid;
    private String name;
    private String username;
    private String anmsname;
    private String email;
    private String profileImage;
    private String img1;
    private String img2;
    private String anmsimg;
    private String token;
    private String dep;
    private String bio;
    private String intrests;
    private String course;

    private long lastMsgTime;


    public User(String uid, String intrests, String name, String bio, String email, String profileImage,String course, String img1, String img2, String anmsname, String anmsimg, String username, String dep,String token) {
        this.uid = uid;
        this.name = name;
        this.email =email;
        this.profileImage = profileImage;
        this.course = course;
        this.img1=img1;
        this.img2=img2;
        this.anmsname=anmsname;
        this.anmsname=anmsname;
        this.username=username;
        this.dep=dep;
        this.intrests=intrests;
        this.bio=bio;
        this.token=token;


    }
    public User(){

    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmailr(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCourse(){
        return course;
    }

    public void setCoins(String course) {
        this.course =course;
    }
    public String getImg1(){
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 =img1;
    }

    public String getImg2(){
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 =img2;
    }


    public String getAnmsname(){
        return anmsname;
    }

    public void setAnmsname(String anmsname) {
        this.anmsname=anmsname;
    }

    public String getAnmsimg(){
        return anmsimg;
    }

    public void setAnmsimg(String anmsimg) {
        this.anmsimg =anmsimg;
    }


    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username =username;
    }

    public String getDep(){
        return dep;
    }

    public void setDep(String dep) {
        this.dep =dep;
    }

    public String getIntrests(){
        return intrests;
    }

    public void setIntrests(String intrests) {
        this.intrests =intrests;
    }
    public String getBio(){
        return bio;
    }

    public void setBio(String bio) {
        this.bio =bio;
    }

    public long getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(long lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }


}
