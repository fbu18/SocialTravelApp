package me.vivh.socialtravelapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class CustomReceiver extends BroadcastReceiver {
    private static final String TAG = "CustomReceiver";
    public static final String intentAction = "com.parse.push.intent.RECEIVE";


    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d("Receiver", intent.toString());
//        String tripObjectId = intent.getStringExtra("trip");
//        createNotification(context, R.drawable.ic_sms_black_24dp, "SocialTravelApp", "New Message", tripObjectId);
        processPush(context, intent);
    }

    private void processPush(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(intentAction)) {
            String channel = intent.getExtras().getString("com.parse.Channel");
            try {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
                // Iterate the parse keys if needed
                Iterator<String> itr = json.keys();
                String tripId = "";
                String message = "New Message";
                String sender = "SocialTravelApp";
                while (itr.hasNext()) {
                    String key = (String) itr.next();
//                    String value = json.getString(key);
                    // Extract custom push data
                    if (key.equals("customData")) {
                        tripId = json.getString(key);
                    }else if(key.equals("message")){
                        message = json.getString("message");
                    }else if(key.equals("sender")){
                        sender = json.getString("sender");
                    }
                }

                createNotification(context, R.drawable.ic_sms_black_24dp, sender, message, tripId);
            } catch (JSONException ex) {
                Log.d(TAG, "JSON failed!");
            }
        }
    }

    public static final int NOTIFICATION_ID = 45;
    public static final String CHANNEL_ID = "my_channel_01";
    // Create a local dashboard notification to tell user about the event
    private void createNotification(Context context, int iconRes, String title, String body, String objectId) {
        // Create an explicit intent for MainActivity of app
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("tripId", objectId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(iconRes)
                .setContentTitle(title)
                .setContentText(body)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "General Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
        }
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
