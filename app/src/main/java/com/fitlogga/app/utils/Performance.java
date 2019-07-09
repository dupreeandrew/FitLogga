package com.fitlogga.app.utils;

import android.util.Log;

public class Performance {

    private static final String TAG = "PerformanceTimeStamp";

    public static void printTimestamp() {
        Log.d(TAG, String.valueOf(System.currentTimeMillis()));
    }
}
