package com.samiapps.kv.roobaruduniya;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KV on 21/7/17.
 */

public class NotificationList extends AppCompatActivity {
    RecyclerView notificationRecyclerView;
    ArrayList<Notification> notificationArrayList;
    ArrayList<NotificationJson> jsonArrayList;
    NotificationAdapter notifiadapter;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_list);
        notificationRecyclerView = (RecyclerView) findViewById(R.id.notificationList);
        Intent i = getIntent();
        String msg = i.getStringExtra("msgNotification");

        JSONObject resjson = null;
        try {
            resjson = new JSONObject(i.getStringExtra("jsondetail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //JSONObject resjson= (JSONObject) i.getSerializableExtra("jsondetail");
        try {
            // Log.d("checkvjson1",resjson.get("msgid").toString());
            String msgid = resjson.get("msgid").toString();
            String userid = resjson.get("userid").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        notificationArrayList = (ArrayList<Notification>) i.getSerializableExtra("passObject");
        jsonArrayList = (ArrayList<NotificationJson>) i.getSerializableExtra("passdataObject");
        //i.getSerializableExtra("passObject");
        // Notification notification=new Notification(msg);
        //notificationArrayList.add(notification);
        // Log.d("sizechk",""+notificationArrayList.size());
        // notifiadapter.notifyDataSetChanged();


        //TODO: add msg in arraylist

        notifiadapter = new NotificationAdapter(this, notificationArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        SeparatorDecoration decoration = new SeparatorDecoration(this, Color.GRAY, 1.5f);
        notificationRecyclerView.addItemDecoration(decoration);
        //SpacesItemDecoration s=new SpacesItemDecoration(1);
        // notificationRecyclerView.addItemDecoration(s);
        notificationRecyclerView.setLayoutManager(layoutManager);
        notificationRecyclerView.setItemAnimator(new DefaultItemAnimator());


        notificationRecyclerView.setAdapter(notifiadapter);
        notifiadapter.setOnItemClickListener(new NotificationAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                NotificationJson nj = jsonArrayList.get(position);
                Intent intent = new Intent(NotificationList.this, ArticleDetail.class);
                intent.putExtra("intentNotification", "NotificationDetail");
                intent.putExtra("NotificationObject", nj);
                // Log.d("chknj",nj.getMsg_id());

                startActivity(intent);


            }
        });
    }

    public void onStart() {
        super.onStart();
    }

    public void onDestroy() {
        super.onDestroy();
        notificationArrayList.clear();
        jsonArrayList.clear();
    }

}
