package com.samiapps.kv.roobaruduniya;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.BulletSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Random;

/**
 * Created by KV on 29/6/17.
 */

public class EditorArticleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText title;
    EditText content;
    FirebaseDatabase db;
    DatabaseReference dbRefMsg;
    DatabaseReference dbRefUser;
    DatabaseReference dbEditor;
    DatabaseReference dbPendingArticle;
    DatabaseReference publishedRef;
    DatabaseReference category;
    DatabaseReference styleReference;
    private DatabaseReference dbtitlepublished;


    private  String contentString;
    ImageButton photoButton;
    int pos;
    String key;
    String writerId;
    RoobaruDuniya rbd;
    MenuItem approveButton;
    MenuItem saveEditorButton;
    MenuItem rejectButton;
    Button italicButton;
    Button boldButton;
    ImageButton bulletButton;
    ArrayList<TextFormat> formatList;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private StorageReference defaultPhoto;
    ArrayAdapter<CharSequence> adapter;
    Spinner categorySpinner;
    private boolean titleChanged;
    int it = 0;

    private boolean contentChanged;
    String categoryChoosen;
    private static final int RC_PHOTO_PICK = 3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_article);
        title = (EditText) findViewById(R.id.post_title_edit);
        content = (EditText) findViewById(R.id.post_content);
        db = FirebaseDatabase.getInstance();
        dbRefMsg = db.getReference("messages");
        dbEditor = db.getReference("editor");
        dbRefUser = db.getReference("user");
        dbPendingArticle = db.getReference("pending");
        dbtitlepublished = db.getReference("publishedTitle");
        publishedRef = db.getReference("published");
        styleReference = db.getReference("contentStyle");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("article_photo");
        category = db.getReference("categories");

        defaultPhoto = firebaseStorage.getReference().child("defaultpp");
        italicButton = (Button) findViewById(R.id.italic_button);
        boldButton = (Button) findViewById(R.id.bold_button);
        bulletButton = (ImageButton) findViewById(R.id.add_bullet);
        formatList = new ArrayList<>();
        categorySpinner = (Spinner) findViewById(R.id.spinner1);
        categorySpinner.setVisibility(View.VISIBLE);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.catgs, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(this);
        Intent intent = getIntent();
        try {
            pos = intent.getIntExtra("position", -1);
            key = intent.getStringExtra("Keypos");

            // Log.d("keypos",key);

            // Log.d("checkpos", "" + pos);
            rbd = (RoobaruDuniya) intent.getSerializableExtra(SentFragment.TAG);
            writerId = rbd.getuserId();
            title.setText(rbd.getTitle());
            loadContent();
            //content.setText(rbd.getContent());
            //draftPressed+=1;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        photoButton = (ImageButton) findViewById(R.id.photoPickerButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log.d("ckphtot","photoslected");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICK);

            }
        });


    }

    private void loadContent() {

        final SpannableStringBuilder str = new SpannableStringBuilder(rbd.getContent());
        styleReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contentString = rbd.getContent();
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
                    content.setText(str);
                } else {
                    content.setText(rbd.getContent());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        approveButton = (MenuItem) menu.findItem(R.id.approved);
        saveEditorButton = (MenuItem) menu.findItem(R.id.saveEditor);
        rejectButton = (MenuItem) menu.findItem(R.id.reject);
        return true;
    }

    public void onStart() {

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence st, int start, int before, int count) {
                titleChanged = true;
                if ((st.toString().trim().length()) > 0)

                {
                    if (rbd != null) {
                        rbd.setTitle(st.toString());
                        saveEditorButton.setEnabled(true);

                    }


                }

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //added to enable keyboard whenever content is edited
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
                contentChanged = true;
                if ((s.toString().trim().length()) > 0)

                {
                    if (rbd != null) {
                        rbd.setContent(s.toString());
                    }


                    if ((s.toString().trim().length()) > 50) {
                        saveEditorButton.setEnabled(true);
                        approveButton.setEnabled(true);
                    } else
                        approveButton.setEnabled(false);

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



        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.approved: {
                //TODO: photo displaying late\


                //check if photo is null
                if (rbd.getPhoto() == null) {
                    //select random photo from storage and put in rbd object and firebase database


                    String st = 0 + ".jpg";
                    // Log.d("image",st);
                    defaultPhoto.child(st).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                        @Override
                        public void onSuccess(Uri uri) {
                            //  Log.d("imageuri",uri.toString());
                            rbd.setPhoto(uri.toString());
                            dbRefMsg.child(key).child("photo").setValue(rbd.getPhoto());
                        }
                    });

                }
                if (contentChanged || titleChanged) {
                    rbd.setTitle(title.getText().toString());
                    rbd.setContent(content.getText().toString());
                    dbRefMsg.child(key).setValue(rbd);

                }

                PendingClass pc = new PendingClass(true, true, TrialActivity.mUsername);
                dbPendingArticle.child(key).setValue(pc);
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

                        styleReference.child(key).push().setValue(ft);


                    }

                }
              /*  AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setTitle("Choose Category");
                String[] types = getResources().getStringArray(R.array.catgs);
                b.setItems(types,new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        switch(which){
                            case 0:
                                categoryChoosen=getString(R.string.cat1);
                                break;
                            case 1:
                                categoryChoosen=getString(R.string.cat2);
                                break;
                            case 3:
                                categoryChoosen=getString(R.string.cat3);
                            case 4:
                                categoryChoosen=getString(R.string.cat4);
                        }
                      //  dialog.dismiss();

                    }

                });

                b.show();
                */
                new SendEmail().execute(rbd);
                addPublishedDatabase();
                //CHANGE VALUE OF ARTICLE STATUS IN USERDB TO PUBLISHED
                changeUserDB();
                addCategoryDb();

                // FirebaseMessagingService
                //approveButton.setEnabled(false);
                saveEditorButton.setEnabled(false);
                rejectButton.setEnabled(false);

                close();


                break;
            }
            case R.id.reject: {
                PendingClass pc = new PendingClass(true, false, TrialActivity.mUsername);
                dbPendingArticle.child(key).setValue(pc);
                approveButton.setEnabled(false);
                saveEditorButton.setEnabled(false);
                removeDB();
                //TODO REMOVE FROM DB
                close();
                break;
            }
            case R.id.saveEditor: {
                dbRefMsg.child(key).setValue(rbd);
                break;
            }

        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void addCategoryDb() {
     //   Log.d("catc", categoryChoosen);
      //  Log.d("ckk", key);
        long tm=-1 * new Date().getTime();
        HomeDisplay hm = new HomeDisplay(rbd.getTitle(), rbd.getPhoto(),tm);

        // it+=1;
        category.child(categoryChoosen).child(key).setValue(hm);
        //  long times=-1 * new Date().getTime();
        //  category.child(categoryChoosen).child(key).child("timestamp").setValue(times);
    }

    private void removeDB() {
        //remove data from firebase msg ref,user ref,pending ref
        dbRefUser.child(writerId).child("articleStatus").child(key).removeValue();
        dbRefMsg.child(key).removeValue();
        dbPendingArticle.child(key).removeValue();
       


    }

    private void addPublishedDatabase() {
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        // Log.d("checkDate",dateString);
        publishedRef.child(key).child("dateCreated").setValue(dateString);
        publishedRef.child(key).child("likes").setValue(0);
        dbtitlepublished.child(key).setValue(rbd.getTitle());

    }

    private void changeUserDB() {
        //  Log.d("writerId",writerId);
        dbRefUser.child(writerId).child("articleStatus").child(key).setValue("published");


    }

    private void close() {
        Intent intent = new Intent(this, TrialActivity.class);
        startActivity(intent);
    }

    public void onActivityResult(int requestcode, int resultcode, Intent data) {

        if (requestcode == RC_PHOTO_PICK && resultcode == RESULT_OK) {
            final Uri SelectedImageUri = data.getData();
            if (content.toString() != null) {
                StorageReference photoref = storageReference.child(SelectedImageUri.getLastPathSegment());
                photoref.putFile(SelectedImageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        try {
                            if (rbd != null) {
                                rbd.setPhoto(downloadUrl.toString());

                            }
                            saveEditorButton.setEnabled(true);


                        } catch (NullPointerException e) {
                            //  Log.d("exception", "" + e);
                        }
                    }
                });
                Toast.makeText(this, R.string.photo_uploaded, Toast.LENGTH_LONG).show();


            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
      //  Log.d("item", (String) parent.getItemAtPosition(position));

        categoryChoosen = (String) parent.getItemAtPosition(position);

        adapter.notifyDataSetChanged();

      //  Log.d("catsel", categoryChoosen);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }
}
