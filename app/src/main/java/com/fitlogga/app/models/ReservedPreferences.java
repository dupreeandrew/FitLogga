package com.fitlogga.app.models;

import java.util.HashSet;
import java.util.Set;

public class ReservedPreferences {
    private static Set<String> reservedPreferences = new HashSet<>();

    static {
        reservedPreferences.add("quotas");
        reservedPreferences.add("pa-shared-pref-file");
        reservedPreferences.add("registered_plans");
    }

    public static boolean contains(String prefName) {
        return reservedPreferences.contains(prefName);
    }

}
