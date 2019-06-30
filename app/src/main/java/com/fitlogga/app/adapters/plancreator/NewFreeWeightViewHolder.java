package com.fitlogga.app.adapters.plancreator;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.FreeWeightExercise;
import com.fitlogga.app.utils.Time;
import com.google.android.material.textfield.TextInputEditText;

public class NewFreeWeightViewHolder extends NewExerciseViewHolder {

    private View view;

    NewFreeWeightViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void manifest(Exercise exercise) {
        FreeWeightExercise freeWeightExercise = (FreeWeightExercise)exercise;

        String name = freeWeightExercise.getName();
        String description = freeWeightExercise.getDescription();
        int numSets = freeWeightExercise.getNumberOfSets();
        int numReps = freeWeightExercise.getNumberOfRepetitions();
        int numWeight = freeWeightExercise.getAmountOfWeight();
        String numWeightUnits = freeWeightExercise.getAmountOfWeightUnits();
        int numSecsRestTotal = freeWeightExercise.getRestTimeBetweenSets();
        int numMinRest = numSecsRestTotal / 60;
        int numSecsRest = numSecsRestTotal % 60;

        setTitle(name);
        setSubtitle(numSets + "x" + numReps + " @ " + numWeight + numWeightUnits);
        setExerciseName(name);
        setDescription(description);
        setNumSets(numSets);
        setNumReps(numReps);
        setNumWeight(numWeight);
        setNumWeightUnits(numWeightUnits);
        setNumMinRest(numMinRest);
        setNumSecsRest(numSecsRest);
        
    }

    private void setExerciseName(String name) {
        TextInputEditText inputExerciseName = view.findViewById(R.id.input_exercise_name);
        inputExerciseName.setText(name);
    }

    private void setDescription(String description) {
        TextInputEditText inputDescription = view.findViewById(R.id.input_exercise_description);
        inputDescription.setText(description);
    }

    private void setNumSets(int numSets) {
        TextInputEditText inputSets = view.findViewById(R.id.input_num_sets);
        inputSets.setText(String.valueOf(numSets));
    }

    private void setNumReps(int numReps) {
        TextInputEditText inputNumReps = view.findViewById(R.id.input_num_reps);
        inputNumReps.setText(String.valueOf(numReps));
    }

    private void setNumWeight(int numWeight) {
        TextInputEditText inputNumWeight = view.findViewById(R.id.input_num_weight);
        inputNumWeight.setText(String.valueOf(numWeight));
    }

    private void setNumWeightUnits(String units) {
        TextInputEditText inputNumWeightUnits = view.findViewById(R.id.input_num_weight_units);
        inputNumWeightUnits.setText(units);
    }

    private void setNumMinRest(int numMinRest) {
        TextInputEditText inputNumSecsRest = view.findViewById(R.id.input_rest_time_minutes);
        inputNumSecsRest.setText(String.valueOf(numMinRest));
    }

    private void setNumSecsRest(int numSecsRest) {
        TextInputEditText inputNumSecsRest = view.findViewById(R.id.input_rest_time_seconds);
        inputNumSecsRest.setText(String.valueOf(numSecsRest));
    }

