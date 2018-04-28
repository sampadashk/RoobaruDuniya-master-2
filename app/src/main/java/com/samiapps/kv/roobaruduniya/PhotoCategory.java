package com.samiapps.kv.roobaruduniya;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KV on 25/2/18.
 */

public class PhotoCategory extends Fragment {

    RecyclerView photoRecycler;
    PhotoAdapter photoApt;
    List<PhotoStories> photoStories;
    private TabLayout tblays;
    private DatabaseReference photoReference;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.photo_category,container,false);
        photoRecycler=(RecyclerView)rootView.findViewById(R.id.show_photorecycler);
        photoStories=new ArrayList<>();
        tblays=(TabLayout) rootView.findViewById(R.id.tabs);
        tblays.addTab(tblays.newTab().setText("Home"));
        tblays.addTab(tblays.newTab().setText("Blogs"));
        tblays.addTab(tblays.newTab().setText("Audio"));
        tblays.addTab(tblays.newTab().setText("Photos"));
        TabLayout.Tab tab = tblays.getTabAt(3);
        tab.select();


        tblays.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showAudioActivity fragment2=new showAudioActivity();
                    android.support.v4.app.FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,fragment2,"home");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else if (tab.getPosition() == 1) {

                    MainPage mp=new MainPage();
                    android.support.v4.app.FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,mp,"home");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
                else if(tab.getPosition()==2)
                {
                    AudioCategory fragment3=new AudioCategory();
                    android.support.v4.app.FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,fragment3,"home");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                else
                {

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        photoReference=db.getReference().child("photoDb");
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        photoRecycler.setLayoutManager(layoutManager);
        photoApt=new PhotoAdapter(getContext(),photoStories);
        photoRecycler.setAdapter(photoApt);
        readPhotoDb();

        return rootView;



    }
    private void readPhotoDb() {
        photoReference.orderByChild("timeval").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    //keys.add(null);


                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String key=postSnapshot.getKey();
                        //Photo photo= (Photo) postSnapshot.getValue();
                        // photos.add(photo);
                        Log.d("checkphototitle",key);
                        Photo p=  postSnapshot.getValue(Photo.class);
                        Log.d("checkphototitle",p.getTitle());
                        PhotoStories pstories=new PhotoStories(key,p);
                        photoStories.add(pstories);
                        // photos.add(p);

                    }
                    photoApt.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    }

