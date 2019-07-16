package com.fitlogga.app.models.exercises;

public class TimedRunExercise extends TimerExercise {

    private int seconds;
    private String description;

    public TimedRunExercise(String description, int seconds) {
        super(ExerciseType.TIMED_RUN, seconds * 1000);
        this.description = description;
        this.seconds = seconds;
    }

    public TimedRunExercise(String description, int seconds, boolean completed) {
        this(description, seconds);
        setCompleted(false);
    }

    public int getSeconds() {
        return seconds;
    }

    public String getDescription() {
        return description;
    }

}
