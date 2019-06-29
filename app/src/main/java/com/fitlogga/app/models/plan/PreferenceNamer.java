package com.fitlogga.app.models.plan;

public class PreferenceNamer {
    public static String fromPlanName(String planName) {
        return planName.replace(' ', '_');
    }
}
