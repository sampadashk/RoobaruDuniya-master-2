package com.samiapps.kv.roobaruduniya;

import java.io.Serializable;

/**
 * Created by KV on 21/7/17.
 */

public class Notification implements Serializable {
    String message;
    String type;


    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Notification(String message) {
        this.message = message;


    }

    public Notification() {

    }

    public Notification(String type, String article, String byPerson) {
        this.type = type;
        this.article = article;
        this.byPerson = byPerson;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getByPerson() {
        return byPerson;
    }

    public void setByPerson(String byPerson) {
        this.byPerson = byPerson;
    }

    String article;
    String byPerson;

}
