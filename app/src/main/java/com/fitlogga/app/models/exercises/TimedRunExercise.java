package com.fitlogga.app.models.exercises;

public class TimedRunExercise extends TimerExercise {

    private int seconds;
    private String description;

    public TimedRunExercise(String description, int seconds) {
        this(description, seconds, false, null);
    }

    public TimedRunExercise(String description, int seconds, boolean completed, String uuid) {
        super(ExerciseType.TIMED_RUN, seconds * 1000, uuid);
        this.description = description;
        this.seconds = seconds;
        setCompleted(false);
    }

    public int getSeconds() {
        return seconds;
    }

    public String getDescription() {
        return description;
    }

}
