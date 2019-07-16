package com.fitlogga.app.adapters.training.viewholders;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.RestExercise;
import com.fitlogga.app.models.exercises.TimerExercise;
import com.fitlogga.app.utils.Time;

public class RestViewHolder extends TimerViewHolder {

    private View view;
    private String startString;

    public RestViewHolder(@NonNull View itemView) {
        super(itemView, R.id.tv_timer, R.id.pb_progress);
        this.view = itemView;

        startString = itemView.getResources().getString(R.string.vh_timed_run_start);
    }

    @Override
    void onTimerEnd(TimerExercise timerExercise) {
        Button startButton = view.findViewById(R.id.btn_start);
        startButton.setText(startString);

        timerExercise.setCompleted(true);
        refreshItemInDataSet();
    }

    @Override
    public void onPostManifest(TimerExercise exercise) {

        RestExercise restExercise = (RestExercise)exercise;
        int seconds = restExercise.getSecondsOfRest();

        // "Rest Break"
        String restBreakString = view.getResources().getString(R.string.vh_rest_rest_break);
        String subtitle = Time.toHHMMFormat(seconds) + " " + restBreakString;

        setTitle(restBreakString);
        setSubtitle(subtitle);
        initStartButton();
        initEndTimerButton();

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

    private void initEndTimerButton() {
        Button endTimerButton = view.findViewById(R.id.btn_end_timer);
        endTimerButton.setOnClickListener(view -> {
            super.endTimer();
        });
    }

    @Override
    protected int[] getCollapsibleViewResourceIds() {
        return new int[] {
                R.id.pb_progress,
                R.id.iv_heart,
                R.id.tv_timer,
                R.id.divider_one,
                R.id.iv_info,
                R.id.tv_description,
                R.id.divider_two,
                R.id.btn_start,
                R.id.btn_end_timer
        };
    }

}
