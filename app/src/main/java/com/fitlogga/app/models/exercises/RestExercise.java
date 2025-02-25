package com.fitlogga.app.models.exercises;

public class RestExercise extends TimerExercise {

    private int amountOfTimeToRest;

    /**
     * Rest Exercise -- take a rest from exercising.
     * @param amountOfTimeToRest Duration of rest in seconds.
     */
    public RestExercise(int amountOfTimeToRest) {
        this(amountOfTimeToRest, false, null);
    }

    public RestExercise(int amountOfTimeToRest, boolean completed, String uuid) {
        super(ExerciseType.REST, amountOfTimeToRest * 1000, uuid);
        this.amountOfTimeToRest = amountOfTimeToRest;
        setCompleted(false);
    }

    public int getSecondsOfRest() {
        return amountOfTimeToRest;
    }


}
