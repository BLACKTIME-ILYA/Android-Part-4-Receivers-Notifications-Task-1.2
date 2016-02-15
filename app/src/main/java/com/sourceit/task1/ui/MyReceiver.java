package com.sourceit.task1.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;

import com.sourceit.task1.R;
import com.sourceit.task1.utils.L;

/**
 * Created by User on 12.02.2016.
 */
public class MyReceiver extends BroadcastReceiver {

    private final int COUNT_ON1 = 1;
    private final int COUNT_ON2 = 2;
    private final int COUNT_ON3 = 3;
    private final int COUNT_ON4 = 4;

    private final int ID = 1;
    private final long[] DOT = new long[200];

    private Intent i = new Intent();
    private Intent localIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        L.d("onReceive " + intent.getAction());

        i.setAction("my.custom.INTENT");

        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                ID, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        if (intent.getAction().equals("android.intent.action.AIRPLANE_MODE")) {
            if (intent.getBooleanExtra("state", false) == false) {
                L.d("onReceive airplane false");
                nm.cancel(ID);
                States.states[States.AIR_MODE] = States.OFF;
            } else if (intent.getBooleanExtra("state", false) == true) {
                L.d("onReceive airplane true");
                States.states[States.AIR_MODE] = States.ON;
            }
            localIntent = new Intent(States.AIR_STRING);
            LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);

        } else if (intent.getAction().equals("android.net.wifi.WIFI_STATE_CHANGED")) {
            L.d("Wi-Fi state ");
            if (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0) == WifiManager.WIFI_STATE_DISABLED) {
                L.d("wi-fi false");
                States.states[States.WI_FI] = States.OFF;
                if (States.states[States.AIR_MODE]) {
                    context.sendBroadcast(i);
                }
            } else if (intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0) == WifiManager.WIFI_STATE_ENABLED) {
                L.d("wi-fi true");
                States.states[States.WI_FI] = States.ON;
                context.sendBroadcast(i);
            }
            localIntent = new Intent(States.WIFI_STRING);
            LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);

        } else if (intent.getAction().equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
            L.d("bluetooth");
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0) == BluetoothAdapter.STATE_ON) {
                L.d("bluetooth on");
                States.states[States.BLUETOOTH] = States.ON;
                context.sendBroadcast(i);
            } else if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0) == BluetoothAdapter.STATE_OFF) {
                L.d("bluetooth off");
                States.states[States.BLUETOOTH] = States.OFF;
                context.sendBroadcast(i);
            }
            localIntent = new Intent(States.BLUETOOTH_STRING);
            LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);

        } else if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {
            LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            L.d("gps" + manager.isProviderEnabled(LocationManager.GPS_PROVIDER));
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                L.d("gps on");
                States.states[States.GPS] = States.ON;
                context.sendBroadcast(i);
            } else if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (States.states[States.GPS]) {
                    L.d("gps off");
                    States.states[States.GPS] = States.OFF;
                    context.sendBroadcast(i);
                }
            }
            localIntent = new Intent(States.GPS_STRING);
            LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);

        } else if (intent.getAction().equals("my.custom.INTENT")) {
            L.d("my custom intent");
            int tempCountOn = 0;

            if (States.states[States.AIR_MODE]) {
                for (boolean state : States.states) {
                    if (state) {
                        tempCountOn++;
                    }
                }
                L.d("countOn = " + tempCountOn);

                if (tempCountOn == COUNT_ON1) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentIntent(contentIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setTicker("Большой брат следит за тобой")
                            .setContentText("Большой брат следит за тобой")
                            .setLights(Color.BLUE, 500, 500);
                    Notification n = builder.build();
                    nm.notify(ID, n);
                } else if (tempCountOn == COUNT_ON2) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentIntent(contentIntent)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setTicker("Большой брат присматривает за тобой")
                            .setContentText("Большой брат присматривает за тобой")
                            .setVibrate(DOT);
                    Notification n = builder.build();
                    nm.notify(ID, n);
                } else if (tempCountOn == COUNT_ON3) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentIntent(contentIntent)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setTicker("Большой брат смотрит за тобой")
                            .setContentText("Большой брат смотрит за тобой");
                    Notification n = builder.build();
                    nm.notify(ID, n);
                } else if (tempCountOn == COUNT_ON4) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setContentIntent(contentIntent)
                            .setSmallIcon(R.drawable.ic_change_history_black_24dp)
                            .setTicker("Большой брат наблюдает за тобой")
                            .setContentText("Большой брат наблюдает за тобой");
                    Notification n = builder.build();
                    nm.notify(ID, n);
                }
            }
        }
    }
}


