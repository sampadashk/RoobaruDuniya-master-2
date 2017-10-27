package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 11/8/17.
 */

public class HomeDisplay {
    String title;
    String photo;
    long timeval;

    public long getTimeval() {
        return timeval;
    }

    public void setTimeval(long timeval) {
        this.timeval = timeval;
    }

    public HomeDisplay() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public HomeDisplay(String title, String photo,long timeval) {
        this.title = title;
        this.photo = photo;
        this.timeval=timeval;
    }
}
