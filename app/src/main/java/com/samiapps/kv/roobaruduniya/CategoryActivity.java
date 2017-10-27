package com.samiapps.kv.roobaruduniya;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by KV on 13/8/17.
 */

public class CategoryActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDtabase;
    private DatabaseReference dbaseReference;

    private DatabaseReference publishedRef;
    private DatabaseReference categoryRef;
    Snackbar snackbar;
    LinearLayout homeLayout;
    ProgressBar mLoadingIndicator;


    TextView error;
    int msg;
    int column;


    private ImgAdapter imageAdapter;
    ArrayList<RoobaruDuniya> rubaru = new ArrayList<RoobaruDuniya>();
    ArrayList<String> keys = new ArrayList<>();


    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    String uid;
    String category;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    Log.d("categorylog","oncreate");
        setContentView(R.layout.headlinelayout);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please Log In!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent=new Intent(CategoryActivity.this,TrialActivity.class);
                            startActivity(intent);
                            //do things
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            //Toast.makeText(CategoryActivity.this,"Please Log in",Toast.LENGTH_LONG).show();
        } else {

            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            firebaseDtabase = FirebaseDatabase.getInstance();
            dbaseReference = firebaseDtabase.getReference().child("messages");
            publishedRef = firebaseDtabase.getReference("published");
            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
            mRecycleView = (RecyclerView) findViewById(R.id.editor_recycleview);
            categoryRef = firebaseDtabase.getReference("categories");

            imageAdapter = new ImgAdapter(rubaru, this);
            //StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            //sglm.setReverseLayout(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CategoryActivity.this);
            // linearLayoutManager.setReverseLayout(true);

            // linearLayoutManager.setStackFromEnd(true);

            //to display in reverse order;setreverselayout(true)


            mRecycleView.setLayoutManager(gridLayoutManager);
            mRecycleView.setItemAnimator(new DefaultItemAnimator());

            mRecycleView.setAdapter(imageAdapter);


            error = (TextView) findViewById(R.id.error);

            homeLayout = (LinearLayout) findViewById(R.id.main_container);


            Intent intent = getIntent();
            if (intent.getStringExtra("articlecat") != null) {
                category = intent.getStringExtra("articlecat");
            } else {
                category = intent.getStringExtra(RoobaruWidgetProvider.EXTRA_STRING);
            }
            // Log.d("categorycheck", category);
            setTitle(category);
            getArticleByCategory();
     /*   categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(category))
                {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    getArticleByCategory();


                }
                else
                {
                    mRecycleView.setVisibility(View.GONE);
                    error.setText("No Data");
                    error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */


            imageAdapter.setOnItemClickListener(new ImgAdapter.ClickListener() {
                @Override
                public void onItemClick(int position, View v) {

                    RoobaruDuniya item = rubaru.get(position);
                    String key = keys.get(position);
                    Intent intent = new Intent(CategoryActivity.this, ArticleDetail.class);
                    intent.putExtra("position", position);
                    intent.putExtra("keySelected", key);
                    Bundle b = new Bundle();
                    b.putSerializable(ArticleDetail.TAG, item);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });


            // mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            // mRecycleView.setHasFixedSize(true);
            mLoadingIndicator.setVisibility(View.VISIBLE);

            registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    public void onResume()
    {
        super.onResume();
     // Log.d("categorylog","onResume");

    }
    public void onStop()
    {
        super.onStop();
      // Log.d("categorylog","onStop");
    }
    public void onPause()
    {
        super.onPause();
     // Log.d("categorylog","onPause");
    }

    private void getArticleByCategory() {
        // Query q= categoryRef.child(category).orderByChild("timestamp").limitToFirst(15);


       categoryRef.child(category).orderByChild("timeval").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {

                    //keys.add(null);


                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                        String key = postSnapshot.getKey();
                    //  Log.d("ckk", key);
                        keys.add(key);
                        error.setVisibility(View.GONE);
                        mRecycleView.setVisibility(View.VISIBLE);

                        mLoadingIndicator.setVisibility(View.VISIBLE);
                        getMessageData(key);


                        // imageAdapter.notifyDataSetChanged();


                        // displayArticles(postSnapshot.getKey());


                    }
                    error.setVisibility(View.INVISIBLE);


                } else {
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    mRecycleView.setVisibility(View.GONE);
                    error.setText(R.string.no_data);
                    error.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


    }


    private void getMessageData(final String key) {
      //  Log.d("keyinmsg",key);
        dbaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Log.d("finalkey",key);

                RoobaruDuniya rbd = dataSnapshot.getValue(RoobaruDuniya.class);


                mLoadingIndicator.setVisibility(View.GONE);
               // Log.d("getcattitle",rbd.getTitle());
                rubaru.add(rbd);
                imageAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public boolean isNetworkUp() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private BroadcastReceiver MyReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isNetworkUp()) {
              //   Log.d("hi","show");
                snackbar = Snackbar.make(homeLayout,
                        getString(R.string.error_no_network),
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            } else {

                if (snackbar != null) snackbar.dismiss();
                emptyView();
            }
            //  snackbar.setActionTextColor(getResources().getColor(R.color.material_red_700));


        }
    };

    public void emptyView() {

        if (imageAdapter.getItemCount() == 0)

        {
            if (!isNetworkUp()) {
                msg = R.string.error_no_network;
            } else {
                publishedRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(category)) {

                            error.setText(R.string.wait_msg);
                        } else
                            error.setText(R.string.no_data);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            // error.setText(msg);
            error.setVisibility(View.VISIBLE);
        } else {
            error.setVisibility(View.INVISIBLE);

        }


    }


    @Override
    public void onDestroy() {
    //  Log.d("categorylog","ondestroy");
        if (MyReceiver != null) {

            unregisterReceiver(MyReceiver);
            MyReceiver = null;
        }
       rubaru.clear();
        keys.clear();
        imageAdapter.notifyDataSetChanged();



        //imageAdapter.notifyDataSetChanged();


        super.onDestroy();
    }

}
