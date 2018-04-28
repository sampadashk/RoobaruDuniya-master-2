package com.samiapps.kv.roobaruduniya;

import java.io.Serializable;

/**
 * Created by KV on 3/1/18.
 */

public class Photo implements Serializable {
    String user;
    String photo;
    String title;
    String aboutPhoto;
    long timeval;

    public long getTimeval() {
        return timeval;
    }

    public String getAboutPhoto() {
        return aboutPhoto;
    }

    public void setAboutPhoto(String aboutPhoto) {
        this.aboutPhoto = aboutPhoto;
    }

    public void setTimeval(long timeval) {
        this.timeval = timeval;
    }

    public Photo()
    {

    }
    public Photo(String user, String photo, String title,long timeval,String aboutPhoto) {
        this.user = user;
        this.photo = photo;
        this.title = title;
        this.timeval=timeval;
        this.aboutPhoto=aboutPhoto;
    }



    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
