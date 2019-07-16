package com.fitlogga.app.adapters.training.viewholders;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.TimedRunExercise;
import com.fitlogga.app.models.exercises.TimerExercise;
import com.fitlogga.app.utils.Time;

public class TimedRunViewHolder extends TimerViewHolder {

    private View view;
    private String startString;

    public TimedRunViewHolder(@NonNull View itemView) {
        super(itemView, R.id.tv_timer, R.id.pb_progress);
        this.view = itemView;
        this.startString = itemView.getResources().getString(R.string.vh_timed_run_start);
    }

    @Override
    void onTimerEnd(TimerExercise timerExercise) {

        Button startButton = view.findViewById(R.id.btn_start);
        startButton.setText(startString);

        timerExercise.setCompleted(true);
        refreshItemInDataSet();
    }

    @Override
    void onPostManifest(TimerExercise exercise) {

        TimedRunExercise timedRunExercise = (TimedRunExercise)exercise;
        int seconds = timedRunExercise.getSeconds();
        String description = timedRunExercise.getDescription();

        // "Timed Run"
        String timedRunString = view.getResources().getString(R.string.vh_timed_run_timed_run);
        String subtitle = Time.toHHMMFormat(seconds) + " " + timedRunString;

        setTitle(timedRunString);
        setSubtitle(subtitle);
        setDescription(description);
        initStartButton();
        initResetButton();

    }

    private void setDescription(String description) {

        if (TextUtils.isEmpty(description)) {
            description = view.getResources()
                    .getString(R.string.vh_timed_run_default_description);
        }

        TextView descriptionView = itemView.findViewById(R.id.tv_description);
        descriptionView.setText(description);
    }

    private void initStartButton() {
        String pauseString = view.getResources().getString(R.string.vh_timed_run_pause);
        Button startButton = view.findViewById(R.id.btn_start);
        startButton.setOnClickListener(view -> {
            if (super.isTimerActive()) {
                super.pauseTimer();
                startButton.setText(startString);
            }
            else {
                super.resumeTimer();
                startButton.setText(pauseString);
            }
        });
    }

    private void initResetButton() {
        Button startButton = view.findViewById(R.id.btn_start);
        Button resetButton = view.findViewById(R.id.btn_reset);
        resetButton.setOnClickListener(view -> {
            startButton.setText(startString);
            super.resetTimer();
        });
    }

    @Override
    protected int[] getCollapsibleViewResourceIds() {
        return new int[] {
                R.id.pb_progress,
                R.id.iv_runner,
                R.id.tv_timer,
                R.id.divider_one,
                R.id.iv_info,
                R.id.tv_description,
                R.id.divider_two,
                R.id.btn_start,
                R.id.btn_reset
        };
    }

}
