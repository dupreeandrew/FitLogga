package com.fitlogga.app.models.plan;

import android.content.Context;

import com.fitlogga.app.models.ApplicationContext;

import java.io.File;

public class PlanIOUtils {

    public static String getIOSafeFileID(String planName) {
        return planName.replace(' ', '_');
    }

    public static File getSharedPrefFile(String planName) {
        Context context = ApplicationContext.getInstance();
        String preferenceName = getIOSafeFileID(planName);
        return new File("/data/data/" + context.getPackageName() + "/shared_prefs/"
                + preferenceName + ".xml");
    }

}
