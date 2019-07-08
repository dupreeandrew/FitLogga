package com.fitlogga.app.adapters.training.viewholders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.RestExercise;
import com.fitlogga.app.utils.CountDownTimerPlus;
import com.fitlogga.app.utils.Time;

public class RestViewHolder extends ExerciseViewHolder {

    private View view;
    private CountDownTimerPlus timer;
    private String startString;

    public RestViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;

        startString = itemView.getResources().getString(R.string.vh_timed_run_start);
    }

    @Override
    public void onManifest(Exercise exercise) {

        RestExercise restExercise = (RestExercise)exercise;
        int seconds = restExercise.getSecondsOfRest();

        // "Rest Break"
        String restBreakString = view.getResources().getString(R.string.vh_rest_rest_break);
        String subtitle = Time.toHHMMFormat(seconds) + " " + restBreakString;

        setTitle(restBreakString);
        setSubtitle(subtitle);
        initTimer(restExercise);
        initStartButton();
        initEndTimerButton();

    }

    private void initTimer(RestExercise restExercise) {

        ProgressBar timerProgressBar = view.findViewById(R.id.pb_progress);
        timerProgressBar.setMax(5000);

        final int MILLISECONDS_OF_TIMER = restExercise.getSecondsOfRest() * 1000;
        timer = getTimerObject(restExercise, MILLISECONDS_OF_TIMER);
        timer.onTick(MILLISECONDS_OF_TIMER);


    }

    private CountDownTimerPlus getTimerObject(RestExercise restExercise, int milliseconds) {
        return new CountDownTimerPlus(milliseconds) {
            @Override
            public void onTick(long millisUntilFinished) {

                TextView timerTextView = view.findViewById(R.id.tv_timer);
                ProgressBar timerProgressBar = view.findViewById(R.id.pb_progress);

                int secondsUntilFinished = (int)(millisUntilFinished / 1000);
                double millisecondsElapsed = milliseconds - millisUntilFinished;
                double progress = millisecondsElapsed / (milliseconds) * 5000.00 ;

                timerTextView.setText(Time.toHHMMFormat(secondsUntilFinished));
                timerProgressBar.setProgress((int)progress);

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                Button startButton = view.findViewById(R.id.btn_start);
                startButton.setText(startString);
                TextView timerTextView = view.findViewById(R.id.tv_timer);
                timerTextView.setText("0:00");

                RestViewHolder.this.setAsCompleted(restExercise);
            }
        };

    }

    private void setAsCompleted(RestExercise restExercise) {
        restExercise.setCompleted(true);
        refreshItemInDataSet();
    }

    private void initStartButton() {
        String pauseString = view.getResources().getString(R.string.vh_timed_run_pause);
        Button startButton = view.findViewById(R.id.btn_start);
        startButton.setOnClickListener(view -> {
            if (timer.isPaused()) {
                timer.resume();
                startButton.setText(pauseString);
            }
            else {
                timer.pause();
                startButton.setText(startString);
            }
        });
    }

    private void initEndTimerButton() {
        Button endTimerButton = view.findViewById(R.id.btn_end_timer);
        endTimerButton.setOnClickListener(view -> {
            timer.fadeEnd();
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
