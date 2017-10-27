package com.samiapps.kv.roobaruduniya;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.BulletSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by KV on 18/6/17.
 */

public class WriteArticleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int RC_PROFILE_PICKER = 4;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase db;
    DatabaseReference dbRefMsg;
    DatabaseReference contentStyleRef;
    ImageButton bulletButton;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    DatabaseReference dbtitlepublished;
    int it;
    DatabaseReference category;




    DatabaseReference dbRefUser;
    DatabaseReference dbEditor;
    DatabaseReference dbPendingArticle;
    DatabaseReference publishedRef;
    Button writerDetail;
    String wName;
    ArrayList<TextFormat> formatList;

    int draftPressed = 0;
    String userEmail;
    String uStatus;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    EditText title;
    EditText content;
    MenuItem draftButton;
    MenuItem saveButton;
    MenuItem publishButton;
    MenuItem deleteButton;
    ImageButton photoButton;
    RoobaruDuniya rbd;
    String userProfile;
    Uri downloadProfileUrl;
    private StorageReference defaultPhoto;

    LinearLayout llout;
    Button italicButton;
    String categoryChoosen;


    String userPos;
    String key;
    String userId;
    User u;
    Boolean b;
    int pos;

    RoobaruDuniya artsel;


    public static final String PREFS_NAME = "AOP_PREFS";
    public static final String PREFS_KEY = "AOP_PREFS_String";

    private static final int RC_PHOTO_PICKER = 2;
    private ValueEventListener eventListener;
    final HashMap<String, Object> userMap = new HashMap<String, Object>();
    private Button boldButton;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO Screen rotation
        //TODO PHOTO LOAD ASYNC
        setContentView(R.layout.write_article);
        SharedPreferences settings;
        boolean res;
        settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1

        res = settings.getBoolean(PREFS_KEY, false); //2
        if(res==false) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.manual)
                    .setNegativeButton("Don't show me again", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences settings;
                            SharedPreferences.Editor editor;
                            settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
                            editor = settings.edit(); //2

                            editor.putBoolean(PREFS_KEY, true); //3
                            editor.commit();

                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }



        llout = (LinearLayout) findViewById(R.id.linearlout);
        italicButton = (Button) findViewById(R.id.italic_button);
        boldButton = (Button) findViewById(R.id.bold_button);
        bulletButton = (ImageButton) findViewById(R.id.add_bullet);
        spinner = (Spinner) findViewById(R.id.spinner1);



        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userEmail = user.getEmail();
        formatList = new ArrayList<>();
        userId = user.getUid();
      uStatus = TrialActivity.userStatus;

       Log.d("TrialOnStatus", uStatus);
        firebaseStorage = FirebaseStorage.getInstance();
        defaultPhoto = firebaseStorage.getReference().child("default");
        //userPos = "Blogger";
        userPos = TrialActivity.userStatus;


        try {
            userProfile = user.getPhotoUrl().toString();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (userProfile == null) {
            String add = "firebasestorage.googleapis.com/v0/b/roobaru-duniya-86f7d.appspot.com/o/default-profilepic%2Fdefaultprof.jpg?alt=media&token=aeca7a55-05e4-4c02-938f-061624f5c8b4";
            userProfile = Uri.parse("https://" + add).toString();
        }


        db = FirebaseDatabase.getInstance();
        dbRefMsg = db.getReference("messages");
        category = db.getReference("categories");
        dbEditor = db.getReference("editor");
        dbRefUser = db.getReference("user");
        dbPendingArticle = db.getReference("pending");
        publishedRef = db.getReference("published");
        contentStyleRef = db.getReference("contentStyle");
        dbtitlepublished = db.getReference("publishedTitle");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("article_photo");
        //  FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);
        title = (EditText) findViewById(R.id.post_title_edit);
        content = (EditText) findViewById(R.id.post_content);
        Intent intent = getIntent();
        try {
            pos = intent.getIntExtra("position", -1);
            key = intent.getStringExtra("Keypos");
            // Log.d("keypos", key);

            //  Log.d("checkpos", "" + pos);
            rbd = (RoobaruDuniya) intent.getSerializableExtra(DraftFragment.TAG);
            title.setText(rbd.getTitle());
            content.setText(rbd.getContent());
            draftPressed += 1;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        /*
        dbEditor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getValue().equals(userEmail)) {
                        userPos = "Editor";
                        dbEditor.removeEventListener(this);
                        break;

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */


        photoButton = (ImageButton) findViewById(R.id.photoPickerButton);
        writerDetail = (Button) findViewById(R.id.writer_detail);
        photoButton.setEnabled(false);

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);


            }
        });


    }

    public Dialog onCreateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_writer, null);
        final EditText writerName = (EditText) dialogView.findViewById(R.id.username);
        final ImageButton profilePhoto = (ImageButton) dialogView.findViewById(R.id.profilePhotoButton);
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PROFILE_PICKER);
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...

                        wName = writerName.getText().toString();
                        if (downloadProfileUrl != null) {
                            //  Log.d("checkdownload",downloadProfileUrl.toString());
                        }
                        if (rbd != null) {
                            rbd.setUser(wName);
                            if (downloadProfileUrl != null) {
                                rbd.setUserProfilePhoto(downloadProfileUrl.toString());
                            }

                            //TODO: SET user profile photo
                        } else if (rbd == null) {
                            rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), null, wName, userId, null, 0, 0);
                            if (downloadProfileUrl != null) {
                                rbd.setUserProfilePhoto(downloadProfileUrl.toString());
                            }
                        }
                        if (downloadProfileUrl == null) {
                            String add = "firebasestorage.googleapis.com/v0/b/roobaru-duniya-86f7d.appspot.com/o/default-profilepic%2Fdefaultprof.jpg?alt=media&token=aeca7a55-05e4-4c02-938f-061624f5c8b4";
                            Uri defaultuserpicUrl = Uri.parse("https://" + add);
                            rbd.setUserProfilePhoto(defaultuserpicUrl.toString());
                        }


                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    public void onStart() {
        //   Log.d("TrialOnStat", uStatus);

        try {
            if (uStatus.equals("editor")) {
                spinner.setVisibility(View.VISIBLE);
                adapter = ArrayAdapter.createFromResource(this,
                        R.array.catgs, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(this);
                writerDetail.setVisibility(View.VISIBLE);
                writerDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Dialog d = onCreateDialog();
                        d.show();

                    }
                });

                // Log.d("writername", wName);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence st, int start, int before, int count) {
                if ((st.toString().trim().length()) > 0)

                {
                    if(st.toString().trim().length()>30)
                    {
                        Toast.makeText(WriteArticleActivity.this,"title length cannot be more than 30",Toast.LENGTH_LONG).show();

                    }
                    else {
                        if (rbd != null) {
                            rbd.setTitle(st.toString());
                            draftButton.setEnabled(true);

                        }
                    }


                }

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

     content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


            }
        });


        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.toString().trim().length()) > 0)

                {
                    if (rbd != null) {
                        rbd.setContent(s.toString());
                    }
                    draftButton.setEnabled(true);
                    photoButton.setEnabled(true);

                    if ((s.toString().trim().length()) > 50) {
                        publishButton.setEnabled(true);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bulletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spannable str = content.getText();
                int start = content.getSelectionStart();
                int end = content.getSelectionEnd();
                boolean exists = false;
                if (start > end) {
                    int temp = end;
                    end = start;
                    start = temp;
                }
                TextFormat format = new TextFormat("bullet", content.getSelectionStart(), content.getSelectionEnd());


                BulletSpan[] quoteSpan = str.getSpans(start, end, BulletSpan.class);

                // If the selected text-part already has UNDERLINE style on it, then we need to disable it
                for (int i = 0; i < quoteSpan.length; i++) {
                    str.removeSpan(quoteSpan[i]);
                    formatList.remove(format);
                    exists = true;
                    break;
                }

                // Else we set UNDERLINE style on it
                if (!exists) {
                    str.setSpan(new BulletSpan(10), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    formatList.add(format);
                }
                content.setSelection(start, end);

            }

        });


        italicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spannable str = content.getText();
                int start = content.getSelectionStart();
                int end = content.getSelectionEnd();
                boolean iExists = false;
                if (start > end) {
                    int temp = end;
                    end = start;
                    start = temp;

                }
                TextFormat format = new TextFormat("italic", content.getSelectionStart(), content.getSelectionEnd());


                StyleSpan[] styleSpans = str.getSpans(start, end, StyleSpan.class);

                // If the selected text-part already has BOLD style on it, then
                // we need to disable it
                for (int i = 0; i < styleSpans.length; i++) {
                    if (styleSpans[i].getStyle() == Typeface.ITALIC) {
                        str.removeSpan(styleSpans[i]);
                        iExists = true;

                        formatList.remove(format);
                        break;
                    }
                }


                // Else we set BOLD style on it
                if (!iExists) {
                    str.setSpan(new StyleSpan(Typeface.ITALIC), start, end,
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    formatList.add(format);
                }

                content.setSelection(start, end);
            }
        });
        boldButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Spannable str = content.getText();
                int start = content.getSelectionStart();
                int end = content.getSelectionEnd();
                boolean bExists = false;
                if (start > end) {
                    int temp = end;
                    end = start;
                    start = temp;
                }
                TextFormat format = new TextFormat("bold", content.getSelectionStart(), content.getSelectionEnd());


                StyleSpan[] styleSpans = str.getSpans(start, end, StyleSpan.class);

                // If the selected text-part already has BOLD style on it, then
                // we need to disable it
                for (int i = 0; i < styleSpans.length; i++) {
                    if (styleSpans[i].getStyle() == android.graphics.Typeface.BOLD) {
                        str.removeSpan(styleSpans[i]);
                        bExists = true;

                        formatList.remove(format);
                        break;
                    }
                }


                // Else we set BOLD style on it
                if (!bExists) {
                    str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end,
                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    formatList.add(format);
                }

                content.setSelection(start, end);
            }
        });

     /*   hyperlink_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SpannableString str = new SpannableString(content.getText());

                if(content.getSelectionEnd() > content.getSelectionStart()) {
                    String selectedText = content.getText().toString().substring(
                           content.getSelectionStart(),
                            content.getSelectionEnd()
                    ) ;


                   // String url = "https://developer.android.com";
                    str.setSpan(new URLSpan(selectedText), content.getSelectionStart(), content.getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// set to textview

                   content.setMovementMethod(LinkMovementMethod.getInstance());

                }

            }
        });
        */


        super.onStart();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_write, menu);
        draftButton = (MenuItem) menu.findItem(R.id.draft);
        saveButton = (MenuItem) menu.findItem(R.id.savecl);
        publishButton = (MenuItem) menu.findItem(R.id.publish);
        deleteButton = (MenuItem) menu.findItem(R.id.delete_draft);
        return true;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.draft: {
                try {

                    if (rbd == null) {
                        rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), null, user.getDisplayName().toString(), userId, userProfile, 1, 0);

                    } else if (rbd != null) {
                        if (rbd.getDraft() == 0) {
                            rbd.setDraft(1);
                        }
                    }
                    if (draftPressed == 0) {
                        key = dbRefMsg.push().getKey();
                        dbRefMsg.child(key).setValue(rbd);
                        draftPressed += 1;
                    } else {
                        dbRefMsg.child(key).setValue(rbd);
                    }

                   /* if (u == null) {
                        u = new User(user.getDisplayName(), userEmail, "draft",userPos);
                    }
                    */


                    dbRefUser.child(userId).child("articleStatus").child(key).setValue("draft");


                    Toast.makeText(this, R.string.draft_saved, Toast.LENGTH_LONG).show();
                    deleteButton.setEnabled(true);
                    draftButton.setEnabled(false);
                    saveButton.setEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.savecl: {
                saveNclose();
                break;
            }
            case R.id.publish: {
                if (content.length() < 200) {
                    Snackbar.make(llout, R.string.word_limit_msg, Snackbar.LENGTH_LONG).show();
                    break;
                }
                if (title.length() < 2) {
                    // Log.d("chktitle","we r here");
                    Snackbar.make(llout, R.string.article_title_msg, Snackbar.LENGTH_SHORT).show();
                    break;
                }


                draftButton.setEnabled(false);


                //       try {


                if (rbd == null) {
                    rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), null, user.getDisplayName().toString(), userId, userProfile, 0, 1);
                    key = dbRefMsg.push().getKey();
                    dbRefMsg.child(key).setValue(rbd);

                }
                //when photo is selected
                else if (rbd != null) {
                    rbd.setDraft(0);
                    rbd.setSent(1);
                    //  Log.d("checkphoto", rbd.getPhoto());
                    if (key == null) {
                        key = dbRefMsg.push().getKey();

                    }

                    dbRefMsg.child(key).setValue(rbd);


                }
                if (formatList.size() > 0) {
                    for (TextFormat ft : formatList) {
                        String str = ft.getStart() + " " + ft.getEnd() + " " + ft.getStyle();
                      //  Log.d("chk", str);
                           /* String st= String.valueOf(ft.getStart());
                            String lt= String.valueOf(ft.getEnd());
                            Map<String,String> mp=new HashMap<>();
                            mp.put(ft.getStyle(),"style");
                            mp.put(st,"start");
                            mp.put(lt,"end");
                            */

                        contentStyleRef.child(key).push().setValue(ft);


                    }

                }



                if (userPos.equals("editor")) {

                    //TODO: publish editor
                    //select photo from storage and put in rbd object and firebase database
                    if (rbd.getPhoto() == null) {
                        String add = "https://firebasestorage.googleapis.com/v0/b/roobaru-duniya-86f7d.appspot.com/o/article_photo%2F2943?alt=media&token=14bb10c1-ddc8-4ac2-b425-4a862721ecd2";
                        rbd.setPhoto(add);
                        dbRefMsg.child(key).child("photo").setValue(rbd.getPhoto());





                   /* Random rand = new Random();
                    int value = rand.nextInt(4);
                    String st=value+".jpg";
                    Log.d("image",st);
                    defaultPhoto.child(st).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {

                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("imageuri",uri.toString());
                            rbd.setPhoto(uri.toString());
                            dbRefMsg.child(key).child("photo").setValue(rbd.getPhoto());
                        }
                    });
                    */


                    }
                    publishEditor();
                    addCategoryDb();
                } else {


                    // Log.d("chkuse", u.getarticleStatus());

                    //  Log.d("pubbool", "" + b2);
                    //  Log.d("pubkey", key);
                    dbRefUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists())
                            {
                                User u=new User(user.getDisplayName(),userEmail,null,uStatus,userProfile);
                                dbRefUser.child(userId).setValue(u);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                    dbRefUser.child(userId).child("articleStatus").child(key).setValue("sent");


                    //catch(Exception e){
                    //   e.printStackTrace();
                    //  }
                    PendingClass pending = new PendingClass(false, false, null);
                    //  Log.d("pendingKey", key);

                    dbPendingArticle.child(key).setValue(pending);
                    Toast.makeText(this, R.string.send_approval, Toast.LENGTH_LONG).show();
                }


                saveNclose();
                break;

            }
            case R.id.delete_draft: {
                dbRefMsg.child(key).removeValue();
                dbRefUser.child(userId).child("articleStatus").child(key).removeValue();
                saveNclose();
                break;
            }


        }
        return super.onOptionsItemSelected(item);
    }

    private void publishEditor() {


        //  Log.d("chkuse", u.getarticleStatus());

        dbRefUser.child(userId).child("articleStatus").child(key).setValue("published");

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        // Log.d("checkDate",dateString);
        publishedRef.child(key).child("dateCreated").setValue(dateString);
        dbtitlepublished.child(key).setValue(rbd.getTitle());
        new SendEmail().execute(rbd);
    }

    private void saveNclose() {
        draftPressed = 0;
        key = null;
        super.onBackPressed();
        //  Intent inti = new Intent(this, TrialActivity.class);
        // startActivity(inti);

    }

    protected void onPause() {
        super.onPause();
        if (eventListener != null) {
            dbRefUser.removeEventListener(eventListener);
            eventListener = null;
        }
    }


    @Override
    public void onBackPressed() {
        saveNclose();

    }

    public void onActivityResult(int requestcode, int resultcode, Intent data) {

        if (requestcode == RC_PHOTO_PICKER && resultcode == RESULT_OK) {

            final Uri SelectedImageUri = data.getData();
            Toast.makeText(this, R.string.wait_photo, Toast.LENGTH_LONG).show();

            StorageReference photoref = storageReference.child(SelectedImageUri.getLastPathSegment());
            photoref.putFile(SelectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    try {
                        if (rbd != null) {
                            rbd.setPhoto(downloadUrl.toString());

                        } else if (rbd == null) {
                            rbd = new RoobaruDuniya(title.getText().toString(), content.getText().toString(), downloadUrl.toString(), user.getDisplayName().toString(), userId, userProfile, 0, 0);

                        }
                        //    Log.d("chkphotoup",rbd.getPhoto());
                        draftButton.setEnabled(true);
                    } catch (NullPointerException e) {
                    }

                    //  publishButton.setEnabled(true);

                }
            });

            // Log.d("exception", "" + e);


            //TODO use progressbar





            Toast.makeText(this, R.string.photo_uploaded, Toast.LENGTH_LONG).show();


        } else if (requestcode == RC_PROFILE_PICKER && resultcode == RESULT_OK) {
            final Uri SelectedProfileUri = data.getData();
            StorageReference picref = storageReference.child(SelectedProfileUri.getLastPathSegment());
            picref.putFile(SelectedProfileUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadProfileUrl = taskSnapshot.getDownloadUrl();
                }


            });


            Toast.makeText(this, R.string.photo_uploaded, Toast.LENGTH_LONG).show();
        }
    }

    private void addCategoryDb() {

        // it+=1;
        long tm=-1 * new Date().getTime();


        HomeDisplay hm = new HomeDisplay(rbd.getTitle(), rbd.getPhoto(),tm);


        category.child(categoryChoosen).child(key).setValue(hm);
        //  long times=-1 * new Date().getTime();
        //  category.child(categoryChoosen).child(key).child("timestamp").setValue(times);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // Log.d("item", (String) parent.getItemAtPosition(position));

        categoryChoosen = (String) parent.getItemAtPosition(position);

        adapter.notifyDataSetChanged();

       // Log.d("catselected", categoryChoosen);


    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class AsyncUpload extends AsyncTask<Object, Object, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            uploaddefaultImage();
            return null;
        }

        private void uploaddefaultImage() {
            Random rand = new Random();
            int value = rand.nextInt(4);
            String st = value + ".jpg";
            // Log.d("image",st);
            defaultPhoto.child(st).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                @Override
                public void onSuccess(Uri uri) {
                    // Log.d("imageuri",uri.toString());
                    rbd.setPhoto(uri.toString());
                    dbRefMsg.child(key).child("photo").setValue(uri.toString());
                }
            });
        }

        @Override
        protected void onPostExecute(Void res) {
            super.onPostExecute(res);
            Toast.makeText(WriteArticleActivity.this, R.string.wait_msg, Toast.LENGTH_LONG).show();
        }


    }


}

