package com.samiapps.kv.roobaruduniya;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 * Created by KV on 10/12/17.
 */

public class showAudioActivity extends Fragment {
    RecyclerView rc1,rc2,latestrc;
    ArrayList<AudioStories> ral1 = new ArrayList<>();
    TabLayout tblays;

    private DatabaseReference dbCat;


    DatabaseReference dbLatest;
    private DatabaseReference audioReference;
    private DatabaseReference photoReference;
    DatabaseReference dbMessage;
    AudioHorizontalAdapter manApt;
    PhotoAdapter photoApt;
    List<Photo> photos;
    List<PhotoStories> photoStories;
    ArrayList<RoobaruList> latestList;
    String key;
    private String key1;
    LatestAdapter latestAdapter;
    SectionAudio a1;
    SectionAudio a2;
    ValueEventListener latestListener;
  List<AudioStories> allSampleData;
    ArrayList<AudioStories> ral = new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        latestList=new ArrayList<>();


        photos=new ArrayList<Photo>();






    }


    public showAudioActivity()
    {
        super();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.show_audiofiles, container, false);
        tblays=(TabLayout) rootView.findViewById(R.id.tabs);
        tblays.addTab(tblays.newTab().setText("Home"));
        tblays.addTab(tblays.newTab().setText("Blogs"));
        tblays.addTab(tblays.newTab().setText("Audio"));
        tblays.addTab(tblays.newTab().setText("Photos"));
        tblays.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {


                } else if (tab.getPosition() == 1) {
                    MainPage fragment2=new MainPage();
                    android.support.v4.app.FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame,fragment2,"home");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else if(tab.getPosition()==2)
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
        latestrc=(RecyclerView) rootView.findViewById(R.id.list_latest);
        latestAdapter=new LatestAdapter(getContext(),latestList);

        LinearLayoutManager linearLayoutManager3=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);


        latestrc.setLayoutManager(linearLayoutManager3);

        latestrc.setAdapter(latestAdapter);
        a1=new SectionAudio();
        a2=new SectionAudio();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbCat = db.getReference().child("audioCategory");
        dbLatest=db.getReference().child("published");
        audioReference=db.getReference().child("audiodb");
        photoReference=db.getReference().child("photoDb");
        dbMessage=db.getReference().child("messages");
        rc1 = (RecyclerView) rootView.findViewById(R.id.list_audiocat1);
        rc2=(RecyclerView) rootView.findViewById(R.id.list_photocat);
        //  String[] headers= new String[]{"दिल की गली से", "जनहित पर भारी"};
        allSampleData=new ArrayList<>();
        photoStories=new ArrayList<>();


        manApt = new AudioHorizontalAdapter(getContext(), allSampleData);
        photoApt=new PhotoAdapter(getContext(),photoStories);

        // manApt1=new AudioAdapter(this,ral1);
        rc1.setAdapter(manApt);
        //  rc2.setAdapter(manApt1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rc1.setLayoutManager(linearLayoutManager);
        rc2.setLayoutManager(linearLayoutManager1);
        rc2.setAdapter(photoApt);
        latestListener= dbLatest.limitToLast(10)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dp:dataSnapshot.getChildren())
                        {
                            String key= dp.getKey();

                            getLatest(key);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        // rc2.setLayoutManager(linearLayoutManager1);
        // readaudioDb();
        getSecondAudio();
        readPhotoDb();


      /*  rc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("hicheck","we clciked this");
            }
        });
        */

      /*  manApt.setOnItemClickListener(new AudioAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d("getpost",""+position);
             /*   Intent intent=new Intent(showAudioActivity.this,PlayAudioActivity.class);
               AudioStory al=ral.get(position);
               intent.putExtra("AudioObj",al);
               Log.d("checkkeysend",keysaudio.get(position));
               intent.putExtra("keysend",keysaudio.get(position));
               startActivity(intent);
               */


        //  }
        // });
        return rootView;

    }

    private void getLatest(final String key) {
       // Log.d("getlatestkey",key);
        dbMessage.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RoobaruDuniya rbd = dataSnapshot.getValue(RoobaruDuniya.class);
                RoobaruList rblist=new RoobaruList(key,rbd);
                latestList.add(rblist);

                latestAdapter.notifyDataSetChanged();

            }
            // String title=rbd.getTitle();
            // String photo=rbd.getPhoto();



              /*  final Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    int i=0;
                    public void run() {

                        Glide.with(getActivity()).load(rbd.getPhoto()).into(ivLatest);
                        tvLatest.setText(rbd.getTitle());
                        //imageView.setImageResource(imageArray[i]);
                        i++;
                        if(i>4)
                        {
                            i=0;
                        }
                        handler.postDelayed(this, 5000);  //for interval...
                    }
                };
              //  handler.postDelayed(runnable, 500); //for initial delay..
                new Thread(runnable).start();
            }
            */




            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                       // Log.d("checkphototitle",key);
                        Photo p=  postSnapshot.getValue(Photo.class);
                       // Log.d("checkphototitle",p.getTitle());
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




    private void getSecondAudio() {




        //categoryActivity

        dbCat.child("जनहित पर भारी").orderByChild("timeval").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count=0;
                if (dataSnapshot.hasChildren()) {

                    //keys.add(null);


long total=dataSnapshot.getChildrenCount();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        count+=1;


                        key = postSnapshot.getKey();
                        //  Log.d("ckk", key);

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
               // ral1.add(aus);
                allSampleData.add(aus);
                manApt.notifyDataSetChanged();




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

    private void getMessageData(final String key) {

        audioReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.d("finalkey",key);

              AudioStory au=dataSnapshot.getValue(AudioStory.class);
                AudioStories as=new AudioStories(key,au);
                ral.add(as);
                //manApt.notifyDataSetChanged();



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
    public void onDestroy()
    {
        super.onDestroy();
        if(latestListener!=null)
        {
            dbLatest.removeEventListener(latestListener);
            latestListener=null;
        }
    }
}
