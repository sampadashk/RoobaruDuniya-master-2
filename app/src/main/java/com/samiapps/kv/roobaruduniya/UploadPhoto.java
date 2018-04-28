package com.samiapps.kv.roobaruduniya;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
 * Created by KV on 3/1/18.
 */

public class UploadPhoto extends AppCompatActivity {
    private static final int RC_PHOTO_PICKER = 5;
    private DatabaseReference photoRef;
    private Button photoUpload;

    private EditText name, title,storyPhoto;
    private ImageView photoImage;
    private Photo photo;
    String namephotographer, photoTitle,storyPhotoText;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Uri downloadUrl;
    private MenuItem publishButton,deleteButton;
    private String key;
    private DatabaseReference photoPublishedDb;
    ProgressDialog progressDialog;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_photo);
        photoRef = FirebaseDatabase.getInstance().getReference("photoDb");
        photoPublishedDb=FirebaseDatabase.getInstance().getReference("photoPublishedDb");
        name = (EditText) findViewById(R.id.post_photographer);
        title = (EditText) findViewById(R.id.post_title_photo);
        storyPhoto = (EditText) findViewById(R.id.post_story_photo);



        photoUpload = (Button) findViewById(R.id.upload_photobutton);
        photoImage = (ImageView) findViewById(R.id.imagedisplay);
        photo=new Photo();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("article_photocategory");
        storyPhoto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    storyPhotoText = charSequence.toString();
                }
            }



            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    namephotographer = charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() > 0) {
                    photoTitle = charSequence.toString();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        photoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_photo, menu);

        publishButton = (MenuItem) menu.findItem(R.id.publish_photo);

        return true;

    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.publish_photo: {
                if (photo.getPhoto() != null) {
                    if (photo.getTitle() != null) {
                        key = photoRef.push().getKey();
                        if(photo.getUser()==null)
                        {
                            if(namephotographer!=null)
                            photo.setUser(namephotographer);
                        }
                        long tm=-1 * new Date().getTime();
                        photo.setTimeval(tm);
                        photoRef.child(key).setValue(photo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                addPublisheDb();

                            }
                        });
                    }
                    else
                    {
                        if(photoTitle.length()<0)
                        {
                            Toast.makeText(UploadPhoto.this,"Please give your photo a title",Toast.LENGTH_LONG).show();
                        }
                        else
                            photo.setTitle(photoTitle);

                            if (storyPhoto.getText().toString() == null) {
                                Toast.makeText(UploadPhoto.this,"Please write about your photo",Toast.LENGTH_LONG).show();


                            }
                            else
                                photo.setAboutPhoto(storyPhotoText);


                    }





                } else {
                    Toast.makeText(UploadPhoto.this,"Please Upload Photo first",Toast.LENGTH_LONG).show();

                }
            }

        }
        return super.onOptionsItemSelected(item);
    }
    private void addPublisheDb() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);
        // Log.d("checkDate",dateString);
        photoPublishedDb.child(key).child("dateCreated").setValue(dateString).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


                Toast.makeText(UploadPhoto.this,"Upload succesful",Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }


    public void onActivityResult(int requestcode, int resultcode, Intent data) {

        if (requestcode == RC_PHOTO_PICKER && resultcode == RESULT_OK) {

            final Uri SelectedImageUri = data.getData();
            progressDialog = new ProgressDialog(UploadPhoto.this);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);

            StorageReference photoref = storageReference.child(SelectedImageUri.getLastPathSegment());
            photoref.putFile(SelectedImageUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    long l= taskSnapshot.getTotalByteCount();


                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    //sets and increments value of progressbar
                    progressDialog.incrementProgressBy((int) progress);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadPhoto.this,"Error in uploading!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     downloadUrl = taskSnapshot.getDownloadUrl();
                    Glide.with(UploadPhoto.this).load(downloadUrl.toString()).into(photoImage);
                    StorageReference photoref = storageReference.child(SelectedImageUri.getLastPathSegment());
                    photoref.putFile(SelectedImageUri).addOnSuccessListener(UploadPhoto.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            try {
                                if (photo!= null) {
                                    photo.setPhoto(downloadUrl.toString());

                                } else if (photo == null) {
                                    photo = new Photo(name.getText().toString(), downloadUrl.toString(),title.getText().toString(),0,storyPhotoText);
                                    Toast.makeText(UploadPhoto.this,"Photo Uploaded",Toast.LENGTH_SHORT).show();

                                }

                                progressDialog.dismiss();
                                //    Log.d("chkphotoup",rbd.getPhoto());

                            } catch (NullPointerException e) {
                            }

                            //  publishButton.setEnabled(true);

                        }
                    });


                }
            });

            // Log.d("exception", "" + e);


            //TODO use progressbar

        }
    }
}
