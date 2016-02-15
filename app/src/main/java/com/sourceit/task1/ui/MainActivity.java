package com.sourceit.task1.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sourceit.task1.R;
import com.sourceit.task1.model.BroadcasterModel;
import com.sourceit.task1.utils.L;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView broadcasters;
    private int ON_AIR = 1;

    private ArrayList<BroadcasterModel> broadcasterModels;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        L.d("start activity");

        broadcasters = (RecyclerView) findViewById(R.id.broadcasters_list);
        broadcasterModels = new ArrayList<>();

        LocationManager managerGps = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        L.d("gps :" + managerGps.isProviderEnabled(LocationManager.GPS_PROVIDER));
        if (managerGps.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            States.states[States.GPS] = States.ON;
        } else {
            States.states[States.GPS] = States.OFF;
        }

        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        L.d("bluetooth : " + bluetooth.isEnabled());
        if (bluetooth.isEnabled()) {
            States.states[States.BLUETOOTH] = States.ON;
        } else {
            States.states[States.BLUETOOTH] = States.OFF;
        }

        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        L.d("wifi: " + wifi.isWifiEnabled());
        if (wifi.isWifiEnabled()) {
            States.states[States.WI_FI] = States.ON;
        } else {
            States.states[States.WI_FI] = States.OFF;
        }

        if (android.provider.Settings.System.getInt(
                getContentResolver(),
                android.provider.Settings.Global.AIRPLANE_MODE_ON, 0) == ON_AIR) {
            States.states[States.AIR_MODE] = States.ON;
            sendMessage();
        } else {
            States.states[States.AIR_MODE] = States.OFF;
        }

        BroadcasterModel broadcasterModel_wifi = new BroadcasterModel(States.WIFI_STRING, States.states[States.WI_FI]);
        BroadcasterModel broadcasterModel_bluetooth = new BroadcasterModel(States.BLUETOOTH_STRING, States.states[States.BLUETOOTH]);
        BroadcasterModel broadcasterModel_gps = new BroadcasterModel(States.GPS_STRING, States.states[States.GPS]);
        BroadcasterModel broadcasterModel_airmode = new BroadcasterModel(States.AIR_STRING, States.states[States.AIR_MODE]);
        broadcasterModels.add(broadcasterModel_wifi);
        broadcasterModels.add(broadcasterModel_bluetooth);
        broadcasterModels.add(broadcasterModel_gps);
        broadcasterModels.add(broadcasterModel_airmode);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        broadcasters.setLayoutManager(layoutManager);
        broadcasters.setAdapter(new MyAdapter(broadcasterModels));
    }

    public void sendMessage() {
        Intent i = new Intent();
        i.setAction("my.custom.INTENT");
        sendBroadcast(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        L.d("paused");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(localReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.d("resume");
        IntentFilter iff = new IntentFilter();
        iff.addAction(States.AIR_STRING);
        iff.addAction(States.WIFI_STRING);
        iff.addAction(States.GPS_STRING);
        iff.addAction(States.BLUETOOTH_STRING);
        LocalBroadcastManager.getInstance(this).registerReceiver(localReceiver, iff);
        broadcasters.getAdapter().notifyDataSetChanged();
    }

    void changeSet(int key, boolean value) {
        broadcasterModels.get(key).setState(value);
        broadcasters.getAdapter().notifyDataSetChanged();
    }

    private BroadcastReceiver localReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            L.d("local receive");
            if (intent.getAction().equals(States.AIR_STRING)) {
                L.d("air mode");
                changeSet(States.AIR_MODE, States.states[States.AIR_MODE]);
            } else if (intent.getAction().equals(States.WIFI_STRING)) {
                changeSet(States.WI_FI, States.states[States.WI_FI]);
            } else if (intent.getAction().equals(States.BLUETOOTH_STRING)) {
                changeSet(States.BLUETOOTH, States.states[States.BLUETOOTH]);
            } else if (intent.getAction().equals(States.GPS_STRING)) {
                changeSet(States.GPS, States.states[States.GPS]);
            }
        }
    };
}