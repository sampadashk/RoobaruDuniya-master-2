package com.samiapps.kv.roobaruduniya;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;


/**
 * Created by KV on 24/7/17.
 */

public class MApplication extends Application {




    public void onCreate() {

        super.onCreate();
       FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);


        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


}
