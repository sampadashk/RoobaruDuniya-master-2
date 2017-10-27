package com.samiapps.kv.roobaruduniya;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.samiapps.kv.roobaruduniya.TrialActivity.activityTitles;
import static com.samiapps.kv.roobaruduniya.TrialActivity.navItemIndex;

/**
 * Created by KV on 2/7/17.
 */

public class FavFragment extends Fragment {
    private FirebaseDatabase firebaseDbase;
    private DatabaseReference dReference;
    private DatabaseReference mReference;
    private ImgAdapter imageAdapter;
    ArrayList<RoobaruDuniya> rubaru = new ArrayList<RoobaruDuniya>();
    ArrayList<String> kList;
    Cursor c;


    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    String uid;
    FavDb favRef;
    SQLiteDatabase db;
    ArrayList<String> childkey;
    public static final String TAG = FavFragment.class.getName();
    // ProgressBar pgbar;
    // TextView noDraftText;

    public FavFragment() {
        super();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Log.d(TAG,"favonCreate");
        // for retaining fragment;like on screen rotation want same screen to be displayed
        setRetainInstance(true);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDbase = FirebaseDatabase.getInstance();

        // dbaseReference = firebaseDtabase.getReference().child("user").child(uid);
        mReference = firebaseDbase.getReference().child("messages");
        kList = new ArrayList<>();
        favRef = new FavDb(getContext());
        db = favRef.getReadableDatabase();


    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(activityTitles[navItemIndex]);
        if (savedInstanceState != null) {

        } else {
            c = favRef.getKey(db);
            computeCursor();
        }

    }

    private void computeCursor() {


        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {


                String key = c.getString(c.getColumnIndex(RoobaruContract.COLUMN_KEY));
                displayFav(key);
                kList.add(key);
                c.moveToNext();
            }

        }
    }

    private void displayFav(String key) {
        mReference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RoobaruDuniya rbd = dataSnapshot.getValue(RoobaruDuniya.class);
                //   Log.d("titleck", rbd.getTitle());

                rubaru.add(rbd);
                imageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //setHasOptionsMenu(true);


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.headlinelayout, container, false);
        //  Log.d(TAG,"favonCreateView");
        mRecycleView = (RecyclerView) rootView.findViewById(R.id.editor_recycleview);
        imageAdapter = new ImgAdapter(rubaru, getContext());
        mRecycleView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setAdapter(imageAdapter);


        imageAdapter.setOnItemClickListener(new ImgAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                RoobaruDuniya item = rubaru.get(position);
                String key = kList.get(position);
                Intent intent = new Intent(getContext(), ArticleDetail.class);
                intent.putExtra("position", position);
                intent.putExtra("keySelected", key);
                Bundle b = new Bundle();
                b.putSerializable(ArticleDetail.TAG, item);
                intent.putExtras(b);


                //intent.putExtra("article",rd);

                startActivity(intent);
            }
        });


        return rootView;
    }


    @Override
    public void onStop() {
        super.onStop();

        // Log.d(TAG,"favstop");
        // rubaru.clear();

    }

    @Override
    public void onPause() {
        super.onPause();
        // Log.d(TAG,"favpause");
        // rubaru.clear();

    }

    public void onResume() {
        super.onResume();

        //  Log.d(TAG,"favresume");
        // rubaru.clear();


    }

    public void onDestroy() {
        super.onDestroy();

        // Log.d(TAG,"favdestroy");
        // rubaru.clear();


    }


}
