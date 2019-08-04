package com.fitlogga.app.models;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class ApplicationContext extends Application {

    @SuppressLint("StaticFieldLeak")
    // ApplicationContext is a singleton by itself. There is no memory leak.
    private static Context context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationContext.context = getApplicationContext();
    }

    public static Context getInstance() {
        return context;
    }
}
