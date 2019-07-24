package com.fitlogga.app.models.plan.log.Historics;

import com.fitlogga.app.models.exercises.ExerciseType;

import java.util.List;

/**
 * This class relates to RepetitionExercise
 */
public class RepetitionHistory extends History {

    public static class Snapshot extends History.Snapshot {

        private int sets;
        private int reps;

        public Snapshot(int sets, int reps,long timestamp) {
            super(timestamp);
            this.sets = sets;
            this.reps = reps;
        }

        public int getSets() {
            return sets;
        }

        public int getReps() {
            return reps;
        }

    }

    private String name;
    private List<Snapshot> snapshots;

    public RepetitionHistory(String name, List<Snapshot> snapshots) {
        super(ExerciseType.REPETITION_EXERCISE);
        this.name = name;
        this.snapshots = snapshots;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Snapshot> getSnapshots() {
        return snapshots;
    }
}
