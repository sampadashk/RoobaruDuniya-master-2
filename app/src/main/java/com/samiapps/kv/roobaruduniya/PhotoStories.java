package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 6/1/18.
 */

public class PhotoStories {
    String key;
    Photo photo;

    public PhotoStories(String key, Photo photo) {
        this.key = key;
        this.photo = photo;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo= photo;
    }
}
