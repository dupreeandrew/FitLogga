package com.fitlogga.app.adapters.training.viewholders;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.FreeWeightExercise;
import com.fitlogga.app.utils.CountDownTimerPlus;
import com.fitlogga.app.utils.Time;
import com.fitlogga.app.viewmods.ViewEnabler;

public class FreeWeightViewHolder extends ExerciseViewHolder {

    private View view;
    private CountDownTimerPlus timer;
    private Button finishSetButton;
    private Button endTimerButton;

    public FreeWeightViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;

        finishSetButton = view.findViewById(R.id.btn_complete_set);
        endTimerButton = view.findViewById(R.id.btn_end_timer);
    }

    @Override
    public void onManifest(Exercise exercise) {
        FreeWeightExercise freeWeightExercise = (FreeWeightExercise)exercise;
        String name = freeWeightExercise.getName();
        String description = freeWeightExercise.getDescription();
        int numSetsCompleted = freeWeightExercise.getNumSetsCompleted();
        int numSets = freeWeightExercise.getNumberOfSets();
        int numReps = freeWeightExercise.getNumberOfRepetitions();
        int numWeight = freeWeightExercise.getAmountOfWeight();
        String weightUnit = freeWeightExercise.getAmountOfWeightUnits();

        String subtitle = numSets + "x" + numReps + " Repetition Exercise";

        setTitle(name);
        setSubtitle(subtitle);
        setDescription(description);
        setSets(numSetsCompleted, numSets);
        setWeightNumberView(numWeight);
        setWeightUnitView(weightUnit);

        initTimer(freeWeightExercise);
        initEndTimerButton();
        initFinishSetButton(freeWeightExercise);
        initIncrementWeightButton(freeWeightExercise);
        initDecrementWeightButton(freeWeightExercise);

    }

    private void setDescription(String description) {

        if (TextUtils.isEmpty(description)) {
            description = "Remember, safety first! Lift only as much as your body allows!";
        }

        TextView descriptionView = itemView.findViewById(R.id.tv_description);
        descriptionView.setText(description);
    }

    private void setSets(int numSetsFinished, int numSetsTotal) {
        TextView setsRepsView = view.findViewById(R.id.tv_num_sets);
        String text = numSetsFinished + "/" + numSetsTotal + " Sets Completed";
        setsRepsView.setText(text);
    }

    private void initTimer(FreeWeightExercise freeWeightExercise) {

        if (timer != null && !timer.isPaused()) return;

        ProgressBar timerProgressBar = view.findViewById(R.id.pb_progress);
        timerProgressBar.setMax(5000);

        final int MILLISECONDS_OF_TIMER = freeWeightExercise.getRestTimeBetweenSets() * 1000;
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

    private void initFinishSetButton(FreeWeightExercise freeWeightExercise) {
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
            timer.reset();
            timer.resume();
            
        });

    }

    private void initEndTimerButton() {
        endTimerButton.setOnClickListener(
                buttonView -> timer.fadeEnd()
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
