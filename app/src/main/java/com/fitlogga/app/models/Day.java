package com.fitlogga.app.models;

import android.content.Context;
import android.content.res.Resources;

import com.fitlogga.app.R;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.GregorianCalendar;

public enum Day {

    @SerializedName("0")
    SUNDAY(0),
    @SerializedName("1")
    MONDAY(1),
    @SerializedName("2")
    TUESDAY(2),
    @SerializedName("3")
    WEDNESDAY(3),
    @SerializedName("4")
    THURSDAY(4),
    @SerializedName("5")
    FRIDAY(5),
    @SerializedName("6")
    SATURDAY(6);

    private int value;

    Day(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    /**
     * Provides a locale-safe string representation of a day.
     */
    public static String getStringRepresentation(Context context, Day day) {
        Resources resources = context.getResources();
        switch (day.getValue()) {
            case 0:
                return resources.getString(R.string.global_sunday);
            case 1:
                return resources.getString(R.string.global_monday);
            case 2:
                return resources.getString(R.string.global_tuesday);
            case 3:
                return resources.getString(R.string.global_wednesday);
            case 4:
                return resources.getString(R.string.global_thursday);
            case 5:
                return resources.getString(R.string.global_friday);
            case 6:
                return resources.getString(R.string.global_saturday);
            default:
                throw new IllegalArgumentException("Invalid day received");
        }
    }

    public String getDayAbbrev() {
        Resources resources = ApplicationContext.getInstance().getResources();
        switch (value) {
            case 0:
                return resources.getString(R.string.tab_sunday_abbreviation);
            case 1:
                return resources.getString(R.string.tab_monday_abbreviation);
            case 2:
                return resources.getString(R.string.tab_tuesday_abbreviation);
            case 3:
                return resources.getString(R.string.tab_wednesday_abbreviation);
            case 4:
                return resources.getString(R.string.tab_thursday_abbreviation);
            case 5:
                return resources.getString(R.string.tab_friday_abbreviation);
            case 6:
                return resources.getString(R.string.tab_saturday_abbreviation);
        }

        throw new NullPointerException();
    }

    public static Day fromValue(int value) {

        for (Day day : Day.values()) {
            if (value == day.getValue()) {
                return day;
            }
        }

        throw new IllegalArgumentException();
    }

    public static Day getToday() {
        GregorianCalendar calendar = new GregorianCalendar();
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        return Day.values()[dayNum - 1];
    }


}
