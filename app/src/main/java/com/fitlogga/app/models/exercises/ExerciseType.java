package com.fitlogga.app.models.exercises;

import com.google.gson.annotations.SerializedName;

public enum ExerciseType {

    @SerializedName("0")
    METER_RUN(0),
    @SerializedName("1")
    TIMED_RUN(1),
    @SerializedName("2")
    REPETITION_EXERCISE(2),
    @SerializedName("3")
    FREE_WEIGHT_EXERCISE(3),
    @SerializedName("4")
    REST(4);

    final int exerciseTypeValue;

    public int getExerciseTypeValue() {
        return exerciseTypeValue;
    }

    public static ExerciseType fromInteger(int i) {

        for (ExerciseType b : ExerciseType.values()) {
            if (b.getExerciseTypeValue() == i) { return b; }
        }

        throw new IndexOutOfBoundsException();

    }

    ExerciseType(int exerciseTypeValue) {
        this.exerciseTypeValue = exerciseTypeValue;
    }

}
