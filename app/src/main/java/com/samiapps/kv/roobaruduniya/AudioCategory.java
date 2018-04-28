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

public class AudioCategory extends Fragment {
    RecyclerView AudioRecycler;
    DatabaseReference audioReference,dbCat;
    AudioHorizontalAdapter audioApt;
    List<AudioStories> allSampleData;
    TabLayout tblays;
    String key;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        audioReference=db.getReference().child("audiodb");
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.audio_category,container,false);
        AudioRecycler=(RecyclerView)rootView.findViewById(R.id.show_audiorecycler);
        allSampleData=new ArrayList<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbCat = db.getReference().child("audioCategory");
        audioReference=db.getReference().child("audiodb");
        tblays=(TabLayout) rootView.findViewById(R.id.tabs);
        tblays.addTab(tblays.newTab().setText("Home"));
        tblays.addTab(tblays.newTab().setText("Blogs"));
        tblays.addTab(tblays.newTab().setText("Audio"));
        tblays.addTab(tblays.newTab().setText("Photos"));
        TabLayout.Tab tab = tblays.getTabAt(2);
        tab.select();
        tblays.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showAudioActivity fragment=new showAudioActivity();
                    android.support.v4.app.FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,fragment,"home");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();


                } else if (tab.getPosition() == 1) {
                    MainPage fragment2=new MainPage();
                    android.support.v4.app.FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,fragment2,"home");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else if(tab.getPosition()==2)
                {



                }
                else
                {
                    PhotoCategory photoCategory=new PhotoCategory();
                    android.support.v4.app.FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,photoCategory,"home");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        AudioRecycler.setLayoutManager(layoutManager);
        audioApt=new AudioHorizontalAdapter(getContext(),allSampleData);
        AudioRecycler.setAdapter(audioApt);
        readAudioDb();

        return rootView;



    }
    private void readAudioDb() {




        //categoryActivity

        dbCat.child("जनहित पर भारी").orderByChild("timeval").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    //keys.add(null);



                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {



                        key = postSnapshot.getKey();
                          Log.d("ckadk", key);

                        getMessageData1(key);


                        //  error.setVisibility(View.GONE);
                        // mRecycleView.setVisibility(View.VISIBLE);

                        // mLoadingIndicator.setVisibility(View.VISIBLE);




                        // imageAdapter.notifyDataSetChanged();


                        // displayArticles(postSnapshot.getKey());


                    }



                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void getMessageData1(final String key) {

        audioReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.d("finalkey",key);

                AudioStory au=dataSnapshot.getValue(AudioStory.class);
                AudioStories aus=new AudioStories(key,au);
                Log.d("checkaukey",key);
                // ral1.add(aus);
                allSampleData.add(aus);
                audioApt.notifyDataSetChanged();




                // AudioStory md=new AudioStory(au.getTitle(),au.getPhoto());


                //mLoadingIndicator.setVisibility(View.GONE);
                // Log.d("getcattitle",rbd.getTitle());
                //rubaru.add(rbd);
                //imageAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
}
