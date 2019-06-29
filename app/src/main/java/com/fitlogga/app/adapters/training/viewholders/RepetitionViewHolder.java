package com.fitlogga.app.adapters.training.viewholders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.RepetitionExercise;
import com.fitlogga.app.utils.CountDownTimerPlus;
import com.fitlogga.app.utils.Time;

public class RepetitionViewHolder extends ExerciseViewHolder {

    private View view;
    private CountDownTimerPlus timer;
    private Button finishSetButton;
    private Button endTimerButton;

    public RepetitionViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;

        finishSetButton = view.findViewById(R.id.btn_complete_set);
        endTimerButton = view.findViewById(R.id.btn_end_timer);
    }

    @Override
    public void onManifest(Exercise exercise) {
        RepetitionExercise repetitionExercise = (RepetitionExercise)exercise;
        String name = repetitionExercise.getName();
        String description = repetitionExercise.getDescription();
        int numSets = repetitionExercise.getNumberOfSets();
        int numReps = repetitionExercise.getNumberOfRepetitions();
        int restSeconds = repetitionExercise.getRestTimeBetweenSets();

        String subtitle = numSets + "x" + numReps + " Repetition Exercise";

        setTitle(name);
        setSubtitle(subtitle);
        setSets(repetitionExercise.getNumSetsCompleted(), numSets);

        initTimer(repetitionExercise);
        initEndTimerButton();
        initFinishSetButton(repetitionExercise);


    }

    private void setSets(int numSetsFinished, int numSetsTotal) {
        TextView setsRepsView = view.findViewById(R.id.tv_num_sets);
        String text = numSetsFinished + "/" + numSetsTotal + " sets";
        setsRepsView.setText(text);
    }

    private void initTimer(RepetitionExercise repetitionExercise) {
        ProgressBar timerProgressBar = view.findViewById(R.id.pb_progress);
        timerProgressBar.setMax(5000);

        final int MILLISECONDS_OF_TIMER = repetitionExercise.getRestTimeBetweenSets() * 1000;
        timer = getTimerObject(MILLISECONDS_OF_TIMER);
        timer.onTick(MILLISECONDS_OF_TIMER);
    }

    private CountDownTimerPlus getTimerObject(int milliseconds) {
        return new CountDownTimerPlus(milliseconds) {

            private TextView timerTextView = view.findViewById(R.id.tv_timer);

            @Override
            public void onTick(long millisUntilFinished) {


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
                finishSetButton.setEnabled(true);
                endTimerButton.setEnabled(false);
                timerTextView.setText("0:00");
            }
        };

    }

    private void initFinishSetButton(RepetitionExercise repetitionExercise) {
        finishSetButton.setOnClickListener(buttonView -> {
            int numSetsCompleted = repetitionExercise.getNumSetsCompleted() + 1;




            timer.resume();
            finishSetButton.setEnabled(false);
            endTimerButton.setEnabled(true);
        });

    }


    private void initEndTimerButton() {
        endTimerButton.setOnClickListener(buttonView -> {
            timer.reset();
            finishSetButton.setEnabled(true);
            endTimerButton.setEnabled(false);
        });
    }


    @Override
    protected View[] getCollapsibleViews() {
        return new View[0];
    }
}
