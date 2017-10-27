package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 19/6/17.
 */

public class User {
    String name;
    String email;
    String articleStatus;
    String status;
    String uPhoto;

    //published
    //  String drafted;
    // String sent;
    public User() {

    }

    public String getArticleStatus() {
        return articleStatus;
    }

    public String getuPhoto() {
        return uPhoto;
    }

    public void setuPhoto(String uPhoto) {
        this.uPhoto = uPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User(String name, String email, String articleStatus, String status, String uPhoto) {
        this.name = name;
        this.email = email;
        this.articleStatus = articleStatus;
        this.status = status;
        this.uPhoto = uPhoto;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setArticleStatus(String articleStatus) {
        this.articleStatus = articleStatus;
    }


}
