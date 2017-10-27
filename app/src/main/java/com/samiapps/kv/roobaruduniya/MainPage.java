package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
    ArrayList<RoobaruDuniya> latestList;
    LatestAdapter latestAdapter;


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
        mainrecycler = (RecyclerView) rootView.findViewById(R.id.main_recycler);
        latestRecyclerView=(RecyclerView) rootView.findViewById(R.id.latest_recycler);
        dbLatest.limitToLast(5).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dp:dataSnapshot.getChildren())
                {
                    String key= dp.getKey();
                    keysLatestList.add(key);
                    getLatest(key);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        latestAdapter=new LatestAdapter(getContext(),latestList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true);
        latestRecyclerView.setLayoutManager(linearLayoutManager);
        latestRecyclerView.setAdapter(latestAdapter);

        latestAdapter.setOnItemClickListener(new LatestAdapter.LatestListener() {
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

    private void getLatest(String key) {
        Log.d("getlatestkey",key);
        dbMessage.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RoobaruDuniya rbd = dataSnapshot.getValue(RoobaruDuniya.class);
                latestList.add(rbd);
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
}
