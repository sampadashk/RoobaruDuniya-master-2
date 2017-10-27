package com.samiapps.kv.roobaruduniya;

/**
 * Created by KV on 28/6/17.
 */

public class PendingClass {
    boolean checked;
    boolean approved;
    String editorName;

    PendingClass(boolean checked) {
        this.checked = checked;
    }

    PendingClass(boolean checked, boolean approved, String editorName) {
        this.checked = checked;
        this.approved = approved;
        this.editorName = editorName;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }


}
