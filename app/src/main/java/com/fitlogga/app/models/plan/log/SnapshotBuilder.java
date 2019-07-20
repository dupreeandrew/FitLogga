package com.fitlogga.app.models.plan.log;

import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.FreeWeightExercise;
import com.fitlogga.app.models.exercises.MeterRunExercise;
import com.fitlogga.app.models.exercises.RepetitionExercise;
import com.fitlogga.app.models.exercises.RestExercise;
import com.fitlogga.app.models.exercises.TimedRunExercise;
import com.fitlogga.app.models.plan.log.Historics.FreeWeightHistory;
import com.fitlogga.app.models.plan.log.Historics.RepetitionHistory;
import com.fitlogga.app.models.plan.log.Historics.StandardExerciseHistory;

public class SnapshotBuilder {

    public static Object from(Exercise exercise) {
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

    public static StandardExerciseHistory.Snapshot from(int value) {
        long timestamp = System.currentTimeMillis();
        return new StandardExerciseHistory.Snapshot(timestamp, value);
    }

    public static RepetitionHistory.Snapshot from(RepetitionExercise exercise) {
        int sets = exercise.getNumberOfSets();
        int reps = exercise.getNumberOfRepetitions();
        long timestamp = System.currentTimeMillis();
        return new RepetitionHistory.Snapshot(sets, reps, timestamp);
    }

    public static FreeWeightHistory.Snapshot from(FreeWeightExercise exercise) {
        int sets = exercise.getNumberOfSets();
        int reps = exercise.getNumberOfRepetitions();
        int weight = exercise.getAmountOfWeight();
        long timestamp = System.currentTimeMillis();
        return new FreeWeightHistory.Snapshot(sets, reps, weight, timestamp);
    }
}
