package com.samiapps.kv.roobaruduniya;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.BulletSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by KV on 23/6/17.
 */

public class ArticleDetail extends AppCompatActivity {
    int pos;
    public static final String TAG = ArticleDetail.class.getName();
    RoobaruDuniya artsel;
    RecyclerView cmtrecyclerV;
    ValueEventListener commentListener;
    ValueEventListener notificationListener;
    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout cdlayout;
    LinearLayout displayCont;
    Button editartticleButton;

    TextView num_Of_likes;
    ArrayList<TextFormat> textFormatList;
    String contentString;
    ImageButton homeButton;


    String userName;
    String userProf;
    ImageView ivw;
    TextView tvtitle;
    TextView tvcontent;
    TextView txtName, txtStatus;
    //SpannableStringBuilder str;
    ImageView imgProfile;
    ImageButton sharedButton;
    ImageButton bookmarkButton;
    String date;
    private FirebaseAuth mAuth;

    FloatingActionButton favButton;
    DatabaseReference publishedRef;
    DatabaseReference msgReference;
    DatabaseReference notificationRef;
    DatabaseReference styleRef;
    DatabaseReference userRef;
    Uri userPhoto;
    private String keySel;
    boolean isFav;
    boolean isBookMarked;
    FirebaseDatabase db;
    int nLikes;
    int cNo;
    EditText commentEditText;
    TextView datetvw;
    Button sendCmt;

    ValueEventListener likeListener;
    ValueEventListener msgListener;
    ValueEventListener delcmtListener;
    ArrayList<Comment> commentList;
    CommentAdapter commentAdapter;
    private ShareActionProvider mShareActionProvider;
    private String userId;
    private String wname;
    // ArrayList<RoobaruDuniya> rbd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.detail_layout);
        Log.d("detailcd", "called");


        commentEditText = (EditText) findViewById(R.id.w_comment);
        artsel = new RoobaruDuniya();
        sendCmt = (Button) findViewById(R.id.send_comment);
        cmtrecyclerV = (RecyclerView) findViewById(R.id.list_comment);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collaptool_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        editartticleButton=(Button) findViewById(R.id.edit_admin);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
        sharedButton = (ImageButton) findViewById(R.id.share);
        txtName = (TextView) findViewById(R.id.name);
        txtStatus = (TextView) findViewById(R.id.user_status);
        num_Of_likes = (TextView) findViewById(R.id.num_likes);
        bookmarkButton = (ImageButton) findViewById(R.id.bookmark);
        cdlayout = (CoordinatorLayout) findViewById(R.id.draw_insets_frame_layout);
        homeButton = (ImageButton) findViewById(R.id.home_but);
        displayCont = (LinearLayout) findViewById(R.id.display_content);

        imgProfile = (ImageView) findViewById(R.id.img_profile);
        ivw = (ImageView) findViewById(R.id.display_image);
        tvtitle = (TextView) findViewById(R.id.post_title);
        tvcontent = (TextView) findViewById(R.id.post_con);
        favButton = (FloatingActionButton) findViewById(R.id.share_fab);
        datetvw = (TextView) findViewById(R.id.published_date);
        textFormatList = new ArrayList<>();

      /*  //TrialTextSize
        final ArrayAdapter<String> adp = new ArrayAdapter<String>(ArticleDetail.this,
                android.R.layout.simple_spinner_item, optTexrSize);


        final Spinner sp = new Spinner(ArticleDetail.this);
        sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adp);

        AlertDialog.Builder builder = new AlertDialog.Builder(ArticleDetail.this);
        builder.setView(sp);
        builder.create().show();
        */


