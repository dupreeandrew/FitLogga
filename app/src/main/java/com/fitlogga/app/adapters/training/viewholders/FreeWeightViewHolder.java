package com.fitlogga.app.adapters.training.viewholders;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.FreeWeightExercise;
import com.fitlogga.app.models.exercises.TimerExercise;
import com.fitlogga.app.viewmods.ViewEnabler;

public class FreeWeightViewHolder extends TimerViewHolder {

    private View view;
    private Button finishSetButton;
    private Button endTimerButton;

    public FreeWeightViewHolder(@NonNull View itemView) {
        super(itemView, () -> {

            // This is called when timer ends.
            Button finishSetButton = itemView.findViewById(R.id.btn_complete_set);
            Button endTimerButton = itemView.findViewById(R.id.btn_end_timer);
            ViewEnabler.setEnabled(finishSetButton, true);
            ViewEnabler.setEnabled(endTimerButton, false);

        }, R.id.tv_timer, R.id.pb_progress);



        this.view = itemView;
        finishSetButton = view.findViewById(R.id.btn_complete_set);
        endTimerButton = view.findViewById(R.id.btn_end_timer);
    }

    @Override
    void onPostManifest(TimerExercise timerExercise) {
        FreeWeightExercise freeWeightExercise = (FreeWeightExercise)timerExercise;
        String name = freeWeightExercise.getName();
        String description = freeWeightExercise.getDescription();
        int numSetsCompleted = freeWeightExercise.getNumSetsCompleted();
        int numSets = freeWeightExercise.getNumberOfSets();
        int numReps = freeWeightExercise.getNumberOfRepetitions();
        int numWeight = freeWeightExercise.getAmountOfWeight();
        String weightUnit = freeWeightExercise.getAmountOfWeightUnits();

        // "Free Weight Exercise"
        String freeWeightExerciseString = view.getResources()
                .getString(R.string.vh_free_weight_free_weight_exercise);
        String subtitle = numSets + "x" + numReps + " " + freeWeightExerciseString;

        setTitle(name);
        setSubtitle(subtitle);
        setDescription(description);
        setSets(numSetsCompleted, numSets);
        setWeightNumberView(numWeight);
        setWeightUnitView(weightUnit);

        initEndTimerButton(freeWeightExercise);
        initFinishSetButton(freeWeightExercise);
        initIncrementWeightButton(freeWeightExercise);
        initDecrementWeightButton(freeWeightExercise);
    }

    private void setDescription(String description) {

        if (TextUtils.isEmpty(description)) {
            description = view.getResources()
                    .getString(R.string.vh_free_weight_default_description);
        }

        TextView descriptionView = itemView.findViewById(R.id.tv_description);
        descriptionView.setText(description);
    }

    private void setSets(int numSetsFinished, int numSetsTotal) {
        TextView setsRepsView = view.findViewById(R.id.tv_num_sets);

        // "Sets Completed"
        String setsCompleted = view.getResources().getString(R.string.vh_free_weight_sets_completed);
        String text = numSetsFinished + "/" + numSetsTotal + " " + setsCompleted;
        setsRepsView.setText(text);
    }



    private void initFinishSetButton(FreeWeightExercise freeWeightExercise) {
        if (freeWeightExercise.isTimerActive()) {
            ViewEnabler.setEnabled(finishSetButton, false);
        }
        else {
            ViewEnabler.setEnabled(finishSetButton, true);
        }

        finishSetButton.setOnClickListener(buttonView -> {

            int numSetsCompleted = freeWeightExercise.getNumSetsCompleted() + 1;
            freeWeightExercise.setNumSetsCompleted(numSetsCompleted);

            if (numSetsCompleted == freeWeightExercise.getNumberOfSets()) {
                freeWeightExercise.setCompleted(true);
                refreshItemInDataSet();
            }

            setSets(numSetsCompleted, freeWeightExercise.getNumberOfSets());

            ViewEnabler.setEnabled(finishSetButton, false);
            ViewEnabler.setEnabled(endTimerButton, true);

            super.startTimer();
            
        });

    }

    private void initEndTimerButton(FreeWeightExercise freeWeightExercise) {

        if (freeWeightExercise.isTimerActive()) {
            ViewEnabler.setEnabled(endTimerButton, true);
        }
        else {
            ViewEnabler.setEnabled(endTimerButton, false);
        }

        endTimerButton.setOnClickListener(
                buttonView -> super.endTimer()
        );
    }

    private void initIncrementWeightButton(FreeWeightExercise freeWeightExercise) {
        ImageView incrementWeightButton = view.findViewById(R.id.iv_weight_up);
        incrementWeightButton.setOnClickListener(imageView -> {
            int newWeight = freeWeightExercise.getAmountOfWeight() + 1;
            freeWeightExercise.setAmountOfWeight(newWeight);
            setWeightNumberView(newWeight);
        });
    }

    private void setWeightNumberView(int newWeight) {
        TextView weightNumberView = view.findViewById(R.id.tv_weight);
        weightNumberView.setText(String.valueOf(newWeight));
    }

    private void initDecrementWeightButton(FreeWeightExercise freeWeightExercise) {
        ImageView decrementWeightButton = view.findViewById(R.id.iv_weight_down);
        decrementWeightButton.setOnClickListener(imageView -> {
            int newWeight = freeWeightExercise.getAmountOfWeight() - 1;

            if (newWeight <= 0) {
                return;
            }

            freeWeightExercise.setAmountOfWeight(newWeight);
            setWeightNumberView(newWeight);

        });
    }

    private void setWeightUnitView(String weightUnit) {
        TextView weightUnitView = view.findViewById(R.id.tv_weight_unit);
        weightUnitView.setText(weightUnit);
    }

    @Override
    protected int[] getCollapsibleViewResourceIds() {
        return new int[] {
                R.id.iv_weight_down,
                R.id.iv_weight_up,
                R.id.tv_weight,
                R.id.tv_weight_unit,
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
