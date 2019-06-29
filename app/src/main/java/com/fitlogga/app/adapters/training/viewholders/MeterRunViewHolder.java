package com.fitlogga.app.adapters.training.viewholders;

import android.view.View;

import androidx.annotation.NonNull;

import com.fitlogga.app.models.exercises.Exercise;

public class MeterRunViewHolder extends ExerciseViewHolder {

    private View view;

    public MeterRunViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void onManifest(Exercise exercise) {

    }

    @Override
    protected View[] getCollapsibleViews() {
        return new View[0];
    }

}
