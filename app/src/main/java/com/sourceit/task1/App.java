package com.sourceit.task1;

import android.app.Application;

/**
 * Created by User on 12.02.2016.
 */
public class App extends Application {
    private static App instance;

    public static App getApp() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
