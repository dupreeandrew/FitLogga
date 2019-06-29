package com.fitlogga.app.models.exercises;

/**
 * Represents field names for Exercise Objects in the database.
 */
public enum ExerciseFields {

    EXERCISE_TYPE("type"),
    NAME("name"),
    SECONDS("seconds"),
    DISTANCE_IN_METERS("distance"),
    NUMBER_OF_SETS("num_sets"),
    NUMBER_OF_REPS("num_reps"),
    AMOUNT_OF_WEIGHT("num_weight"),
    DESCRIPTION("desc"),
    REST_TIME("rest_time");

    private String fieldNameInDatabase;

    @Override
    public String toString() {
        return fieldNameInDatabase;
    }

    ExerciseFields(String fieldNameInDatabase) {
        this.fieldNameInDatabase = fieldNameInDatabase;
    }
}