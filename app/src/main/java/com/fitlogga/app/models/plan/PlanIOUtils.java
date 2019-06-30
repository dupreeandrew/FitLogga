package com.fitlogga.app.models.plan;

class PlanIOUtils {

    static String getIOSafeFileID(String planName) {
        return planName.replace(' ', '_');
    }

}
