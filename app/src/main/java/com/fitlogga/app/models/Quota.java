package com.fitlogga.app.models;

import android.content.Context;
import android.content.SharedPreferences;

public class Quota {

    private SharedPreferences sharedPreferences;
    private final String KEY_TIMESTAMP;
    private final String KEY_USES;

    public static Quota get(String name) {
        return new Quota(name);
    }

    private Quota(String prefix) {
        this.KEY_TIMESTAMP = prefix + "_timestamp";
        this.KEY_USES = prefix + "_uses";

        Context context = ApplicationContext.getInstance();
        this.sharedPreferences = context.getSharedPreferences("quotas", Context.MODE_PRIVATE);
    }

    public int addUse() {
        int newNumUses = getNumUses() + 1;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USES, newNumUses);
        editor.apply();
        return newNumUses;
    }

    public int getNumUses() {
        return sharedPreferences.getInt(KEY_USES, 0);
    }

    public void resetQuota() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_TIMESTAMP, System.currentTimeMillis());
        editor.putInt(KEY_USES, 0);
        editor.apply();
    }

    public long getMillisSinceLastReset() {
        long lastResetTimestamp = sharedPreferences
                .getLong(KEY_TIMESTAMP, 0);
        return System.currentTimeMillis() - lastResetTimestamp;
    }

}
