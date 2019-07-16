package com.fitlogga.app.models.exercises;

public abstract class TimerExercise extends Exercise {

    private transient long millisLastTicked = -1;
    private transient boolean isTimerResumed = false;
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
        this.millisLastTicked = System.currentTimeMillis();
        this.millisRemaining = millisRemaining;
    }

    public long getMillisRemaining() {

        if (!isTimerResumed) {
            return millisRemaining;
        }

        considerOutdatedMillisDifference();
        return this.millisRemaining;
    }

    /*
    Keep in mind that RecyclerView has a reason behind its name -- it's recycling viewholders.
    Because of this, assigned timers for viewholders could constantly be changing, and
    #updateMillisRemaining() won't be called. This function takes consideration into this.
     */
    private void considerOutdatedMillisDifference() {
        if (millisLastTicked != -1) {
            long outdatedMillisDifference = System.currentTimeMillis() - millisLastTicked;
            long newMillisRemaining = this.millisRemaining - outdatedMillisDifference;
            updateMillisRemaining(newMillisRemaining);
        }
    }

    public boolean isTimerResumed() {
        return isTimerResumed;
    }

    public void resumeTimerMode(boolean resume) {
        this.isTimerResumed = resume;
        if (resume) {
            millisLastTicked = System.currentTimeMillis();
        }
        else {
            considerOutdatedMillisDifference();
        }
    }

    public void resetTimerMode() {
        millisLastTicked = -1;
        millisRemaining = millisTotal;
        isTimerResumed = false;
    }

}
