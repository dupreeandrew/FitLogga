package com.fitlogga.app.models.plan.log.Historics;

import com.fitlogga.app.models.exercises.ExerciseType;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A historical is an object that represents a log of a specific exercise.
 */
public abstract class History {

    public static abstract class Snapshot {

        private long timestamp;

        public Snapshot(long timestamp) {
            this.timestamp = timestamp;
        }

        public long getTimestamp() {
            return timestamp;
        }

    }

    @SerializedName("exerciseType")
    private ExerciseType exerciseType;

    public History(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public abstract List<? extends Snapshot> getSnapshots();

    public abstract String getName();
}