        mAuth = FirebaseAuth.getInstance();
        try {
            if (mAuth.getCurrentUser() == null) {

                Log.d("ckdetail", "here");
                Intent inti = new Intent(this, TrialActivity.class);
                inti.putExtra("firebasedl", "dynamiclink");
                startActivity(inti);


            }
        } catch (NullPointerException e) {
            Log.d("ckdetail", "here");
            Intent inti = new Intent(this, TrialActivity.class);
            inti.putExtra("firebasedl", "dynamiclink");
            startActivity(inti);

        }


//      getSupportActionBar().setDisplayShowTitleEnabled(false);
        // getSupportActionBar().setLogo(R.drawable.roobaru_logo);
        if (mAuth.getCurrentUser() != null) {

            userPhoto = mAuth.getCurrentUser().getPhotoUrl();
            userId = mAuth.getCurrentUser().getUid();
            String uemail=mAuth.getCurrentUser().getEmail();
            if(uemail.equals("shukla.sampada@gmail.com")||uemail.equals("ankita87jn@gmail.com"))
            {
                editartticleButton.setVisibility(View.VISIBLE);
            }


            commentList = new ArrayList<>();
            commentAdapter = new CommentAdapter(this, commentList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            cmtrecyclerV.setLayoutManager(layoutManager);
            cmtrecyclerV.setHasFixedSize(false);

            cmtrecyclerV.setAdapter(commentAdapter);

            db = FirebaseDatabase.getInstance();

            publishedRef = db.getReference("published");
            notificationRef = db.getReference("notification");
            styleRef = db.getReference("contentStyle");
            notificationRef.keepSynced(true);
            msgReference = db.getReference("messages");
            userRef = db.getReference("user");
            editartticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(ArticleDetail.this,EditorArticleActivity.class);
                    intent.putExtra("position",pos);
                    intent.putExtra("Keypos",keySel);
                    Bundle b = new Bundle();
                    b.putSerializable(SentFragment.TAG, artsel);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            // msgReference.keepSynced(true);
            commentAdapter.setOnItemClickListener(new CommentAdapter.ClickListener() {

                @Override
                public void onItemClick(final int position, View v) {


                    Comment c = commentList.get(position);
                    //  Log.d("posdelf",""+position);
                    //only give delete right to the person who wrote the comment;Than only delete button will be visisble
                    if (c.commentorName.equals(TrialActivity.mUsername)) {

                        final ImageButton deleteButton = (ImageButton) v.findViewById(R.id.delete_button);
                        if (deleteButton.getVisibility() == View.VISIBLE) {
                            deleteButton.setVisibility(View.INVISIBLE);
                        } else {
                            deleteButton.setVisibility(View.VISIBLE);
                            deleteButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Comment cmt = commentList.get(position);

                                    commentList.remove(cmt);  // remove the item from list
                                    commentAdapter.notifyItemRemoved(position);
                                    publishedRef.child(keySel).child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();

                                            int i = 0;
                                            // String[] sampleString = new String[length];
                                            //TODO DELETE
                                            //        Log.d("posdel", "" + position);
                                            while (i < position) {
                                                if (iterator.hasNext()) {
                                                    iterator.next();
                                                    i += 1;
                                                }
                                            }
                                            try {
                                                //get the key using iterator and than delete it

                                                String key = iterator.next().getKey();
                                                //           Log.d("keydel", key);
                                                dataSnapshot.child(key).getRef().removeValue();

                                                return;
                                            } catch (NoSuchElementException e) {
                                                e.printStackTrace();
                                            } catch (Exception ee) {
                                                ee.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                    Toast.makeText(ArticleDetail.this, R.string.delete_comment, Toast.LENGTH_SHORT).show();
                                    deleteButton.setVisibility(View.INVISIBLE);

                                }

                            });
                        }
                    }
                }
            });
            displayCont.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    showAlertTextSize();
                    return false;
                }
            });
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentHome = new Intent(ArticleDetail.this, TrialActivity.class);
                    startActivity(intentHome);
                }
            });


            // FirebaseAuth.getInstance().


            sharedButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // String msg = "पढ़िए नया लेख " + artsel.getTitle() + " by " + artsel.getUser() + " in Roobaru Duniya " + generateDynamicLinks(keySel);
                    String msg = "\"" + artsel.getTitle() + "\"" + " - रूबरू दुनिया में पढ़िए " + artsel.getUser() + " द्वारा लिखित नया लेख \n" + generateDynamicLinks(keySel);
                    Intent shareintent = new Intent(Intent.ACTION_SEND);
                    shareintent.setType("text/plain");

                                  /*  try{
                                        byte [] encodeByte= Base64.decode(artsel.getPhoto(),Base64.DEFAULT);
                                        bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                                    }catch(Exception e){
                                        e.getMessage();

                                    }
                                   // Bitmap image =artsel.getPhoto();
                                    SharePhoto photo = new SharePhoto.Builder()
                                            .setBitmap(bitmap)
                                            .build();
                                    SharePhotoContent content = new SharePhotoContent.Builder()
                                            .addPhoto(photo).setContentUrl(Uri.parse(generateDynamicLinks(keySel)))
                                            .build();
                                    ShareDialog.show(ArticleDetail.this,content);
                                    */


                    shareintent.putExtra(Intent.EXTRA_SUBJECT, artsel.getTitle());
                    shareintent.putExtra(Intent.EXTRA_TEXT, msg);
                    startActivity(Intent.createChooser(shareintent, "Share using"));


                }
            });
        }
    }

    




    private void showAlertTextSize() {

        //following code will be in your activity.java file
        final CharSequence[] items = {getString(R.string.small),getString(R.string.medium),getString(R.string.large)};
        // arraylist to keep the selected items
        //  final ArrayList seletedItems=new ArrayList();
        final int[] selectedPosition = new int[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_font);
        builder.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedPosition[0] = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

            }
        })


                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //     Log.d("checksi","here");
                        switch (selectedPosition[0]) {
                            case 0:
                                setFontForContainer(displayCont, 14);
                                // tvcontent.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

                                break;
                            case 1:
                                setFontForContainer(displayCont, 18);
                                //  tvcontent.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                                break;
                            case 2:
                                setFontForContainer(displayCont, 22);
                                // tvcontent.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                                break;

                        }
                        // tvcontent.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);

                        //  Your code when user icked on OK
                        //  You can write the code  to save the selected item here

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  Your code when user clicked on Cancel

                    }
                });

        AlertDialog dialog = builder.create();//AlertDialog dialog; create like this outside onClick
        dialog.show();

    }

    private void setFontForContainer(ViewGroup contentLayout, int font) {
        for (int i = 0; i < contentLayout.getChildCount(); i++) {
            View view = contentLayout.getChildAt(i);
            if (view instanceof TextView)
                ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, font);
            else if (view instanceof ViewGroup)
                setFontForContainer((ViewGroup) view, font);
        }
    }


    @Override
    public void onStart() {
        super.onStart();



            //TODO ASYNC in msgreference
        if(mAuth.getCurrentUser()!=null) {
            Intent intent = getIntent();
            String action = intent.getAction();
            Uri data = intent.getData();
            if (data != null) {
                //   Log.d("uri is",data.toString());
                String path = data.getPath();
                path = path.substring(0, path.length() - 1);
                String idStr = path.substring(path.lastIndexOf('/') + 1);

                keySel = idStr;
                LoadUIFromkey(keySel);


            } else if (intent.getStringExtra("intentNotification") != null) {
                NotificationJson obj = (NotificationJson) intent.getSerializableExtra("NotificationObject");
                //  Log.d("intentchk", "notification");
                keySel = obj.getMsg_id();
                // Log.d("chkrecikey", keySel);
                // Log.d("chkurl", "https://roobaru-duniya-86f7d.firebaseio.com/messages/" + keySel);
                LoadUIFromkey(keySel);


//                Log.d("titleck", rbd.getTitle());

            } else if (intent.getStringExtra("bkgnotification") != null) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(intent.getStringExtra("bkgnotification"));
                    keySel = obj.get("msgid").toString();

                    //  Log.d("chkrecikey", keySel);

                    LoadUIFromkey(keySel);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (intent.getStringExtra("searchString") != null) {
                keySel = intent.getStringExtra("searchString");
                //  Log.d("kkk",keySel);

                if (keySel != null) {
                    LoadUIFromkey(keySel);
                }


            } else {
                pos = intent.getIntExtra("position", -1);
                // Log.d("checkpos", "" + pos);


                //artsel = (RoobaruDuniya) intent.getSerializableExtra(ArticleDetail.TAG);
                // Log.d("ckart", artsel.getTitle());
                keySel = intent.getStringExtra("keySelected");
                //  Log.d("ck",keySel);
                LoadUIFromkey(keySel);

             /*   msgListener = msgReference.child(keySel).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        artsel = dataSnapshot.getValue(RoobaruDuniya.class);
                        //updating UI when you have article
                        loadUI();
                        Log.d("artchktit", artsel.getTitle());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("dberror", databaseError.toString());
                    }
                });
                */


            }
        }
        }


        //Deleting comments;




    private void LoadUIFromkey(String keySel) {

       // Log.d("kratika", keySel);

        msgListener = msgReference.child(keySel).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    artsel = dataSnapshot.getValue(RoobaruDuniya.class);
                    //updating UI when you have article
                    loadUI();
                }
                else
                {
                    Toast.makeText(ArticleDetail.this,"Article doesnt exist anymore!",Toast.LENGTH_LONG).show();
                }
                //  Log.d("artchktit", artsel.getTitle());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //  Log.d("dberror", databaseError.toString());
            }
        });

    }

    public void loadUI() {
        if (artsel != null) {

            collapsingToolbarLayout.setTitle(artsel.getTitle());


            userName = artsel.getUser();

            // Log.d("chkname",userName);
            userProf = artsel.getUserProfilePhoto();
            Glide.with(this).load(artsel.getPhoto()).into(ivw);
            tvtitle.setText(artsel.getTitle());


            // if(styleRef.child(keySel)!=null) {

            final SpannableStringBuilder str = new SpannableStringBuilder(artsel.getContent());
            styleRef.child(keySel).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    contentString = artsel.getContent();
                    if (dataSnapshot.exists()) {
                        //  Log.d("ckval", "hasvalue");
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            //  Log.d("ckke", ds.getKey());

                            TextFormat tf = ds.getValue(TextFormat.class);

                            //   Log.d("chktd", tf.getStyle());
//
                            // SpannableStringBuilder str = new SpannableStringBuilder(contentString);


                            if (tf.getStyle().equals("bold")) {

                                str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), tf.getStart(), tf.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                //  Log.d("ckbold", "" + str);


                            }
                            if (tf.getStyle().equals("italic")) {

                                str.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), tf.getStart(), tf.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                //    Log.d("ckbold", "" + str);


                            }
                            if (tf.getStyle().equals("bullet")) {
                                str.setSpan(new BulletSpan(10), tf.getStart(), tf.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            //  tvcontent.setText(str);


                        }
                        tvcontent.setText(str);
                    } else {
                        tvcontent.setText(artsel.getContent());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }


            });

            // }
            txtName.setPaintFlags(txtName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


            txtName.setText(userName);
            String usid = artsel.getuserId();
            userRef.child(usid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    wname = dataSnapshot.getValue().toString();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uid = artsel.getuserId();

                    //  Log.d("checkname",wname);
                    // Log.d("checkuser",userName);
                    if (wname.equals(userName)) {

                        // String uphoto=artsel.getUserProfilePhoto();
                        Intent intent = new Intent(ArticleDetail.this, Profile.class);
                        intent.putExtra("senuid", uid);
                        // intent.putExtra("senphoto",uphoto);
                        startActivity(intent);
                    } else {
                        Snackbar.make(cdlayout, R.string.no_profile, Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
            Uri uri = Uri.parse(userProf);
            // Loading profile image
            Glide.with(this).load(uri)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
        }
        loadComment();


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
            publishedRef.child(keySel).child("comments").addValueEventListener(commentListener);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // Log.d("resume", "here");
        if (mAuth.getCurrentUser() != null) {

            FavDb favRef = new FavDb(ArticleDetail.this);
            SQLiteDatabase sqldb = favRef.getReadableDatabase();


            Cursor cursor = favRef.queryKey(sqldb, keySel, "favourite");
            Cursor cr = favRef.queryKey(sqldb, keySel, "booked");
            if (cursor.getCount() > 0) {


                favButton.setImageResource(R.drawable.ic_favorite);
                isFav = true;
            }

            if (cr.getCount() > 0) {

                bookmarkButton.setImageResource(R.drawable.ic_bookmark_white_24dp);
                isBookMarked = true;

            }
            likeListener = publishedRef.child(keySel).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        date = dataSnapshot.child("dateCreated").getValue().toString();
                        datetvw.setText(date);
                        //  Log.d("ckdate", date);
                        if (dataSnapshot.child("likes").exists()) {
                            String n_Likes = dataSnapshot.child("likes").getValue().toString();
                            nLikes = Integer.parseInt(n_Likes);


                            num_Of_likes.setText(n_Likes + "like");
                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            // Log.d("chkobj",""+artsel);




       /* publishedRef.child(keySel).child("comments").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot ds:dataSnapshot.getChildren()) {
                        Comment c = ds.getValue(Comment.class);
                        commentList.add(c);
                        Log.d("checkcname", c.commentorName);
                        Log.d("checkcmt", c.comment);
                        commentAdapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
            commentEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().trim().length() > 0) {
                        sendCmt.setEnabled(true);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            sendCmt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = commentEditText.getText().toString();
                    String cName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


                    long date = System.currentTimeMillis();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String dateString = sdf.format(date);
                    try {
                        if (userPhoto == null) {

                            String add = "firebasestorage.googleapis.com/v0/b/roobaru-duniya-86f7d.appspot.com/o/default-profilepic%2Fdefaultprof.jpg?alt=media&token=aeca7a55-05e4-4c02-938f-061624f5c8b4";
                            userPhoto = Uri.parse("https://" + add);
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                    Comment c = new Comment(cName, comment, dateString, userPhoto.toString(), userId);
                    //commentList.add(c);
                    publishedRef.child(keySel).child("comments").push().setValue(c);
                    HashMap<String, String> notificationData = new HashMap<String, String>();
                    notificationData.put("from", mAuth.getCurrentUser().getUid());
                    notificationData.put("type", "comment");
                    // checkValue();


                    notificationData.put("commentNo", Long.toString(System.currentTimeMillis()));
                    notificationRef.child(artsel.getuserId()).child(keySel).setValue(notificationData);

                    sendCmt.setEnabled(false);
                    commentEditText.clearFocus();
                    commentEditText.setText("");


                }
            });




           /* publishedRef.child(keySel).child("dateCreated").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        date = dataSnapshot.getValue().toString();
                        Log.d("ckdate", date);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */
            bookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FavDb favdbRef = new FavDb(ArticleDetail.this);
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
                    if (isBookMarked) {
                        favdbRef.deleteKey(db, keySel, "booked");
                        bookmarkButton.setImageResource(R.drawable.ic_bookmark_border_white_24dp);
                        isBookMarked = false;
                        Snackbar.make(cdlayout, R.string.bookmark_removed, Snackbar.LENGTH_SHORT).show();


                    } else {
                        favdbRef.insertKey(db, keySel, "booked");
                        bookmarkButton.setImageResource(R.drawable.ic_bookmark_white_24dp);
                        isBookMarked = true;
                        Snackbar.make(cdlayout, R.string.bookmark_saved, Snackbar.LENGTH_SHORT).show();
                    }

                }
            });


            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    FavDb favdbRef = new FavDb(ArticleDetail.this);
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
                        favButton.setImageResource(R.drawable.ic_favorite_border);
                        isFav = false;
                        if (nLikes > 0) {
                            nLikes -= 1;
                        }

                    } else {
                        favdbRef.insertKey(db, keySel, "favourite");
                        favButton.setImageResource(R.drawable.ic_favorite);
                        HashMap<String, String> notificationData = new HashMap<String, String>();
                        notificationData.put("from", mAuth.getCurrentUser().getUid());
                        notificationData.put("type", "like");
                        notificationRef.child(artsel.getuserId()).child(keySel).setValue(notificationData);
                        isFav = true;
                        nLikes += 1;
                    }
                    publishedRef.child(keySel).child("likes").setValue(nLikes);

                }
            });


        }
    }


    //  TrialActivity.shouldLoadHomeFragOnBackPress=false;
    // getFragmentManager().popBackStackImmediate();
    //  finish();
