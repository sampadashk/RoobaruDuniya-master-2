package com.samiapps.kv.roobaruduniya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KV on 3/12/17.
 */

public class UploadAudio extends AppCompatActivity  {
    private static final int RC_PHOTO = 123;
    private static final int RC_AUDIO=124;
    ImageButton photoButton;
    ImageButton audioButton;

    String title, wName, sTeller, abStory;
    StorageReference audioPhotoRef;
    StorageReference audioRef;
   DatabaseReference fbd;
   AudioStory au;
   ProgressDialog progressDialog,pd1;

   DatabaseReference audioCatRef;
   DatabaseReference audioPublishedDb;


    EditText audioTtitle, audioWriter, storyTeller, abtStory;
    Uri downloadPhUrl;
    private Uri downloadAuUrl;
    private String categoryChose;

    private String key;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_audiow, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_upload);

        photoButton = (ImageButton) findViewById(R.id.photoPickerButton1);
        audioButton = (ImageButton) findViewById(R.id.audio1);
        fbd=FirebaseDatabase.getInstance().getReference("audiodb");
        audioCatRef=FirebaseDatabase.getInstance().getReference("audioCategory");
        audioPublishedDb=FirebaseDatabase.getInstance().getReference("audioPublished");
        au=new AudioStory();
        categoryChose=getString(R.string.audio_opt2);



        audioTtitle = (EditText) findViewById(R.id.post_title_audio);
        audioWriter = (EditText) findViewById(R.id.post_storywriter);
        storyTeller = (EditText) findViewById(R.id.storyteller);
        abtStory = (EditText) findViewById(R.id.abt_story);


        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        audioPhotoRef = firebaseStorage.getReference().child("audio_photo");
        audioRef=firebaseStorage.getReference().child("audio");









        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO);


            }
        });
        audioButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_AUDIO);

            }
        });


    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id)
        {
            case R.id.publish_audio:
            {
                if(au.getAudio()!=null)
                {
                   key= fbd.push().getKey();
                    fbd.child(key).setValue(au).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("uploadstataus","upload succesful");
                            addCategoryDb();
                           // addPublisheDb();
                        }
                    });
                }
                break;

            }
        }
        return super.onOptionsItemSelected(item);

    }

    private void addPublisheDb() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        // Log.d("checkDate",dateString);
        audioPublishedDb.child(key).child("dateCreated").setValue(dateString).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UploadAudio.this,"Upload succesful",Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }




    public void onStart()
    {
        super.onStart();
        audioTtitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()>0)
                {
                    String title=charSequence.toString();
                    au.setTitle(title);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        abtStory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()>0)
                {
                    String abtStory=charSequence.toString();
                    au.setAbout(abtStory);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        storyTeller.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()>0)
                {
                    String teller=charSequence.toString();
                    au.setTeller(teller);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        audioWriter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()>0)
                {
                    String writer=charSequence.toString();
                    au.setWriter(writer);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });




    }

    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        if (requestcode == RC_PHOTO && resultcode == RESULT_OK) {
            progressDialog = new ProgressDialog(UploadAudio.this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);
            final Uri SelectedImageUri = data.getData();
            StorageReference picref = audioPhotoRef.child(SelectedImageUri.getLastPathSegment());
            picref.putFile(SelectedImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //sets and increments value of progressbar
                    progressDialog.incrementProgressBy((int) progress);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadAudio.this,"uploading failed!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    downloadPhUrl = taskSnapshot.getDownloadUrl();
                    au.setPhoto(downloadPhUrl.toString());

                    Toast.makeText(UploadAudio.this,"Photo Uploaded!",Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            if (requestcode == RC_AUDIO && resultcode == RESULT_OK) {
                pd1 = new ProgressDialog(UploadAudio.this);
                pd1.setMax(100);
                pd1.setMessage("Uploading...");
                pd1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd1.show();
                pd1.setCancelable(false);
                final Uri SelectedAudioUri = data.getData();
                Log.d("audioch",SelectedAudioUri.toString());
                StorageReference audioreference = audioRef.child(SelectedAudioUri.getLastPathSegment());
                audioreference.putFile(SelectedAudioUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        //sets and increments value of progressbar
                        pd1.incrementProgressBy((int) progress);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd1.dismiss();
                        Toast.makeText(UploadAudio.this,"Audio Uploaded failed",Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        downloadAuUrl = taskSnapshot.getDownloadUrl();
                        au.setAudio(downloadAuUrl.toString());
                        pd1.dismiss();

                        Toast.makeText(UploadAudio.this,"Audio Uploaded!",Toast.LENGTH_SHORT).show();

                    }
                });

            }

        }
    }


    private void addCategoryDb() {

        // it+=1;
        long tm=-1 * new Date().getTime();


        HomeDisplay hm = new HomeDisplay(au.getTitle(), au.getPhoto(),tm);


        audioCatRef.child(categoryChose).child(key).setValue(hm).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                addPublisheDb();

            }
        });
        //  long times=-1 * new Date().getTime();
        //  category.child(categoryChoosen).child(key).child("timestamp").setValue(times);
    }


}