    @Override
    protected int[] getCollapsibleViewResourceIds() {
        return new int[] {
                R.id.input_exercise_name_layout,
                R.id.input_exercise_description_layout,
                R.id.input_num_sets_layout,
                R.id.input_num_reps_layout,
                R.id.input_num_weight_layout,
                R.id.input_num_weight_units_layout,
                R.id.input_rest_time_minutes_layout,
                R.id.input_rest_time_seconds_layout,
                R.id.btn_delete
        };
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void tryToSave(SaveListener saveListener) {
        TextInputEditText inputNameView = view.findViewById(R.id.input_exercise_name);
        TextInputEditText inputDescriptionView = view.findViewById(R.id.input_exercise_description);
        TextInputEditText inputNumSetsView = view.findViewById(R.id.input_num_sets);
        TextInputEditText inputNumRepsView = view.findViewById(R.id.input_num_reps);
        TextInputEditText inputNumWeightView = view.findViewById(R.id.input_num_weight);
        TextInputEditText inputUnitsView = view.findViewById(R.id.input_num_weight_units);
        TextInputEditText inputRestMinutesView = view.findViewById(R.id.input_rest_time_minutes);
        TextInputEditText inputRestSecondsView = view.findViewById(R.id.input_rest_time_seconds);

        String inputNameString = inputNameView.getText().toString();
        String inputDescriptionString = inputDescriptionView.getText().toString();
        String inputNumSetsString = inputNumSetsView.getText().toString();
        String inputNumRepsString = inputNumRepsView.getText().toString();
        String inputNumWeightString = inputNumWeightView.getText().toString();
        String inputUnitsString = inputUnitsView.getText().toString();
        String inputRestMinutesString = inputRestMinutesView.getText().toString();
        String inputRestSecondsString = inputRestSecondsView.getText().toString();
        int totalRestSeconds = Time.getSeconds(inputRestMinutesString, inputRestSecondsString);

        boolean isSaveable = true;


        resetRequiredErrors();

        if (TextUtils.isEmpty(inputNameString)) {
            applyRequiredError(R.id.input_exercise_name_layout);
            isSaveable = false;
        }

        if (TextUtils.isEmpty(inputNumSetsString)) {
            applyRequiredError(R.id.input_num_sets_layout);
            isSaveable = false;
        }
        else if (isUnacceptableNumber(inputNumSetsString)) {
            applyBadNumberError(R.id.input_num_sets_layout);
            isSaveable = false;
        }

        if (TextUtils.isEmpty(inputNumRepsString)) {
            applyRequiredError(R.id.input_num_reps_layout);
            isSaveable = false;
        }
        else if (isUnacceptableNumber(inputNumRepsString)) {
            applyBadNumberError(R.id.input_num_reps_layout);
            isSaveable = false;
        }

        if (TextUtils.isEmpty(inputNumWeightString)) {
            applyRequiredError(R.id.input_num_weight_layout);
            isSaveable = false;
        }
        else if (isUnacceptableNumber(inputNumWeightString)) {
            applyBadNumberError(R.id.input_num_weight_layout);
            isSaveable = false;
        }

        if (TextUtils.isEmpty(inputUnitsString)) {
            applyRequiredError(R.id.input_num_weight_units_layout);
            isSaveable = false;
        }

        if (totalRestSeconds <= 0) {
            applyErrorBackground(R.id.input_rest_time_minutes_layout);
            applyBadNumberError(R.id.input_rest_time_seconds_layout);
            isSaveable = false;
        }


        //

        if (!isSaveable) {
            saveListener.onFail();
            return;
        }

        Exercise builtExercise
                = buildExercise(inputNameString, inputDescriptionString, inputNumSetsString,
                inputNumRepsString, inputNumWeightString, inputUnitsString,
                inputRestMinutesString, inputRestSecondsString);
        saveListener.onSave(builtExercise);

    }

    private Exercise buildExercise(String inputNameString, String inputDescriptionString,
                                   String inputNumSetsString, String inputNumRepsString,
                                   String inputNumWeightString, String inputUnitsString,
                                   String inputRestMinutesString, String inputRestSecondsString) {

        int numSets = Integer.parseInt(inputNumSetsString);
        int numReps = Integer.parseInt(inputNumRepsString);
        int numRestMinutes = Integer.parseInt(inputRestMinutesString);
        int numRestSeconds = Integer.parseInt(inputRestSecondsString);
        int numWeight = Integer.parseInt(inputNumWeightString);
        int numRestTotal = (numRestMinutes * 60) + numRestSeconds;

        return new FreeWeightExercise.Builder()
                .setName(inputNameString)
                .setDescription(inputDescriptionString)
                .setNumberOfSets(numSets)
                .setNumberOfReps(numReps)
                .setAmountOfWeight(numWeight)
                .setAmountOfWeightUnits(inputUnitsString)
                .setRestTimeBetweenSets(numRestTotal)
                .build();

    }
}
