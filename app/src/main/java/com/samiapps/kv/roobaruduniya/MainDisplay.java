package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 13/8/17.
 */

public class MainDisplay {
    int image;
    String text;

    public MainDisplay() {

    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MainDisplay(String text, int image) {
        this.image = image;

        this.text = text;
    }
}
