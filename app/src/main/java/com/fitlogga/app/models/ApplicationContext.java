package com.fitlogga.app.models;

import android.annotation.SuppressLint;
import android.content.Context;

public class ApplicationContext {

    @SuppressLint("StaticFieldLeak")
    // ApplicationContext is a singleton by itself. There is no memory leak.
    private static Context context = null;

    /**
     * Loaded from SplashActivity.
     */
    public static void init(Context context) {
        if (ApplicationContext.context == null) {
            ApplicationContext.context = context;
        }
    }

    public static Context getInstance() {
        return context;
    }
}
