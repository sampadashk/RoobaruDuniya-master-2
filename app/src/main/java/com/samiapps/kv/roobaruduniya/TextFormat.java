package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 4/8/17.
 */

public class TextFormat {
    String style;
    int start;
    int end;

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public TextFormat(String style, int start, int end) {
        this.style = style;
        this.start = start;
        this.end = end;
    }

    TextFormat() {

    }
}
