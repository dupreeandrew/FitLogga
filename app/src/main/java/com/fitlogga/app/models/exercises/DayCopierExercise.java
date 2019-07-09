package com.fitlogga.app.models.exercises;

import androidx.annotation.Nullable;

import com.fitlogga.app.models.Day;

/**
 * This class contains exercises from one day into one exercise.
 */
public class DayCopierExercise extends Exercise {

    private Day dayBeingCopied;

    public DayCopierExercise(@Nullable Day dayBeingCopied) {
        super(ExerciseType.COPIER);
        this.dayBeingCopied = dayBeingCopied;
    }

    @Nullable
    public Day getDayBeingCopied() {
        return dayBeingCopied;
    }
}
