package com.fitlogga.app.models.exercises;

public class BlankExerciseGenerator {

    public static TimedRunExercise getTimedRun() {
        return new TimedRunExercise("", 0);
    }

    public static MeterRunExercise getMeterRun() {
        return new MeterRunExercise("", 0, "");
    }

    public static RepetitionExercise getRepetition() {
        return new RepetitionExercise("Repetition Exercise", "", 0, 0, 0);
    }

    public static FreeWeightExercise getFreeWeight() {
        return new FreeWeightExercise("Free Weight Exercise", "", 0, 0, 0, "", 0);
    }

    public static RestExercise getRest() {
        return new RestExercise(0);
    }



}
