package com.fitlogga.app.utils;

import android.os.CountDownTimer;

/**
 * Extended version of CountDownTimer with bonus functionality:
 * + pause()
 * + resume()
 * + reset()
 * + fadeEnd()
 */
public abstract class CountDownTimerPlus {

    private CountDownTimer timer;
    private long millisTimerDuration;
    private long remainingMillisecondsOfTimer;
    private boolean isTimerPaused;
    private static final int REFRESH_INTERVAL_MILLIS = 20;

    public CountDownTimerPlus(long milliseconds) {

        this.millisTimerDuration = milliseconds;
        this.isTimerPaused = true;

        setCountDownTimer(millisTimerDuration);

    }

    public abstract void onTick(long millisUntilFinished);

    public abstract void onFinish();

    /**
     * Resumes the timer. This can also considered a "start()" method.
     */
    public void resume() {
        if (isPaused()) {
            timer.start();
            isTimerPaused = false;
        }
    }

    public void pause() {
        if (!isPaused()) {
            setCountDownTimer(remainingMillisecondsOfTimer);
            isTimerPaused = true;
        }
    }

    public boolean isPaused() {
        return isTimerPaused;
    }

    /**
     * Resets the timer to its paused factory state.
     */
    public void reset() {
        isTimerPaused = true;
        setCountDownTimer(millisTimerDuration);
        timer.onTick(millisTimerDuration);
    }

    /**
     * Changes the timer duration to 3 seconds remaining.
     */
    public void fadeEnd() {
        setCountDownTimer(3000);
        timer.start();
    }

    /**
     * Creates a brand new timer object based on pre-defined timer fields from constructor.
     * @param millisecondsOfTimer Seconds that the timer will be set to.
     */
    private void setCountDownTimer(final long millisecondsOfTimer) {

        cancelAnyTimerCountDown();

        timer = new CountDownTimer(millisecondsOfTimer, REFRESH_INTERVAL_MILLIS) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingMillisecondsOfTimer = millisUntilFinished;
                CountDownTimerPlus.this.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                isTimerPaused = true;
                setCountDownTimer(millisTimerDuration);
                CountDownTimerPlus.this.onFinish();
            }
        };

    }

    private void cancelAnyTimerCountDown() {

        // not the same as #pause(). This is a one way, irreversible pause.

        if (timer != null) {
            timer.cancel();
        } // else: no count down present.
    }

    public static String toClockFormat(int millis) {

        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int secondsMax60 = seconds % 60;

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