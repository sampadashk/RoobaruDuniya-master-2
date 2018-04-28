package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ActionProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by KV on 6/1/18.
 */

public class DisplayPhoto extends AppCompatActivity {
    ImageView photo;
    TextView abtPhoto,datephoto,num_of_like;
    TextView photographername,storyPhoto;
    Button sendComment;
    private ValueEventListener commentListener;
    RecyclerView commentlistrecycler;
    FloatingActionButton likebut;
    private DatabaseReference photoPublishedref,photoDb;
    
    EditText writeComment;
    String key;
    private boolean isFav;
    int nLikes;
    FirebaseAuth mAuth;
    Photo p;
    private ValueEventListener likeListener;
    private ArrayList<Comment> commentList;
    private CommentAdapter commentAdapter;
    private ActionProvider mShareActionProvider;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_photo);
        photo=(ImageView)findViewById(R.id.displ_image);
        abtPhoto=(TextView)findViewById(R.id.abt_photo);

        num_of_like=(TextView) findViewById(R.id.numlikes);
        mAuth = FirebaseAuth.getInstance();
        commentlistrecycler=(RecyclerView) findViewById(R.id.l_comment);
        commentList=new ArrayList<>();
        photoPublishedref = FirebaseDatabase.getInstance().getReference("photoPublishedDb");
        photoDb=FirebaseDatabase.getInstance().getReference("photoDb");
        likebut=(FloatingActionButton) findViewById(R.id.like_photo);
        storyPhoto=(TextView) findViewById(R.id.story_photo);
        datephoto=(TextView) findViewById(R.id.date_display);
        photographername=(TextView) findViewById(R.id.photographer);
        sendComment=(Button)findViewById(R.id.s_comment);
        writeComment=(EditText) findViewById(R.id.write_comment);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        //TODO photo upload


        Intent intent=getIntent();
         p=(Photo)intent.getSerializableExtra("sendPhoto");
         key=intent.getStringExtra("keysend");
        if(key==null)
        {
            JSONObject obj = null;
            try {
                obj = new JSONObject(intent.getStringExtra("bkgnotification"));
                key = obj.get("msgid").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
         if(p==null&&key!=null)
         {
             getPhotoObject();
         }

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(DisplayPhoto.this, commentList);
        commentlistrecycler.setLayoutManager(layoutManager);
        commentlistrecycler.setHasFixedSize(false);

        commentlistrecycler.setAdapter(commentAdapter);
        loadComment();

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = writeComment.getText().toString();
                String cName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                Uri userPhoto = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


                long date = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String dateString = sdf.format(date);
                try {
                    if (userPhoto == null) {

                        String add = "firebasestorage.googleapis.com/v0/b/roobaru-duniya-86f7d.appspot.com/o/defaultpp-profilepic%2Fdefaultprof.jpg?alt=media&token=aeca7a55-05e4-4c02-938f-061624f5c8b4";
                        userPhoto = Uri.parse("https://" + add);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


                Comment c = new Comment(cName, comment, dateString, userPhoto.toString(), userId);
                photoPublishedref.child(key).child("comments").push().setValue(c);
                //commentList.add(c);
               // commentReference.child(key).child("comments").push().setValue(c);


                sendComment.setEnabled(false);
                writeComment.clearFocus();
                writeComment.setText("");


            }
        });




    }

    private void getPhotoObject() {
        //Log.d("photokey",key);
        try {
            photoDb.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        p = dataSnapshot.getValue(Photo.class);
                      //  Log.d("checkpp", p.getTitle());
                        abtPhoto.setText("Photo Title: " + p.getTitle());
                        long dt = p.getTimeval();
                        datephoto.setText(dt + "");
                        Glide.with(DisplayPhoto.this).load(p.getPhoto()).into(photo);
                        photographername.setText(p.getUser());
                        storyPhoto.setText("About Photo: " + p.getAboutPhoto());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onStart()
    {
        super.onStart();
       /* Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        if(data!=null)
        Log.d("geti",data.toString());
        */
        if(p!=null) {
            abtPhoto.setText("Photo Title: " + p.getTitle());
            long dt = p.getTimeval();
            datephoto.setText(dt + "");
            Glide.with(this).load(p.getPhoto()).into(photo);
            photographername.setText(p.getUser());
            storyPhoto.setText("About Photo: " + p.getAboutPhoto());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
//TODO sharing in FB
      //  Log.d("menucalled","called");
   getMenuInflater().inflate(R.menu.menu_sharephoto, menu);

        menu.findItem(R.id.menu_photo_share).setVisible(true);
        MenuItem item = menu.findItem(R.id.menu_photo_share);
     item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem menuItem) {
             Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
             String msg = "\"" + p.getTitle() + "\"" + " - रूबरू दुनिया में देखिए " + p.getUser() + " की  नज़र से एक अनोखी दास्तान सुनाती तस्वीर। \n" + generateDynamicLinks(key);
             sharingIntent.setType("text/plain");
             String shareBody = "here goes your share content body";
             sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, p.getTitle());
             sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
             startActivity(sharingIntent);
             return true;
         }
     });
        //create the sharing intent




        //then set the sharingIntent

        return true;


        //MenuItem item = menu.findItem(R.id.menu_item_share);

        //create the sharing intent




        //then set the sharingIntent


    }
    /*

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.d("checkt",item.getTitle().toString());
        switch(item.getItemId()){
            case R.id.menu_photo_share:
                Log.d("checkf","pressed");
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "here goes your share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, p.getTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, p.getPhoto());
                startActivity(sharingIntent);
                return true;
                //DO SOMETHING

            defaultpp:
                return super.onOptionsItemSelected(item);
        }







    }
    */

    private static String generateDynamicLinks(String key) {
      // return "https://a6qgq.app.goo.gl/photo/"+key;

        //return "https://a6qgq.app.goo.gl/?link=https://roobaruduniya.com/photo&apn=com.samiapps.kv.roobaruduniya";
        return "https://a6qgq.app.goo.gl/?link=https://roobaruduniya.com/photo" + key + "/&apn=com.samiapps.kv.roobaruduniya";

    }

    public void onResume()
    {
        super.onResume();
        if (mAuth.getCurrentUser() != null) {

            FavDb favRef = new FavDb(DisplayPhoto.this);
            SQLiteDatabase sqldb = favRef.getReadableDatabase();
            if(key!=null) {


                Cursor cursor = favRef.queryKey(sqldb, key, "favphoto");

                if (cursor.getCount() > 0) {


                    likebut.setImageResource(R.drawable.ic_favorite);
                    isFav = true;
                }


                likeListener = photoPublishedref.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String date = dataSnapshot.child("dateCreated").getValue().toString();
                            datephoto.setText(date);
                            //  Log.d("ckdate", date);
                            if (dataSnapshot.child("likes").exists()) {
                                String n_Likes = dataSnapshot.child("likes").getValue().toString();
                                nLikes = Integer.parseInt(n_Likes);


                                num_of_like.setText(nLikes + " like");
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            writeComment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().trim().length() > 0) {
                        sendComment.setEnabled(true);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });





            likebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FavDb favdbRef = new FavDb(DisplayPhoto.this);
                    SQLiteDatabase db = favdbRef.getWritableDatabase();
                 /*   publishedRef.child(key).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String value = dataSnapshot.getValue().toString();
                            nLikes = Integer.parseInt(value);


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    */
                    if (isFav) {
                        favdbRef.deleteKey(db, key, "favphoto");
                        likebut.setImageResource(R.drawable.ic_favorite_border);
                        isFav = false;
                        if (nLikes > 0) {
                            nLikes -= 1;
                        }

                    } else {
                        favdbRef.insertKey(db, key, "favphoto");
                        likebut.setImageResource(R.drawable.ic_favorite);

                        isFav = true;
                        nLikes += 1;
                    }
                    photoPublishedref.child(key).child("likes").setValue(nLikes);

                }
            });


        }
    }
    public void onDestroy()
    {
        super.onDestroy();
        if (likeListener != null) {
            photoPublishedref.child(key).removeEventListener(likeListener);
            likeListener = null;
        }


        if (commentListener != null) {
            photoPublishedref.child(key).child("comments")
                    .removeEventListener(commentListener);
            commentListener = null;
            commentList.clear();
        }
        finish();
    }
    public void loadComment() {
        try {


            if (commentListener == null) {
                commentListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

//clear the arraylist before display so that duplicate result is not printed
                        // Log.d("commentcheck", "check");
                        commentList.clear();

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                //    Log.d("commentkey", keySel);


                                Comment c = ds.getValue(Comment.class);
                                commentList.add(c);
                                commentAdapter.notifyDataSetChanged();
                                try {
                                    // Log.d("checkcmt", c.comment);
                                    // Log.d("checkcname", c.commentorName);

                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                            }


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                // commentReference.child(keySel).child("comments").addValueEventListener(commentListener);
                photoPublishedref.child(key).child("comments").addValueEventListener(commentListener);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
