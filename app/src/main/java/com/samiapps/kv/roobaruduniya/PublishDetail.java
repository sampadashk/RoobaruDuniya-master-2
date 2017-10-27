package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by KV on 17/7/17.
 */

public class PublishDetail extends AppCompatActivity {
    int pos;
    public static final String TAG = PublishDetail.class.getName();
    RoobaruDuniya artpub;

    CollapsingToolbarLayout collapToolbar;

    String uName;
    String uProf;
    ImageView iview;
    TextView ttitle;
    TextView tcontent;
    TextView txtName, txtStatus;
    ImageView iprofile;


    String date;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FloatingActionButton favButton;
    DatabaseReference publishedRef;
    Uri uPhoto;
    String keySel;

    FirebaseDatabase dbase;


    TextView datetvw;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.publish_layout);
//TODO : DISPLAY EMPTY WHEN NO PUBLISHED


        collapToolbar = (CollapsingToolbarLayout) findViewById(R.id.c_layout);

        collapToolbar.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapToolbar.setExpandedTitleTextAppearance(R.style.expandedappbar);
        iprofile = (ImageView) findViewById(R.id.i_profile);


        dbase = FirebaseDatabase.getInstance();
        publishedRef = dbase.getReference("published");

        // FirebaseAuth.getInstance().
        Intent intent = getIntent();
        pos = intent.getIntExtra("position", -1);
        // Log.d("checkpos",""+pos);
        artpub = (RoobaruDuniya) intent.getSerializableExtra(PublishDetail.TAG);
        keySel = intent.getStringExtra("keySelected");
        // Log.d("keysel",keySel);


        uName = artpub.getUser();

        // Log.d("chkname",userName);
        uProf = artpub.getUserProfilePhoto();
        publishedRef.child(keySel).child("dateCreated").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    date = dataSnapshot.getValue().toString();
                    //  Log.d("ckdate", date);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        txtName = (TextView) findViewById(R.id.name_text);
        txtStatus = (TextView) findViewById(R.id.u_status);


        iview = (ImageView) findViewById(R.id.d_image);
        ttitle = (TextView) findViewById(R.id.p_title);
        tcontent = (TextView) findViewById(R.id.p_content);


        Glide.with(this).load(artpub.getPhoto()).into(iview);
        ttitle.setText(artpub.getTitle());
        tcontent.setText(artpub.getContent());
        txtName.setText(uName);

        Uri uri = Uri.parse(uProf);
        // Loading profile image
        Glide.with(this).load(uri)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iprofile);


        // Log.d("chkobj",""+artpub);


    }

    @Override
    public void onStart() {
        super.onStart();


    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//TODO sharing in FB
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        //create the sharing intent

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "here goes your share content body";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, artpub.getTitle());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, artpub.getContent());


        //then set the sharingIntent
        mShareActionProvider.setShareIntent(sharingIntent);
        return true;
    }
    */
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
