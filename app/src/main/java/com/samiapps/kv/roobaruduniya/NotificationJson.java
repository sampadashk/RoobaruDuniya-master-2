package com.samiapps.kv.roobaruduniya;

import java.io.Serializable;

/**
 * Created by KV on 23/7/17.
 */

public class NotificationJson implements Serializable {
    String msg_id;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String user_id;

    public NotificationJson(String msg_id, String user_id) {
        this.msg_id = msg_id;
        this.user_id = user_id;
    }
}
