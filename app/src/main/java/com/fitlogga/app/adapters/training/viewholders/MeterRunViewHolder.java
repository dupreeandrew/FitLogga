package com.fitlogga.app.adapters.training.viewholders;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.MeterRunExercise;
import com.fitlogga.app.viewmods.ViewEnabler;

public class MeterRunViewHolder extends ExerciseViewHolder {

    private View view;

    public MeterRunViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void onManifest(Exercise exercise) {
        MeterRunExercise meterRunExercise = (MeterRunExercise)exercise;
        int distance = meterRunExercise.getDistance();
        String distanceUnits = meterRunExercise.getDistanceUnits();
        String description = meterRunExercise.getDescription();

        // "Meter Run Exercise"
        String title = view.getResources().getString(R.string.vh_meter_run_meter_run_exercise);
        // "Run"
        String runString = view.getResources().getString(R.string.vh_meter_run_run);
        String subtitle = runString + " " + distance + " " + distanceUnits;

        setTitle(title);
        setSubtitle(subtitle);
        setRunDistanceText(subtitle);
        setDescription(description);

        initProgressBar();
        initImFinishedButton(meterRunExercise);
        initEnableGPSButton();

    }

    private void setRunDistanceText(String subtitle) {
        TextView runDistanceTextView = view.findViewById(R.id.tv_distance);
        runDistanceTextView.setText(subtitle);
    }

    private void setDescription(String description) {

        if (TextUtils.isEmpty(description)) {
            description = view.getResources()
                    .getString(R.string.vh_meter_run_default_description);
        }

        TextView descriptionView = itemView.findViewById(R.id.tv_description);
        descriptionView.setText(description);
    }

    private void initProgressBar() {
        ProgressBar progressBar = view.findViewById(R.id.pb_distance);
        ViewEnabler.setEnabled(progressBar, false);
    }

    private void initImFinishedButton(MeterRunExercise meterRunExercise) {
        Button imFinishedButton = view.findViewById(R.id.btn_im_finished);
        imFinishedButton.setOnClickListener(buttonView -> {
            meterRunExercise.setCompleted(true);
            refreshItemInDataSet();
        });
    }

    private void initEnableGPSButton() {
        // to be implemented
    }

    @Override
    protected int[] getCollapsibleViewResourceIds() {
        return new int[] {
                R.id.tv_distance,
                R.id.pb_distance,
                R.id.tv_enable_gps_for_odometer,
                R.id.divider_one,
                R.id.iv_info,
                R.id.tv_description,
                R.id.divider_two,
                R.id.btn_im_finished,
                R.id.btn_enable_gps
        };
    }

}
