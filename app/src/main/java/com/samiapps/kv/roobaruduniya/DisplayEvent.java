package com.samiapps.kv.roobaruduniya;

import java.util.ArrayList;

/**
 * Created by KV on 11/8/17.
 */

public class DisplayEvent {

    private String heading;
    private ArrayList<HomeDisplay> mDescriptions;

    public DisplayEvent() {

    }

    public DisplayEvent(String name, ArrayList<HomeDisplay> mDescriptions) {
        this.heading = name;
        this.mDescriptions = mDescriptions;

    }

    public String getHeader() {
        return heading;
    }

    public void setHeader(String name) {
        this.heading = name;
    }

    public ArrayList<HomeDisplay> getAllItemsInSection() {
        return mDescriptions;
    }

    public void setAllItemsInSection(ArrayList<HomeDisplay> allItemsInSection) {
        this.mDescriptions = allItemsInSection;
    }

}
