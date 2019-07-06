package com.fitlogga.app.models.exercises;

import androidx.annotation.Nullable;

import com.fitlogga.app.utils.CountDownTimerPlus;

public class TimedRunExercise extends Exercise {

    private int seconds;
    private String description;
    private CountDownTimerPlus timer;

    public TimedRunExercise(String description, int seconds) {
        super(ExerciseType.TIMED_RUN);
        this.description = description;
        this.seconds = seconds;
    }

    public TimedRunExercise(String description, int seconds, boolean completed) {
        this(description, seconds);
        setCompleted(false);
    }


    public void setTimerObject(CountDownTimerPlus timer) {
        this.timer = timer;
    }

    @Nullable
    public CountDownTimerPlus getTimerObject() {
        return timer;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getDescription() {
        return description;
    }

}
