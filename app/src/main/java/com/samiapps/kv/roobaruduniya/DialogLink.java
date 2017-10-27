package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by KV on 5/8/17.
 */

public class DialogLink extends AppCompatActivity {
    EditText urlText;
    EditText urlLink;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_link);
        urlText = (EditText) findViewById(R.id.url_text);
        urlLink = (EditText) findViewById(R.id.url_edit);
        Intent i = getIntent();


    }

}
