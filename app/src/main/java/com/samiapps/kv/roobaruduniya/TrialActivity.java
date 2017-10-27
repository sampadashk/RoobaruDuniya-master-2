package com.samiapps.kv.roobaruduniya;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class TrialActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private FirebaseAuth mAuth;
    static Button notifCount;
    static int mNotifCount = 0;
    String notificationMessage;
    ArrayList<Notification> notificationArrList;
    ArrayList<NotificationJson> notificationJsonList;

    private static String LAST_OPENED_FRAGMENT_REF;

    private FirebaseAuth.AuthStateListener mAuthListener;
    public static String mUsername;
    public static final String ANONYMOUS = "anonymous";
    private static final int RC_SIGN_IN = 123;
    Uri photoUri;
    Toast t;
    private View navHeader;

    public static String[] activityTitles;
    private ImageView imgProfile;
    private TextView txtName, txtStatus;
    private Toolbar toolbar;
    String uname;
    String uemail;

    private Handler mHandler;
    DrawerLayout drawer;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    public static String userStatus;

    // tags used to attach the fragments
    private static final String TAG = TrialActivity.class.getName();
    private static final String TAG_HOME = "home";
    private static final String TAG_FAV = "favorites";
    private static final String TAG_DRAFTS = "drafts";
    private static final String TAG_PUBLISHED = "published";
    private static final String TAG_SENT = "sent";
    public static String CURRENT_TAG = TAG_HOME;
    public static boolean shouldLoadHomeFragOnBackPress;
    FloatingActionButton fab;
    public static boolean isEditor;
    FirebaseDatabase firebaseDtabase;
    DatabaseReference userDtabase;
    DatabaseReference dbEditor;
    NavigationView navigationView;
    MenuItem sentart;
    ActionBarDrawerToggle toggle;
    private BroadcastReceiver mReceiver;

    private JSONObject json_object;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.d(TAG, "ONCREATE 1");
        setContentView(R.layout.activity_trial);


        //postponeEnterTransition();

        mUsername = ANONYMOUS;
        userStatus = "Blogger";
        shouldLoadHomeFragOnBackPress = true;
        navItemIndex = 0;
        CURRENT_TAG = TAG_HOME;


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {


                    //  Log.d(TAG, "Signed in 3");


                    onSignedInInitialize(user.getDisplayName(), user.getEmail(), user.getPhotoUrl(), savedInstanceState);


                } else {
                    //Log.d(TAG, "Signed in 2");
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false).setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),

                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(), new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build())).setLogo(R.drawable.collagecopy)
                                    .build(), RC_SIGN_IN);
                }

            }
        };


        firebaseDtabase = FirebaseDatabase.getInstance();

        //   Logger.Level debugLevel = Logger.Level.valueOf("DEBUG");


        dbEditor = firebaseDtabase.getReference("editor");
        userDtabase = firebaseDtabase.getReference("user");
        // mAuth.addAuthStateListener(mAuthListener);
        notificationArrList = new ArrayList<>();
        notificationJsonList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        //send email to roobaru.duniya@gmail.com
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "roobaru.duniya@gmail.com";
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu navmenu = navigationView.getMenu();
        sentart = navmenu.findItem(R.id.nav_sent);


        navigationView.setNavigationItemSelectedListener(this);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtStatus = (TextView) navHeader.findViewById(R.id.user_status);

        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);


        //registering broadcast receiver
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.BADGE_COUNT_UPDATE");

        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                //extract our message from intent
                notificationMessage = intent.getStringExtra("badge_count_msg");
                try {
                    json_object = new JSONObject(intent.getExtras().getString("badge_jsondata"));
                    NotificationJson notificationJson = new NotificationJson(json_object.get("msgid").toString(), json_object.get("userid").toString());
                    //Log.d("ckjsonob", json_object.get("msgid").toString());
                    //Log.d("ckjsonobdev", json_object.get("userid").toString());
                    notificationJsonList.add(notificationJson);

                    Notification n = new Notification(notificationMessage);
                    //Log.d("getmsg", n.getMessage());
                    notificationArrList.add(n);

                    //Log.d("chknsize", "" + notificationArrList);
                    int count = intent.getIntExtra("badge_count", 0);
                    //log our message value
                    //Log.d("checknotifcount", "" + count);
                    setNotifCount(count);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        registerReceiver(ConnectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        //registering our receiver
        this.registerReceiver(mReceiver, intentFilter);


    }


    private void checkEditor() {
        // Log.d(TAG,"checkEditor 4");
        dbEditor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot di : dataSnapshot.getChildren()) {
                    if (di.getValue().equals(uemail)) {
                        //   Log.d("hey", (String) di.getValue());
                        isEditor = true;
                        userStatus = "editor";


                        activityTitles[4] = getString(R.string.editor_unpublished);
                        sentart.setTitle(R.string.editor_unpublished);
                        txtStatus.setText(R.string.editor);
                        userDtabase.child(mAuth.getCurrentUser().getUid()).child("status").setValue("editor");

                        break;

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        checkUserDb();

        //  Log.d("checkedit",""+isEditor);
        //   Log.d("checkstatus",userStatus);
    }


    private void loadNavHeader() {
        // name, website

        // loading header background image


        // Loading profile image
        txtStatus.setText(userStatus);
        if (photoUri == null) {
            String add = "firebasestorage.googleapis.com/v0/b/roobaru-duniya-86f7d.appspot.com/o/default-profilepic%2Fdefaultprof.jpg?alt=media&token=aeca7a55-05e4-4c02-938f-061624f5c8b4";
            photoUri = Uri.parse("https://" + add);


        }
        if (photoUri != null) {
            Glide.with(this).load(photoUri)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
        }
        txtName.setText(uname);


    }


    @Override
    public void onBackPressed() {
//TODO TEST 1
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        } else if (drawer.isDrawerOpen(GravityCompat.START) == false) {

        }

        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navigationView.getMenu().getItem(navItemIndex).setChecked(false);
                navItemIndex = 0;
                navigationView.getMenu().getItem(navItemIndex).setChecked(true);
                CURRENT_TAG = TAG_HOME;
                getSupportActionBar().setTitle(activityTitles[navItemIndex]);
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);


        View count = menu.findItem(R.id.badge).getActionView();
        notifCount = (Button) count.findViewById(R.id.notif_count);
        notifCount.setText(String.valueOf(mNotifCount));


        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Log.d("ckjs", json_object.get("userid").toString());


                    setNotifCount(0);
                    Intent intent1 = new Intent(getApplicationContext(), NotificationList.class);
                    intent1.putExtra("msgNotification", notificationMessage);
                    // Bundle args=new Bundle();
                    //args.putSerializable("ARRAYLIST",(Serializable)notificationArrList);
                    intent1.putExtra("passObject", notificationArrList);
                    intent1.putExtra("passdataObject", notificationJsonList);
                    intent1.putExtra("jsondetail", json_object.toString());


                    startActivity(intent1);
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
                }

                notificationArrList.clear();
                notificationJsonList.clear();


            }
        });


        //  View count = menu.findItem(R.id.badge).getActionView();
        // notifCount = (Button) count.findViewById(R.id.notif_count);
        // notifCount.setText(String.valueOf(mNotifCount));
        return super.onCreateOptionsMenu(menu);

    }

    public void setNotifCount(int count) {
        mNotifCount = count;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.searchid: {
                Intent intent = new Intent(TrialActivity.this, SearchResultsActivity.class);
                startActivity(intent);


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
                        Intent intent=new Intent(TrialActivity.this,SearchResultsActivity.class);
                        intent.setAction(Intent.ACTION_SEARCH);
                        intent.putExtra(SearchManager.QUERY,query);
                        startActivity(intent);
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

            //noinspection SimplifiableIfStatement
            case R.id.write_blog: {
                //Log.d("heyhey", "here");
                Intent intent = new Intent(this, WriteArticleActivity.class);
                startActivity(intent);
                break;

            }
            case R.id.action_settings: {
                AuthUI.getInstance().signOut(this);
                break;
            }


        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);
        if (requestcode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultcode == RESULT_OK) {
                Toast.makeText(this, R.string.signed_in, Toast.LENGTH_SHORT).show();

            } else if (resultcode == RESULT_CANCELED) {
                Toast.makeText(this, R.string.signed_out, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // Log.d(TAG,"nav selected");
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;

        } else if (id == R.id.nav_bookmarked) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_FAV;

        } else if (id == R.id.nav_draft) {
            navItemIndex = 2;
            CURRENT_TAG = TAG_DRAFTS;


        } else if (id == R.id.nav_published) {
            navItemIndex = 3;
            CURRENT_TAG = TAG_PUBLISHED;

        } else if (id == R.id.nav_sent) {
            navItemIndex = 4;
            CURRENT_TAG = TAG_SENT;

        } else if (id == R.id.nav_about_us) {
            Intent intent = new Intent(TrialActivity.this, AboutUsActivity.class);
            startActivity(intent);

        }
        else if(id==R.id.nav_privacy_policy)
        {
            Intent intent=new Intent(TrialActivity.this,PrivacyPolicy.class);
            startActivity(intent);
        }
        //TODO TEST 2

        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        loadHomeFragment();
        return true;
    }


    private void loadHomeFragment() {
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        // checkEditor();
        // loadNavHeader();
        setToolbarTitle();
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }
        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                // fragmentTransaction.addToBackStack(null);
                //fragmentTransaction.commit();
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();
        invalidateOptionsMenu();


    }



    @Override
    public void onResume() {
        super.onResume();


        //Log.d("checkmain", CURRENT_TAG);
        //  Log.d(TAG,"resume");

   /*    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            uname = user.getDisplayName();
            photoUri = user.getPhotoUrl();
            mHandler = new Handler();
            Log.d("cname", uname);
            Log.d("curi", photoUri.toString());
            loadNavHeader();
            checkEditor();
            if(isEditor)
            {
                sentart.setTitle(R.string.editor_unpublished);
                txtStatus.setText("Editor");
            }
        }
        */


        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {

            mAuth.removeAuthStateListener(mAuthListener);


        }
        //Log.d(TAG, "onStop()");


    }

    public void onDestroy() {
        super.onDestroy();
        //Log.d("TrialOndestroy", "" + userStatus);

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;

        }
        if (ConnectivityReceiver != null) {
            unregisterReceiver(ConnectivityReceiver);
            ConnectivityReceiver = null;
        }

    }

    public void onStart() {
        super.onStart();

        // Log.d(TAG,"start");


    }


    private void onSignedInInitialize(String username, String email, Uri photo, Bundle savedInstanceState) {
        try {
            mUsername = username;
            uname = username;
            photoUri = photo;
            uemail = email;


            mHandler = new Handler();

            //Log.d("cname", uname);
            //Log.d("curi", photoUri.toString());


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        String FCMToken = null;
        try {
            FCMToken = FirebaseInstanceId.getInstance().getToken();
            DatabaseReference dbToken = firebaseDtabase.getReference("FCMToken");
            dbToken.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(FCMToken);

            /** Store this token to firebase database along with user id **/
        } catch (Exception e) {
            e.printStackTrace();
        }


         checkUserDb();
        checkEditor();


        loadNavHeader();
        if (savedInstanceState == null) {
            //   navItemIndex = 0;
            //  CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

        // homeFragment = new HomeFragment();

        //getSupportFragmentManager().beginTransaction().replace(R.id.frame, homeFragment, CURRENT_TAG).commit();


    }

    private void checkUserDb() {



        userDtabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String uid = mAuth.getCurrentUser().getUid();
                    //Log.d("testui", uid);
                    if (dataSnapshot.hasChild(uid)) {
                        //Log.d("haschi", "true");
                        return;
                    } else {
                        //Log.d("haschi", "false");
                        String uphoto = mAuth.getCurrentUser().getPhotoUrl().toString();
                        User u = new User(uname, uemail, null, TrialActivity.userStatus, uphoto);
                        userDtabase.child(uid).setValue(u);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.d("cker", databaseError.toString());

            }
        });

    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
        userStatus = "Blogger";


    }

    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }

    private Fragment getHomeFragment() {
        //Log.d(TAG, "GETFrag");
        switch (navItemIndex) {
            case 0:
                // home
                MainPage homeFragment = new MainPage();
                return homeFragment;
            case 1:
                // fav
                FavFragment favFragment = new FavFragment();
                return favFragment;

            case 2:
                // draft fragment
                DraftFragment draftFragment = new DraftFragment();
                return draftFragment;
            case 3:
                // published fragment
                PublishedFragment publishedFragment = new PublishedFragment();
                return publishedFragment;

            case 4:
                // sent fragment
                SentFragment sentFragment = new SentFragment();
                return sentFragment;


            default:
                return new MainPage();
        }

    }

    private void setToolbarTitle() {
        // Log.d(TAG,"checknavIndextitle"+navItemIndex);
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    public void onPause() {
        super.onPause();
        //unregisterReceiver(mReceiver);
    }


    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

   /*     String get_Fragment = intent.getStringExtra("menuFragment");
        // Log.d("getnotif",get_Fragment);
        if (get_Fragment.equals("HomeFragment")) {
            HomeFragment hmFragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frame, hmFragment).commit();
            */
    }

    public boolean isNetworkUp() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private BroadcastReceiver ConnectivityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isNetworkUp()) {
                // Log.d("hi","show");
                t = Toast.makeText(TrialActivity.this, R.string.no_connectivity, Toast.LENGTH_SHORT);
                t.show();
            } else {

                if (t != null)
                    t.cancel();

            }
            //  snackbar.setActionTextColor(getResources().getColor(R.color.material_red_700));


        }
    };

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }









    /*
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
       toggle.onConfigurationChanged(newConfig);
    }
    */
}
