package com.fitlogga.app.adapters.training.viewholders;

import android.annotation.SuppressLint;
import android.text.TextUtils;
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
import com.fitlogga.app.viewmods.ViewEnabler;

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
        int numSetsCompleted = repetitionExercise.getNumSetsCompleted();
        int numSets = repetitionExercise.getNumberOfSets();
        int numReps = repetitionExercise.getNumberOfRepetitions();

        // "Repetition Exercise"
        String repetitionExerciseString = view.getResources()
                .getString(R.string.vh_repetition_repetition_exercise);
        String subtitle = numSets + "x" + numReps + " " + repetitionExerciseString;

        setTitle(name);
        setSubtitle(subtitle);
        setDescription(description);
        setSets(numSetsCompleted, numSets);

        initTimer(repetitionExercise);
        initEndTimerButton();
        initFinishSetButton(repetitionExercise);

    }

    private void setDescription(String description) {

        if (TextUtils.isEmpty(description)) {
            description = view.getResources()
                    .getString(R.string.vh_repetition_default_description);
        }

        TextView descriptionView = itemView.findViewById(R.id.tv_description);
        descriptionView.setText(description);
    }

    private void setSets(int numSetsFinished, int numSetsTotal) {
        TextView setsRepsView = view.findViewById(R.id.tv_num_sets);

        // "Sets Completed"
        String setsCompleted = view.getResources().getString(R.string.vh_repetition_sets_completed);
        String text = numSetsFinished + "/" + numSetsTotal + " " + setsCompleted;
        setsRepsView.setText(text);
    }

    private void initTimer(RepetitionExercise repetitionExercise) {

        if (timer != null && !timer.isPaused()) return;

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
                ViewEnabler.setEnabled(finishSetButton, true);
                ViewEnabler.setEnabled(endTimerButton, false);
            }
        };

    }

    private void initFinishSetButton(RepetitionExercise repetitionExercise) {
        finishSetButton.setOnClickListener(buttonView -> {

            int numSetsCompleted = repetitionExercise.getNumSetsCompleted() + 1;
            repetitionExercise.setNumSetsCompleted(numSetsCompleted);

            if (numSetsCompleted == repetitionExercise.getNumberOfSets()) {
                repetitionExercise.setCompleted(true);
                refreshItemInDataSet();
            }

            setSets(numSetsCompleted, repetitionExercise.getNumberOfSets());

            ViewEnabler.setEnabled(finishSetButton, false);
            ViewEnabler.setEnabled(endTimerButton, true);
            timer.reset();
            timer.resume();

        });

    }

    private void initEndTimerButton() {
        endTimerButton.setOnClickListener(
                buttonView -> timer.fadeEnd()
        );
    }

    @Override
    protected int[] getCollapsibleViewResourceIds() {
        return new int[] {
                R.id.pb_progress,
                R.id.iv_clock,
                R.id.tv_timer,
                R.id.tv_num_sets,
                R.id.divider_one,
                R.id.iv_info,
                R.id.tv_description,
                R.id.divider_two,
                R.id.btn_complete_set,
                R.id.btn_end_timer,
        };
    }
}
