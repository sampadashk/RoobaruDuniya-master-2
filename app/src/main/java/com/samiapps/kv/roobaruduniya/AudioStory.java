package com.samiapps.kv.roobaruduniya;

import java.io.Serializable;

/**
 * Created by KV on 3/12/17.
 */

public class AudioStory implements Serializable {
    String writer;
    String teller;
    String title;

    public AudioStory()
    {

    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTeller() {
        return teller;
    }

    public void setTeller(String teller) {
        this.teller = teller;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    String about;
    String photo;
    String audio;

    public AudioStory( String title,String writer, String teller, String about, String photo, String audio) {
        this.writer = writer;
        this.teller = teller;
        this.title = title;
        this.about = about;
        this.photo = photo;
        this.audio = audio;
    }
}
