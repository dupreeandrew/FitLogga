package com.fitlogga.app.models.plan.log.Historics;

import com.fitlogga.app.models.exercises.ExerciseType;

import java.util.List;

/**
 * This class relates to single-valued exercise objects:
 * Meter Run
 * Timed Run
 * Rest Exercise
 */
public class StandardExerciseHistory extends History {

    public static class Snapshot extends History.Snapshot {

        private int value;

        public Snapshot(long timestamp, int value) {
            super(timestamp);
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    private List<Snapshot> snapshots;

    public StandardExerciseHistory(ExerciseType exerciseType, List<Snapshot> snapshots) {
        super(exerciseType);
        this.snapshots = snapshots;
    }

    @Override
    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

    @Override
    public String getName() {
        return getExerciseType().getLocaleName();
    }
}
