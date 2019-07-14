package com.fitlogga.app.models.exercises;

public abstract class TimerExercise extends Exercise {

    private transient long millisLastTicked = -1;
    private transient boolean isTimerActive = false;
    private transient final long millisTotal;
    private transient long millisRemaining;

    TimerExercise(ExerciseType exerciseType, long millisTotal) {
        super(exerciseType);
        this.millisTotal = millisTotal;
        this.millisRemaining = millisTotal;
    }

    public long getMillisTotal() {
        return this.millisTotal;
    }

    public void updateMillisRemaining(long millisRemaining) {
        // millisRemaining is getting reset expand/close
        if (isTimerActive) {
            this.millisLastTicked = System.currentTimeMillis();
            this.millisRemaining = millisRemaining;
        }
    }

    public long getMillisRemaining() {

        // 10 seconds total.
        // 2 seconds passed.
        // 4 seconds out of date
        // 6 is the remainder.

        /*
        // basically, how many ms is it outdated. 0 is up to date. 1000 = 1 second late.
        long millisPassed; // should be 0.
        long outdatedMillisDifference;
        if (isTimerActive && millisLastTicked != -1) {
            millisPassed = millisTotal - millisRemaining;
            outdatedMillisDifference = System.currentTimeMillis() - millisLastTicked;
        }
        else {
            millisPassed = 0;
            outdatedMillisDifference = 0;
        }

        Log.d("xDD123", "1) " + outdatedMillisDifference);

        Log.d("xDD123", "2) " + (this.millisTotal - outdatedMillisDifference - millisPassed));

        return this.millisTotal - outdatedMillisDifference - millisPassed;
        */

        if (!isTimerActive) {
            return millisTotal;
        }
        else if (millisLastTicked != -1) {
            long outdatedMillisDifference = System.currentTimeMillis() - millisLastTicked;
            long newMillisRemaining = this.millisRemaining - outdatedMillisDifference;
            updateMillisRemaining(newMillisRemaining);
        }

        return this.millisRemaining;
    }

    public boolean isTimerActive() {
        return isTimerActive;
    }

    public void startPreparingTimer(boolean timerActive) {
        isTimerActive = timerActive;
        if (timerActive) {
            millisLastTicked = System.currentTimeMillis();
        }
        else {
            millisLastTicked = -1;
            millisRemaining = millisTotal;
        }
    }

}
