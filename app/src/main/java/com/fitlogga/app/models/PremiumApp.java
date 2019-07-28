package com.fitlogga.app.models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class is used to edit the device's premium status.
 * Premium Status costs money, and unlocks the full app.
 */
public class PremiumApp {

    /*
    There is no meaning behind this value. It's just to help prevent rooted users from opening
    up their shared-preferences folder, and easily enable PremiumApp through a boolean value.
    The value is completely made up to scare users.

    One note is, NEVER change this value, unless you have a way for users who already have premium
    enabled to migrate over to a different value/system that indicates premium status.
     */
    private static final String SECRET_PREMIUM_VALUE =
            "WARNING, ENSURE YOU KNOW WHAT YOU ARE DOING. EDITING SYSTEM FILES IS DISABLED BY " +
                    "DEFAULT, AND CAN SEVERELY DAMAGE YOUR DEVICE. THIS IS YOUR ONLY WARNING.";
    private static final String SECRET_PREMIUM_KEY = "enabled-string";

    private static SharedPreferences getSharedPreferences() {
        String prefName = "pa-shared-pref-file";
        Context context = ApplicationContext.getInstance();
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public static void setEnabled(boolean enabled) {
        SharedPreferences pref = getSharedPreferences();

        SharedPreferences.Editor editor = pref.edit();
        if (enabled) {
            editor.putString(SECRET_PREMIUM_KEY, SECRET_PREMIUM_VALUE);
        }
        else {
            editor.putString(SECRET_PREMIUM_KEY, "");
        }
        editor.apply();
    }

    public static boolean isEnabled() {
        SharedPreferences pref = getSharedPreferences();
        String value = pref.getString(SECRET_PREMIUM_KEY, "");
        return SECRET_PREMIUM_VALUE.equals(value);
    }

}
