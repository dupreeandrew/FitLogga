package com.fitlogga.app.models.exercises;

import android.content.Context;
import android.content.res.Resources;

import com.fitlogga.app.R;
import com.fitlogga.app.models.ApplicationContext;
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
    REST(4),
    @SerializedName("5")
    COPIER(5);

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

    public String getLocaleName() {
        Context context = ApplicationContext.getInstance();
        Resources resources = context.getResources();
        switch (this) {
            case METER_RUN:
                return resources.getString(R.string.global_exercise_meter_run);
            case TIMED_RUN:
                return resources.getString(R.string.global_exercise_timed_run);
            case REPETITION_EXERCISE:
                return resources.getString(R.string.global_exercise_repetition_exercise);
            case FREE_WEIGHT_EXERCISE:
                return resources.getString(R.string.global_exercise_free_weight_exercise);
            case REST:
                return resources.getString(R.string.global_exercise_rest_break);
            default:
                return null;
        }
    }

    ExerciseType(int exerciseTypeValue) {
        this.exerciseTypeValue = exerciseTypeValue;
    }

}
