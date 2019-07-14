package com.fitlogga.app.adapters.training.viewholders;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.TimerExercise;
import com.fitlogga.app.utils.CountDownTimerPlus;
import com.fitlogga.app.utils.Time;

abstract class TimerViewHolder extends ExerciseViewHolder {

    public interface TimerStatus {
        void onFinish();
    }

    private View view;
    private TimerStatus timerStatus;
    private int timerTextResId;
    private int progressBarResId;
    private CountDownTimerPlus timer;
    private TimerExercise timerExercise;
    private static final String TAG = "laa92!";

    TimerViewHolder(@NonNull View itemView, TimerStatus timerStatus,
                    @IdRes int timerTextResId, @IdRes int progressBarResId) {
        super(itemView);
        this.view = itemView;
        this.timerStatus = timerStatus;
        this.timerTextResId = timerTextResId;
        this.progressBarResId = progressBarResId;
    }

    @Override
    final void onManifest(Exercise exercise) {
        TimerExercise timerExercise = (TimerExercise)exercise;
        this.timerExercise = timerExercise;
        initTimer(timerExercise);
        onPostManifest(timerExercise);
    }

    private void initTimer(TimerExercise timerExercise) {

        if (timer != null) {
            timer.pause();
            timer = null;
        }

        ProgressBar timerProgressBar = view.findViewById(progressBarResId);
        timerProgressBar.setMax(5000);
        timerProgressBar.setProgress(0);



        final long MILLISECONDS_OF_TIMER = timerExercise.getMillisRemaining();
        timer = getTimerObject(MILLISECONDS_OF_TIMER);
        timer.onTick(MILLISECONDS_OF_TIMER);

        Log.d("testt1", String.valueOf(MILLISECONDS_OF_TIMER));

        if (timerExercise.isTimerActive()) {
            timer.resume();
        }

        if (getAdapterPosition() == 0) {
            Log.d(TAG, "RESETTED TIMER TO " + MILLISECONDS_OF_TIMER);
        }



    }

    /**
     * Simply generates a timer object that has a specified duration.
     * Nothing less, nothing more.
     * @param timerDurationMillis
     * @return
     */
    private CountDownTimerPlus getTimerObject(long timerDurationMillis) {
        return new CountDownTimerPlus(timerDurationMillis) {

            private TextView timerTextView = view.findViewById(timerTextResId);
            private ProgressBar timerProgressBar = view.findViewById(progressBarResId);

            @Override
            public void onTick(long millisUntilFinished) {
                // getMillisTotal is accurrate.
                // millisUntilFinished is accurate.

                // when timer is faded, millisduration needs to be updated to total duration.

                timerExercise.updateMillisRemaining(millisUntilFinished);

                int secondsUntilFinished = (int)(millisUntilFinished / 1000);
                timerTextView.setText(Time.toHHMMFormat(secondsUntilFinished));

                long millisecondsTotal = timerExercise.getMillisTotal();
                double millisecondsElapsed = millisecondsTotal - millisUntilFinished;
                double progress = millisecondsElapsed / (millisecondsTotal) * 5000.00 ;
                timerProgressBar.setProgress((int)progress);

                if (getAdapterPosition() == 0) {
                    /*
                    Log.d(TAG, "===");
                    Log.d(TAG, "MILLISELAPSED: " + millisecondsElapsed);
                    Log.d(TAG, "MILLISDURATION:" + timerDurationMillis);
                    Log.d(TAG, "MILLISTOTAL: " + millisecondsTotal);
                    Log.d(TAG, "PROGRESS: " + progress);
                    Log.d(TAG, "===");
                    */
                }




            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                Log.d("xDD123", "timer is disabled");
                timerExercise.startPreparingTimer(false);
                initTimer(timerExercise);
                timerStatus.onFinish();
            }
        };

    }

    abstract void onPostManifest(TimerExercise timerExercise);

    void startTimer() {
        Log.d(TAG, "timer started");
        timerExercise.startPreparingTimer(true);
        initTimer(timerExercise);
    }

    void endTimer() {
        Log.d(TAG, "timer faded.");
        timer.pause();
        timer = getTimerObject(3000);
        timer.resume();
    }

}
