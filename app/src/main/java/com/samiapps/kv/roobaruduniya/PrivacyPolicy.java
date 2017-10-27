package com.samiapps.kv.roobaruduniya;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by KV on 20/6/17.
 */

public class PrivacyPolicy extends AppCompatActivity {
    private TextView tv;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);
        setTitle(R.string.privacy_policy);
        tv=(TextView)findViewById(R.id.privacy);
        tv.setText(R.string.privacy);
    }
}
