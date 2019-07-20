package com.fitlogga.app.models.plan.log.Historics;

import com.fitlogga.app.models.exercises.ExerciseType;

import java.util.List;

/**
 * This class relates to FreeWeightExercise
 */
public class FreeWeightHistory extends History {

    public static class Snapshot extends RepetitionHistory.Snapshot {

        private int weight;

        public Snapshot(int sets, int reps, int weight, long timestamp) {
            super(sets, reps, timestamp);
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

    }

    private String name;
    private List<Snapshot> snapshots;

    public FreeWeightHistory(String name, List<Snapshot> snapshots) {
        super(ExerciseType.FREE_WEIGHT_EXERCISE);
        this.name = name;
        this.snapshots = snapshots;
    }

    public String getName() {
        return name;
    }

    public List<Snapshot> getSnapshots() {
        return snapshots;
    }

}
