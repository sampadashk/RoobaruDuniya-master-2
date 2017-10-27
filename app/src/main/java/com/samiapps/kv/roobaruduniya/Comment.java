package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 13/7/17.
 */

public class Comment {
    String commentorName;
    String comment;
    String uid;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public Comment(String commentorName, String comment, String date, String uPhoto, String uid) {
        this.commentorName = commentorName;
        this.comment = comment;
        this.date = date;
        this.uPhoto = uPhoto;
        this.uid = uid;


    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getuPhoto() {
        return uPhoto;
    }

    public void setuPhoto(String uPhoto) {
        this.uPhoto = uPhoto;
    }

    String date;
    String uPhoto;

    public Comment() {

    }

    public String getCommentorName() {
        return commentorName;
    }

    public void setCommentorName(String commentorName) {
        this.commentorName = commentorName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public Comment(String commentorName, String comment) {
        this.commentorName = commentorName;
        this.comment = comment;

    }
}
