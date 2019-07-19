package com.fitlogga.app.models.exercises;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.UUID;

public abstract class Exercise {

    private ExerciseType exerciseType;
    private boolean completed = false;
    private String uuid;

    public Exercise(ExerciseType exerciseType, @Nullable String uuid) {
        this.exerciseType = exerciseType;

        if (TextUtils.isEmpty(uuid)) {
            this.uuid = UUID.randomUUID().toString();
        }
        else {
            this.uuid = uuid;
        }
    }

    public Exercise(ExerciseType exerciseType) {
        this(exerciseType, null);
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

    @NonNull
    public String getUuid() {
        return this.uuid;
    }

}
