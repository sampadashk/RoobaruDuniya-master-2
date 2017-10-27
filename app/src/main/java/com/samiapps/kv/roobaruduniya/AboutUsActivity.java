package com.samiapps.kv.roobaruduniya;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by KV on 20/6/17.
 */

public class AboutUsActivity extends AppCompatActivity {
    FirebaseStorage fs;
    ImageView ivw;
    TextView textView;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);
       setTitle(R.string.nav_about_us);
        fs=FirebaseStorage.getInstance();
        StorageReference sr=fs.getReference("about_us").child("ankita.jpg");


        textView=(TextView)findViewById(R.id.about_us);
        textView.setText(R.string.aboutus);
        ivw=(ImageView)findViewById(R.id.about_photo);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(sr)
                .into(ivw);

    }


}
