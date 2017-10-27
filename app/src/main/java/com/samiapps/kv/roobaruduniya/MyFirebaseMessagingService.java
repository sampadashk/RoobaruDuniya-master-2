package com.samiapps.kv.roobaruduniya;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by KV on 18/7/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String notificatication_type = remoteMessage.getData().get("title");
        String msgTitle = remoteMessage.getData().get("body");
        String user = remoteMessage.getData().get("userName");
        String notification_body;
        String notification_title;

        if (user != null) {
            notification_body = "New " + notificatication_type + " on your article " + msgTitle + " by " + user;
            notification_title = "New" + notificatication_type;


        } else {
            notification_body = notificatication_type + " " + msgTitle;
            notification_title = "New Article";
        }


       // Log.d("notification_body", notification_body);
        //TODO new article notification
        // String click_action = remoteMessage.getNotification().getClickAction();
        String mid = remoteMessage.getData().get("msgid");
        String uid = remoteMessage.getData().get("userid");
        Map<String, String> params = new HashMap<String, String>();
        //params = remoteMessage.getData().get("msgid"),remoteMessage.getData().get("userid");
        params.put("msgid", mid);
        params.put("userid", uid);
        JSONObject object = new JSONObject(params);
      //  Log.d("JSON_OBJECT", object.toString());


        int countNo = TrialActivity.mNotifCount;
        countNo += 1;
        setBadge(getApplicationContext(), countNo, notification_body, object);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notification_title)
                        .setContentText(notification_body)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this, ArticleDetail.class);
        resultIntent.setAction(Long.toString(System.currentTimeMillis()));
        // Log.d("getAction",click_action);
        resultIntent.putExtra("bkgnotification", object.toString());
        //  resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

// Because clicking the notification opens a new ("special") activity, there's
// no need to create an artificial back stack.
        int mNotificationId = (int) System.currentTimeMillis();
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(resultPendingIntent);


// Sets an ID for the notification


// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.

        // FirebaseDatabase db=FirebaseDatabase.getInstance();
        // DatabaseReference dbref=db.getReference("notification");
        //dbref.removeValue();


    }

    public static void setBadge(Context applicationContext, int countNo, String msg, JSONObject ob) {
        String launcherClassName = getLauncherClassName(applicationContext);
        if (launcherClassName == null) {
            Log.e("classname", "null");
            return;
        }
        Log.d("notifbd","caleed");
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", countNo);
        intent.putExtra("badge_count_package_name", applicationContext.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        intent.putExtra("badge_count_msg", msg);
        intent.putExtra("badge_jsondata", ob.toString());


      //  Log.d("ckjso", ob.toString());
        applicationContext.sendBroadcast(intent);

    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                //  Log.d("classNme",className);
                return className;
            }
        }
        return null;
    }
}
