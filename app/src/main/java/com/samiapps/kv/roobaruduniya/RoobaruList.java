package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 8/1/18.
 */

public class RoobaruList {
    String key;
   RoobaruDuniya roobaru;

    public RoobaruList(String key, RoobaruDuniya roobaru) {
        this.key = key;
        this.roobaru = roobaru;
    }

    public RoobaruDuniya getRoobaru() {

        return roobaru;
    }

    public void setRoobaru(RoobaruDuniya roobaru) {
        this.roobaru = roobaru;
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


}
