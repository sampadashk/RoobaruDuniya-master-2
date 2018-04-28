package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KV on 12/8/17.
 */

public class MainPage extends Fragment {

    RecyclerView mainrecycler;
    ImageView ivLatest;
    TextView tvLatest;
    ArrayList<MainDisplay> al = new ArrayList<>();
    FirebaseDatabase fbd;
    DatabaseReference dbLatest;
    DatabaseReference dbMessage;
    ArrayList<String> keysLatestList;
    RecyclerView latestRecyclerView;
    List<RoobaruList> latestList;

    ValueEventListener latestListener;
    Button bu;
    TabLayout tblays;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fbd=FirebaseDatabase.getInstance();
        dbLatest=fbd.getReference().child("published");
        dbMessage=fbd.getReference().child("messages");

        keysLatestList=new ArrayList<>();
        latestList=new ArrayList<>();



    }

    public MainPage() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_page, container, false);
        ivLatest=(ImageView) rootView.findViewById(R.id.latest_img);
        tvLatest=(TextView) rootView.findViewById(R.id.latest_title);
        tblays=(TabLayout) rootView.findViewById(R.id.tabs);
        tblays.addTab(tblays.newTab().setText("Home"));
        tblays.addTab(tblays.newTab().setText("Blogs"));
        tblays.addTab(tblays.newTab().setText("Audio"));
        tblays.addTab(tblays.newTab().setText("Photos"));

        mainrecycler = (RecyclerView) rootView.findViewById(R.id.main_recycler);

        TabLayout.Tab tab = tblays.getTabAt(1);
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
       /* bu=(Button) rootView.findViewById(R.id.play_audio);
        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intio=new Intent(getActivity(),PlayAudioActivity.class);
                startActivity(intio);
            }
        });
*/

        // ArrayList<MainDisplay> al=new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.drawimage);
        // MainDisplay dp=new MainDisplay(getString(R.string.cat1),getResources().getResourceEntryName(R.drawable.spotlight));
        // al.add(dp);
        // MainDisplay dp=new MainDisplay(getString(R.string.cat1),R.drawable.spotlight);
        for (int i = 0; i < 14; i++) {
         //   Log.d("ckarr", "" + getResources().getIntArray(R.array.drawimage)[0]);

            MainDisplay dp = new MainDisplay((getResources().getStringArray(R.array.catgs)[i]), imgs.getResourceId(i, -1));
            al.add(dp);
        }


      /*  latestAdapter.setOnItemClickListener(new LatestAdapter.LatestListener() {
            @Override
            public void onItemClick(int position, View v) {

               // RoobaruDuniya item = rubaru.get(position);
                String key = keysLatestList.get(position);
                Intent intent = new Intent(getActivity(), ArticleDetail.class);
               // intent.putExtra("position", position);
                intent.putExtra("keySelected", key);
               // Bundle b = new Bundle();
              //  b.putSerializable(ArticleDetail.TAG, item);
              //  intent.putExtras(b);
                startActivity(intent);
            }




        });
        */

        MainAdapter mainAdapter = new MainAdapter(getContext(), al);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mainrecycler.setLayoutManager(gridLayoutManager);
        mainrecycler.setAdapter(mainAdapter);
        mainrecycler.setItemAnimator(new DefaultItemAnimator());
        mainAdapter.setOnItemClickListener(new MainAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                MainDisplay md = al.get(position);
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra("articlecat", md.getText());
                startActivity(intent);


            }
        });



        return rootView;
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

