package com.fitlogga.app.models.exercises;

public abstract class Exercise {

    private ExerciseType exerciseType;
    private boolean completed = false;

    public Exercise(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public final boolean isCompleted() {
        return completed;
    }

    public final void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
