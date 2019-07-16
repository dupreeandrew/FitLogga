package com.fitlogga.app.adapters.training.viewholders;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.RepetitionExercise;
import com.fitlogga.app.models.exercises.TimerExercise;
import com.fitlogga.app.viewmods.ViewEnabler;

public class RepetitionViewHolder extends TimerViewHolder {

    private View view;
    private Button finishSetButton;
    private Button endTimerButton;


    public RepetitionViewHolder(@NonNull View itemView) {
        super(itemView, R.id.tv_timer, R.id.pb_progress);

        this.view = itemView;
        this.finishSetButton = view.findViewById(R.id.btn_complete_set);
        this.endTimerButton = view.findViewById(R.id.btn_end_timer);
    }

    @Override
    void onTimerEnd(TimerExercise timerExercise) {
        ViewEnabler.setEnabled(finishSetButton, true);
        ViewEnabler.setEnabled(endTimerButton, false);
    }

    @Override
    void onPostManifest(TimerExercise exercise) {
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
            super.resumeTimer();

        });

    }

    private void initEndTimerButton() {
        endTimerButton.setOnClickListener(
                buttonView -> super.endTimer()
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