//        getSupportActionBar().setTitle(activityTitles[navItemIndex]);


    private void checkValue() {
        notificationListener = notificationRef.child(artsel.getuserId()).child(keySel).child("commentNo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String sres = dataSnapshot.getValue().toString();

                    cNo = Integer.parseInt(sres);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       /* if (notificationListener == null) {
            notificationListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        String sres = (String) dataSnapshot.getValue();

                        cNo = Integer.parseInt(sres);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };


            notificationRef.child(artsel.getuserId()).child(keySel).child("commentNo").addValueEventListener(notificationListener);
        }
        */

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
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, artsel.getTitle());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, artsel.getContent());


        //then set the sharingIntent
        mShareActionProvider.setShareIntent(sharingIntent);
        return true;
    }
    */

    public void onDestroy() {
        super.onDestroy();
       // Log.d("detaildestroy", "destory");
if(commentList!=null) {
    commentList.clear();
}

        if (commentListener != null) {
            publishedRef.child(keySel).child("comments")
                    .removeEventListener(commentListener);
            commentListener = null;
        }
        if (notificationListener != null) {
            notificationRef.child(artsel.getuserId()).child(keySel).removeEventListener(notificationListener);
            notificationRef = null;
        }
        if (likeListener != null) {
            publishedRef.child(keySel).removeEventListener(likeListener);
            likeListener = null;
        }
        if (msgListener != null) {
            msgReference.child(keySel).removeEventListener(msgListener);
            msgListener = null;
        }
        if (delcmtListener != null) {
            publishedRef.child(keySel).child("comments").removeEventListener(delcmtListener);
            delcmtListener = null;
        }


    }

    public static String generateDynamicLinks(String key) {
        return "https://a6qgq.app.goo.gl/?link=https://roobaruduniya.com/" + key + "/&apn=com.samiapps.kv.roobaruduniya";
    }


    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("where","I am called");
        setIntent(intent);


    }


}



