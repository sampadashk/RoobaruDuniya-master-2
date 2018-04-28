package com.samiapps.kv.roobaruduniya;

import java.util.ArrayList;

/**
 * Created by KV on 22/12/17.
 */

public class SectionAudio {
    private String headerTitle;
    private ArrayList<AudioStories> allStories;

    public SectionAudio(String headerTitle, ArrayList<AudioStories> allStories) {
        this.headerTitle = headerTitle;
        this.allStories = allStories;
    }

    public SectionAudio() {
    }

    public String getHeaderTitle() {
        return headerTitle;

    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<AudioStories> getAllStories() {
        return allStories;
    }

    public void setAllStories(ArrayList<AudioStories> allStories) {
        this.allStories = allStories;
    }
}
