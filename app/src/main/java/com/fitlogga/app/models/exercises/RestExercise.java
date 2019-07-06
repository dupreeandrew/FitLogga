package com.fitlogga.app.models.exercises;

public class RestExercise extends Exercise {

    private int amountOfTimeToRest;

    /**
     * Rest Exercise -- take a rest from exercising.
     * @param amountOfTimeToRest Duration of rest in seconds.
     */
    public RestExercise(int amountOfTimeToRest) {
        super(ExerciseType.REST);
        this.amountOfTimeToRest = amountOfTimeToRest;
    }

    public RestExercise(int amountOfTimeToRest, boolean completed) {
        this(amountOfTimeToRest);
        setCompleted(false);
    }

    public int getAmountOfTimeToRest() {
        return amountOfTimeToRest;
    }


}
