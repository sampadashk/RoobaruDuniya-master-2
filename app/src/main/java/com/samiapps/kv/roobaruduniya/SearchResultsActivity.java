package com.samiapps.kv.roobaruduniya;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by KV on 21/8/17.
 */

public class SearchResultsActivity extends AppCompatActivity {

    FirebaseDatabase dbfb;
    DatabaseReference dbsearch;
    DatabaseReference userReference;
    TextView noResTextView;

    String query;
    ListView lvw;
    TabLayout tabLayout;
    ArrayList<String> list;
    ArrayList<String> klist;
    boolean isAuthor;
    private ArrayAdapter<String> listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_search);

        dbfb = FirebaseDatabase.getInstance();
        dbsearch = dbfb.getReference("publishedTitle");
        userReference = dbfb.getReference("user");
        lvw = (ListView) findViewById(R.id.listview);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        noResTextView = (TextView) findViewById(R.id.noResult);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.search_title));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.search_author));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
               // Log.d("ckp", "" + i);
                if (i == 1) {
                    isAuthor = true;
                } else
                    isAuthor = false;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        klist = new ArrayList<String>();


        list = new ArrayList<String>();
        listAdapter = new ArrayAdapter<String>(this, R.layout.searchtext, list);
       // Log.d("heyhere", "here");
        lvw.setAdapter(listAdapter);
        lvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isAuthor) {
                    String key = klist.get(position);
                    Intent inten = new Intent(SearchResultsActivity.this, ArticleDetail.class);
                    inten.putExtra("searchString", key);
                    startActivity(inten);
                } else {
                    String userId = klist.get(position);
                    Intent inten = new Intent(SearchResultsActivity.this, Profile.class);
                    inten.putExtra("senuid", userId);
                    startActivity(inten);
                }
            }


        });


        // handleIntent(getIntent());

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.searchi).setChecked(true);
        menu.findItem(R.id.searchi).setEnabled(true);
        //Also you can do this for sub menu

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.searchi);
        SearchView searchView =
                (SearchView) searchItem.getActionView();
        //for Implement Search Bar on Activity Load


        searchItem.expandActionView();
       // Log.d("checksearc", searchView.getQuery().toString());


        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                if (isAuthor) {
                    queryAuthor(query);
                } else {
                  //  Log.d("here", "we");
                    list.clear();
                    listAdapter.clear();
                    klist.clear();
                    //   dbsearch.orderByValue("title").addChildEventListener(new ChildEventListener() {
                    dbsearch.orderByValue().addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                           // Log.d("check", dataSnapshot.getValue().toString());


                            String st = dataSnapshot.getValue().toString();
                            if (st.toLowerCase().contains(query.toLowerCase())) {

                                noResTextView.setVisibility(View.INVISIBLE);

                                klist.add(dataSnapshot.getKey());
                                list.add(st);
                                listAdapter.notifyDataSetChanged();
                            }


                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });


                }
                if (list.isEmpty()) {
                    noResTextView.setVisibility(View.VISIBLE);
                }


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void queryAuthor(final String query) {
        list.clear();
        klist.clear();
        listAdapter.clear();
        userReference.orderByChild("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    String nm = dataSnapshot.child("name").getValue().toString();
                    //  Log.d("checknm",nm);
                    if (nm.toLowerCase().contains(query.toLowerCase())) {


                        list.add(nm);
                        listAdapter.notifyDataSetChanged();
                        klist.add(dataSnapshot.getKey());
                        noResTextView.setVisibility(View.GONE);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.searchi: {

             /*  SearchManager searchManager =
                        (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView =
                        (SearchView) item.getActionView();
                Log.d("checksearc",searchView.getQuery().toString());


                searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.d("here","we");
                        dbmsg.orderByChild("title").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Log.d("check",dataSnapshot.child("title").getValue().toString());
                                String st=dataSnapshot.child("title").getValue().toString();
                                list.add(st);
                                listAdapter.notifyDataSetChanged();


                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        return false;
                    }
                });
                */


                break;
            }


        }
        return super.onOptionsItemSelected(item);
    }

/*

    @Override


        public void onNewIntent(Intent intent) {
            setIntent(intent);
            handleIntent(intent);

    }



    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
             query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("getquery",query);
            dbmsg.orderByChild("title").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.d("check",dataSnapshot.child("title").getValue().toString());
                    String st=dataSnapshot.child("title").getValue().toString();
                    list.add(st);
                    listAdapter.notifyDataSetChanged();


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            doSearch(query);
            //use the query to menu_search your data somehow
        }
    }
    private void doSearch(String queryStr) {
        // get a Cursor, prepare the ListAdapter
        tv.setText(queryStr);
        // and set it
    }
    */
}
