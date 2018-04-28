package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 26/12/17.
 */

public class AudioStories {
    String key;
    AudioStory au;

    public AudioStories(String key, AudioStory au) {
        this.key = key;
        this.au = au;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public AudioStory getAu() {
        return au;
    }

    public void setAu(AudioStory au) {
        this.au = au;
    }
}
