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

    public TimedRunViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void onManifest(Exercise exercise) {

        TimedRunExercise timedRunExercise = (TimedRunExercise)exercise;
        int seconds = timedRunExercise.getSeconds();

        String subtitle = Time.toHHMMFormat(seconds) + " Timed Run";

        setTitle("Timed Run");
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
                startButton.setText("Start");
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
        Button startButton = view.findViewById(R.id.btn_start);
        startButton.setOnClickListener(view -> {
            if (timer.isPaused()) {
                timer.resume();
                startButton.setText("Pause");
            }
            else {
                timer.pause();
                startButton.setText("Start");
            }
        });
    }

    private void initResetButton() {
        Button startButton = view.findViewById(R.id.btn_start);
        Button resetButton = view.findViewById(R.id.btn_reset);
        resetButton.setOnClickListener(view -> {
            startButton.setText("Start");
            timer.reset();
        });
    }

    @Override
    protected View[] getCollapsibleViews() {
        return new View[] {
                view.findViewById(R.id.pb_progress),
                view.findViewById(R.id.iv_runner),
                view.findViewById(R.id.tv_timer),
                view.findViewById(R.id.btn_start),
                view.findViewById(R.id.btn_reset)
        };
    }

}
