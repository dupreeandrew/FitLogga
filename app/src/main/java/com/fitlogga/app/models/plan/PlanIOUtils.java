package com.fitlogga.app.models.plan;

public class PlanIOUtils {

    public static String getIOSafeFileID(String planName) {
        return planName.replace(' ', '_');
    }

}
