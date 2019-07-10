package com.fitlogga.app.models.exercises;

import java.util.List;

public class DayCopierDetector {

    public static boolean isDayCopier(List<Exercise> dailyRoutine) {
        // Day Copiers exist by themselves. There are no other exercises at all.
        Exercise firstExercise = dailyRoutine.get(0);
        return firstExercise.getExerciseType() == ExerciseType.COPIER;
    }
}
