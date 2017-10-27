package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by KV on 15/8/17.
 */

public class Profile extends AppCompatActivity {

    TextView emailTextView;
    TextView uStatusTextView;
    TextView uNameTextView;
    ImageView userPhotoView;
    TextView noPublishedText;
    String uemai;
    String uname;
    String uid;
    RecyclerView rvc;
    private ArrayList<RoobaruDuniya> rubaru = new ArrayList<RoobaruDuniya>();
    private ArrayList<String> keyList;
    ListPublishedAdapter listPublishedAdapter;


    String status;
    private FirebaseDatabase firebasedb;
    private DatabaseReference dbUser;
    private DatabaseReference msgReference;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);


        emailTextView = (TextView) findViewById(R.id.profileEmail);
        uStatusTextView = (TextView) findViewById(R.id.user_status);
        uNameTextView = (TextView) findViewById(R.id.name);
        userPhotoView = (ImageView) findViewById(R.id.img_profile);
        firebasedb = FirebaseDatabase.getInstance();
        dbUser = firebasedb.getReference("user");
        msgReference = firebasedb.getReference("messages");
        noPublishedText = (TextView) findViewById(R.id.no_published);
        rvc = (RecyclerView) findViewById(R.id.publishedProfileRecycle);
        listPublishedAdapter = new ListPublishedAdapter(rubaru, this);
        //   pgbar=(ProgressBar) rootView.findViewById(R.id.pbar);
        //  noDraftText=(TextView) rootView.findViewById(R.id.nodraftText);
        rvc.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvc.setItemAnimator(new DefaultItemAnimator());
        SeparatorDecoration decoration = new SeparatorDecoration(this, Color.GRAY, 1.5f);
        rvc.addItemDecoration(decoration);
        listPublishedAdapter.setOnItemClickListener(new ListPublishedAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                RoobaruDuniya item = rubaru.get(position);
                Intent intent = new Intent(Profile.this, ArticleDetail.class);

                String key = keyList.get(position);
                intent.putExtra("keySelected", key);
                intent.putExtra("position", position);
                Bundle b = new Bundle();
                b.putSerializable(ArticleDetail.TAG, item);
                intent.putExtras(b);


                //intent.putExtra("article",rd);

                startActivity(intent);

            }
        });
        // Log.d("actchk","2");
        keyList = new ArrayList<>();

        rvc.setAdapter(listPublishedAdapter);
        Intent intent = getIntent();
        uid = intent.getStringExtra("senuid");
        // String uPhoto = intent.getStringExtra("senphoto");
        // Glide.with(this).load(uPhoto).into(userPhotoView);
       // Log.d("guid", uid);

        dbUser.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    uname = dataSnapshot.child("name").getValue(String.class);
                    status = dataSnapshot.child("status").getValue(String.class);
                    uemai = dataSnapshot.child("email").getValue(String.class);
                    String uPhoto = dataSnapshot.child("uPhoto").getValue(String.class);
                    Glide.with(Profile.this).load(uPhoto).into(userPhotoView);
                    uNameTextView.setText(uname);
                    uStatusTextView.setText(status);
                    if (uemai != null) {


                        emailTextView.setText(uemai);
                    }

                } catch (NullPointerException ee) {
                    ee.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        readmsgId();


    }

    private void readmsgId() {


        // Log.d("actchk","3");

        dbUser.child(uid).child("articleStatus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    //  Log.d("actchk", "dbchk");
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //  Log.d("valck",ds.getValue()+"");
                        if (ds.getValue().equals("published")) {


                            //msgList.add(ds.getKey());
                            //  Log.d("keyck", ds.getKey());
                            checkMessages(ds.getKey());


                        }


                    }


                } else {
                    rvc.setVisibility(View.INVISIBLE);
                    noPublishedText.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void checkMessages(final String key) {
        // Log.d("actchk","4");


        msgReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RoobaruDuniya rbd = dataSnapshot.getValue(RoobaruDuniya.class);
                if (rbd.getUser().equals(uname)) {

                    //  Log.d("titleck", rbd.getTitle());
                    rubaru.add(rbd);
                    keyList.add(key);
                    listPublishedAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}