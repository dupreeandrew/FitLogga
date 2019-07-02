package com.fitlogga.app.adapters.training.viewholders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.TimedRunExercise;
import com.fitlogga.app.utils.CountDownTimerPlus;
import com.fitlogga.app.utils.Time;

public class TimedRunViewHolder extends ExerciseViewHolder {

    private View view;
    private CountDownTimerPlus timer;
    private String startString;

    public TimedRunViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;

        startString = itemView.getResources().getString(R.string.vh_timed_run_start);
    }

    @Override
    public void onManifest(Exercise exercise) {

        TimedRunExercise timedRunExercise = (TimedRunExercise)exercise;
        int seconds = timedRunExercise.getSeconds();

        // "Timed Run"
        String timedRunString = view.getResources().getString(R.string.vh_timed_run_timed_run);
        String subtitle = Time.toHHMMFormat(seconds) + " " + timedRunString;

        setTitle(timedRunString);
        setSubtitle(subtitle);
        initTimer(timedRunExercise);
        initStartButton();
        initResetButton();

    }

    private void initTimer(TimedRunExercise timedRunExercise) {

        ProgressBar timerProgressBar = view.findViewById(R.id.pb_progress);
        timerProgressBar.setMax(5000);

        final int MILLISECONDS_OF_TIMER = timedRunExercise.getSeconds() * 1000;
        timer = getTimerObject(timedRunExercise, MILLISECONDS_OF_TIMER);
        timer.onTick(MILLISECONDS_OF_TIMER);


    }

    private CountDownTimerPlus getTimerObject(TimedRunExercise timedRunExercise, int milliseconds) {
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

                TimedRunViewHolder.this.setAsCompleted(timedRunExercise);
            }
        };

    }

    private void setAsCompleted(TimedRunExercise timedRunExercise) {
        timedRunExercise.setCompleted(true);
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

    private void initResetButton() {
        Button startButton = view.findViewById(R.id.btn_start);
        Button resetButton = view.findViewById(R.id.btn_reset);
        resetButton.setOnClickListener(view -> {
            startButton.setText(startString);
            timer.reset();
        });
    }

    @Override
    protected int[] getCollapsibleViewResourceIds() {
        return new int[] {
                R.id.pb_progress,
                R.id.iv_runner,
                R.id.tv_timer,
                R.id.btn_start,
                R.id.btn_reset
        };
    }

}
