package com.fitlogga.app.models;

import java.util.Calendar;
import java.util.GregorianCalendar;

public enum Day {
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);

    private int value;

    Day(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
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
