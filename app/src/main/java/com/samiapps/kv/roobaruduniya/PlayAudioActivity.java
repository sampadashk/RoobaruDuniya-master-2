package com.samiapps.kv.roobaruduniya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by KV on 17/11/17.
 */

public class PlayAudioActivity extends AppCompatActivity {
    ImageButton b1,b2,b3,b4;
    ImageView iv;
    TextView tx1,tx2,tx3,tx4,tx5;
    Button sendComment;
    SeekBar seekbar;
    MediaPlayer player;
    EditText writeComment;
    String storyadd;
    DatabaseReference commentReference;
    private double startTime = 0;
    private double finalTime = 0;
    public static int oneTimeOnly = 0;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    RecyclerView rcvComment;
    int nLikes;
    boolean isFav;
    TextView nLike;
    AudioStory audioStory;
    int nLiken;
    FloatingActionButton likeButton;
    private FirebaseAuth mAuth;

    private Handler myHandler = new Handler();;
    private String keySel;
    private DatabaseReference audioPublishedref,audioDatabase;
    private TextView datetvw;
    private ValueEventListener likeListener;
    private TextView num_Of_likes;
    private Uri userPhoto;
    private String userId;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> commentList;
    private ValueEventListener commentListener;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_audio);
        b1 = (ImageButton) findViewById(R.id.button);
        b2 = (ImageButton) findViewById(R.id.button2);
        b3 = (ImageButton) findViewById(R.id.button3);
        b4 = (ImageButton) findViewById(R.id.button4);
        iv = (ImageView) findViewById(R.id.audio_img);
       // nLike = (TextView) findViewById(R.id.num_like);
        datetvw = (TextView) findViewById(R.id.date_txt);
        num_Of_likes = (TextView) findViewById(R.id.numlike);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {

            userPhoto = mAuth.getCurrentUser().getPhotoUrl();
            userId = mAuth.getCurrentUser().getUid();
            String uemail = mAuth.getCurrentUser().getEmail();


            likeButton = (FloatingActionButton) findViewById(R.id.fav_but);

            audioPublishedref = FirebaseDatabase.getInstance().getReference("audioPublished");

            tx1 = (TextView) findViewById(R.id.audio_title);
            tx2 = (TextView) findViewById(R.id.audio_sw);
            tx3 = (TextView) findViewById(R.id.audio_teller);
            tx4 = (TextView) findViewById(R.id.audio_abtstory);
            tx5 = (TextView) findViewById(R.id.audiolen);
            commentReference = FirebaseDatabase.getInstance().getReference("commentpublished");
            audioDatabase=FirebaseDatabase.getInstance().getReference("audiodb");
            rcvComment = (RecyclerView) findViewById(R.id.l_comment);
            seekbar = (SeekBar) findViewById(R.id.seekBar);
            seekbar.setClickable(false);
            sendComment = (Button) findViewById(R.id.s_comment);
            writeComment = (EditText) findViewById(R.id.write_comment);

            b2.setEnabled(false);

            SharedPreferences sp = this.getSharedPreferences("Status", Context.MODE_PRIVATE);
            String status = sp.getString("userStatus", "Blogger");
            Log.d("checks", status);

            //User u=RoobaruGlobaLProvider.getUser();
//        Log.d("checku",u.getStatus());
            Intent intent = getIntent();
            audioStory = (AudioStory) intent.getSerializableExtra("AudioObj");
            keySel = intent.getStringExtra("keysend");
            if(keySel==null)
            {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(intent.getStringExtra("bkgnotification"));
                    keySel = obj.get("msgid").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(audioStory==null)
            {
                getAudioStory();
            }
            else {


                 storyadd = audioStory.getAudio();


                try {
                    tx1.setText("Title: " + audioStory.getTitle());
                    tx2.setText("Writer: " + audioStory.getWriter());
                    tx3.setText("Teller: " + audioStory.getTeller());
                    if (audioStory.getAbout() != null)
                        tx4.setText("About: " + audioStory.getAbout());

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                if (audioStory.getPhoto() != null) {
                    Glide.with(this).load(audioStory.getPhoto()).into(iv);
                } else
                    iv.setVisibility(View.GONE);
            }

            commentList = new ArrayList<>();
            commentAdapter = new CommentAdapter(this, commentList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            rcvComment.setLayoutManager(layoutManager);
            rcvComment.setHasFixedSize(false);

            rcvComment.setAdapter(commentAdapter);
            loadComment();


            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                    player.start();

                    finalTime = player.getDuration();
                    startTime = player.getCurrentPosition();

                    if (oneTimeOnly == 0) {
                        seekbar.setMax((int) finalTime);
                        oneTimeOnly = 1;
                    }

                    tx5.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime)))
                    );


                    seekbar.setProgress((int) startTime);
                    myHandler.postDelayed(UpdateSongTime, 100);
                    b2.setEnabled(true);
                    b3.setEnabled(false);
                }
            });

            try {
                player = new MediaPlayer();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    player.setAudioAttributes(new AudioAttributes.Builder().build());
                    finalTime = player.getDuration();
                    startTime = player.getCurrentPosition();
                    tx5.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            finalTime))));
                }
                //  player.setDataSource("https://firebasestorage.googleapis.com/v0/b/roobaru-duniya-86f7d.appspot.com/o/audio%2Faudio%3A976?alt=media&token=ff8160e1-ca7a-4026-8ad5-a824221abea5");
                player.setDataSource(storyadd);
                player.prepare();

            } catch (Exception e) {
                // TODO: handle exception
            }
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Pausing sound", Toast.LENGTH_SHORT).show();
                    player.pause();
                    b2.setEnabled(false);
                    b3.setEnabled(true);
                }
            });
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = (int) startTime;

                    if ((temp + forwardTime) <= finalTime) {
                        startTime = startTime + forwardTime;
                        player.seekTo((int) startTime);
                        Toast.makeText(getApplicationContext(), "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            b4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = (int) startTime;

                    if ((temp - backwardTime) > 0) {
                        startTime = startTime - backwardTime;
                        player.seekTo((int) startTime);
                        Toast.makeText(getApplicationContext(), "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
                    //commentList.add(c);
                   // commentReference.child(keySel).child("comments").push().setValue(c);
                    audioPublishedref.child(keySel).child("comments").push().setValue(c);


                    sendComment.setEnabled(false);
                    writeComment.clearFocus();
                    writeComment.setText("");


                }
            });


        }
    }

    private void getAudioStory() {
        audioDatabase.child(keySel).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    audioStory=dataSnapshot.getValue(AudioStory.class);
                    Log.d("checkpp",audioStory.getTitle());
                     storyadd = audioStory.getAudio();
                     Log.d("checkstoryaudio",storyadd);


                    try {
                        tx1.setText("Title: " + audioStory.getTitle());
                        tx2.setText("Writer: " + audioStory.getWriter());
                        tx3.setText("Teller: " + audioStory.getTeller());
                        if (audioStory.getAbout() != null)
                            tx4.setText("About: " + audioStory.getAbout());

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    if (audioStory.getPhoto() != null) {
                        Glide.with(PlayAudioActivity.this).load(audioStory.getPhoto()).into(iv);
                    } else
                        iv.setVisibility(View.GONE);

                    try {
                        player.setDataSource(storyadd);
                        player.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = player.getCurrentPosition();

            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_shareaudio,menu);
        MenuItem item = menu.findItem(R.id.menu_audio_share);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                String msg = "\"" +audioStory.getTitle() + "\"" + " - रूबरू दुनिया में सुनिए " + audioStory.getTeller() + " की आवाज़ में \"जनहित में जारी\" हुई नई रचना \n" + generateDynamicLinks(keySel);
                sharingIntent.setType("text/plain");
                String shareBody = "here goes your share content body";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, audioStory.getTitle());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
                startActivity(sharingIntent);
                return true;
            }
        });
        //create the sharing intent




        //then set the sharingIntent

        return true;

    }

    public void onBackPressed()
    {
        super.onBackPressed();
        player.stop();
        player.seekTo(0);
        oneTimeOnly=0;

    }
    public void loadComment() {
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
            audioPublishedref.child(keySel).child("comments").addValueEventListener(commentListener);
        }

    }
    public void onResume()
    {
        super.onResume();
        if (mAuth.getCurrentUser() != null) {

            FavDb favRef = new FavDb(PlayAudioActivity.this);
            SQLiteDatabase sqldb = favRef.getReadableDatabase();


            Cursor cursor = favRef.queryKey(sqldb, keySel, "favaudio");

            if (cursor.getCount() > 0) {


                likeButton.setImageResource(R.drawable.ic_favorite);
                isFav = true;
            }


            likeListener = audioPublishedref.child(keySel).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                         String date = dataSnapshot.child("dateCreated").getValue().toString();
                        datetvw.setText(date);
                        //  Log.d("ckdate", date);
                        if (dataSnapshot.child("likes").exists()) {
                            String n_Likes = dataSnapshot.child("likes").getValue().toString();
                            nLikes = Integer.parseInt(n_Likes);


                            num_Of_likes.setText(n_Likes + " like");
                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


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





            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FavDb favdbRef = new FavDb(PlayAudioActivity.this);
                    SQLiteDatabase db = favdbRef.getWritableDatabase();
                 /*   publishedRef.child(keySel).child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
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
                        favdbRef.deleteKey(db, keySel, "favourite");
                        likeButton.setImageResource(R.drawable.ic_favorite_border);
                        isFav = false;
                        if (nLikes > 0) {
                            nLikes -= 1;
                        }

                    } else {
                        favdbRef.insertKey(db, keySel, "favourite");
                        likeButton.setImageResource(R.drawable.ic_favorite);

                        isFav = true;
                        nLikes += 1;
                    }
                    audioPublishedref.child(keySel).child("likes").setValue(nLikes);

                }
            });


        }
    }
    public void onDestroy() {
        super.onDestroy();
        // Log.d("detaildestroy", "destory");
        if(commentList!=null) {
            commentList.clear();
        }

        if (commentListener != null) {
            audioPublishedref.child(keySel).child("comments")
                    .removeEventListener(commentListener);
            commentListener = null;
        }

        if (likeListener != null) {
            audioPublishedref.child(keySel).removeEventListener(likeListener);
            likeListener = null;
        }




    }
    private static String generateDynamicLinks(String key) {
        // return "https://a6qgq.app.goo.gl/photo/"+key;

        //return "https://a6qgq.app.goo.gl/?link=https://roobaruduniya.com/photo&apn=com.samiapps.kv.roobaruduniya";
        return "https://a6qgq.app.goo.gl/?link=https://roobaruduniya.com/audio" + key + "/&apn=com.samiapps.kv.roobaruduniya";

    }


}
