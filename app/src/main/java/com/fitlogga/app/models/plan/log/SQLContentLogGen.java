package com.fitlogga.app.models.plan.log;

import android.content.ContentValues;

import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.FreeWeightExercise;
import com.fitlogga.app.models.exercises.MeterRunExercise;
import com.fitlogga.app.models.exercises.RepetitionExercise;
import com.fitlogga.app.models.exercises.RestExercise;
import com.fitlogga.app.models.exercises.TimedRunExercise;

import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_COMMON_VALUE;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NUM_REPS;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NUM_SETS;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NUM_WEIGHT;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_TIMESTAMP;

public class SQLContentLogGen {
    public static ContentValues from(Exercise exercise) {
        switch (exercise.getExerciseType()) {
            case METER_RUN:
                MeterRunExercise meterRunExercise = (MeterRunExercise) exercise;
                return from(meterRunExercise.getDistance());
            case TIMED_RUN:
                TimedRunExercise timedRunExercise = (TimedRunExercise) exercise;
                return from(timedRunExercise.getSeconds());
            case REST:
                RestExercise restExercise = (RestExercise) exercise;
                return from(restExercise.getSecondsOfRest());
            case REPETITION_EXERCISE:
                return from((RepetitionExercise)exercise);
            case FREE_WEIGHT_EXERCISE:
                return from((FreeWeightExercise)exercise);
        }
        throw new IllegalArgumentException();
    }

    private static ContentValues from(int value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_COMMON_VALUE, value);
        contentValues.put(COLUMN_TIMESTAMP, System.currentTimeMillis());
        return contentValues;
    }

    private static ContentValues from(RepetitionExercise exercise) {
        int sets = exercise.getNumberOfSets();
        int reps = exercise.getNumberOfRepetitions();
        long timestamp = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NUM_SETS, sets);
        contentValues.put(COLUMN_NUM_REPS, reps);
        contentValues.put(COLUMN_TIMESTAMP, timestamp);
        return contentValues;

    }

    private static ContentValues from(FreeWeightExercise exercise) {
        int sets = exercise.getNumberOfSets();
        int reps = exercise.getNumberOfRepetitions();
        int weight = exercise.getAmountOfWeight();
        long timestamp = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NUM_SETS, sets);
        contentValues.put(COLUMN_NUM_REPS, reps);
        contentValues.put(COLUMN_NUM_WEIGHT, weight);
        contentValues.put(COLUMN_TIMESTAMP, timestamp);
        return contentValues;

    }
}
