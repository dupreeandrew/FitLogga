package com.fitlogga.app.utils;

public class Time {

    public static int getSeconds(String minutes, String seconds) {
        int minutesInt = Integer.parseInt(minutes);
        int secondsInt = Integer.parseInt(seconds);
        return getSeconds(minutesInt, secondsInt);
    }

    public static int getSeconds(int minutes, int seconds) {
        return (minutes * 60) + seconds;
    }

    public static String toHHMMFormat(long seconds) {
        long minutes = seconds / 60;
        long secondsMax60 = seconds % 60;

        String secondsMax60String;
        if (secondsMax60 < 10) {
            secondsMax60String = "0" + secondsMax60;
        }
        else {
            secondsMax60String = String.valueOf(secondsMax60);
        }

        return minutes + ":" + secondsMax60String;
    }

}
