package com.fitlogga.app.adapters.plancreator;

import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.LimitedArrayAdapter;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.FreeWeightExercise;
import com.fitlogga.app.utils.Time;
import com.google.android.material.textfield.TextInputEditText;

public class NewFreeWeightViewHolder extends NewExerciseViewHolder {

    private View view;
    private FreeWeightExercise freeWeightExercise;

    NewFreeWeightViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void manifest(Exercise exercise) {
        this.freeWeightExercise = (FreeWeightExercise)exercise;

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

        initExerciseNameAutoComplete();
        
    }

    private void setExerciseName(String name) {
        AutoCompleteTextView inputExerciseName = view.findViewById(R.id.input_exercise_name);
        inputExerciseName.setText(name);
    }

    private void setDescription(String description) {
        TextInputEditText inputDescription = view.findViewById(R.id.input_exercise_description);
        inputDescription.setText(description);
    }

    private void setNumSets(int numSets) {
        if (numSets != 0) {
            TextInputEditText inputSets = view.findViewById(R.id.input_num_sets);
            inputSets.setText(String.valueOf(numSets));
        }
    }

    private void setNumReps(int numReps) {
        if (numReps != 0) {
            TextInputEditText inputNumReps = view.findViewById(R.id.input_num_reps);
            inputNumReps.setText(String.valueOf(numReps));
        }
    }

    private void setNumWeight(int numWeight) {
        if (numWeight != 0) {
            TextInputEditText inputNumWeight = view.findViewById(R.id.input_num_weight);
            inputNumWeight.setText(String.valueOf(numWeight));
        }
    }

    private void setNumWeightUnits(String units) {
        TextInputEditText inputNumWeightUnits = view.findViewById(R.id.input_num_weight_units);
        inputNumWeightUnits.setText(units);
    }

    private void setNumMinRest(int numMinRest) {
        if (numMinRest != 0) {
            TextInputEditText inputNumSecsRest = view.findViewById(R.id.input_rest_time_minutes);
            inputNumSecsRest.setText(String.valueOf(numMinRest));
        }
    }

    private void setNumSecsRest(int numSecsRest) {
        if (numSecsRest != 0) {
            TextInputEditText inputNumSecsRest = view.findViewById(R.id.input_rest_time_seconds);
            inputNumSecsRest.setText(String.valueOf(numSecsRest));
        }
    }

    private void initExerciseNameAutoComplete() {
        String[] possibleAutoCompletes = view.getResources()
                .getStringArray(R.array.free_weight_exercises);
        AutoCompleteTextView inputExerciseName = view.findViewById(R.id.input_exercise_name);
        ArrayAdapter<String> adapter = new LimitedArrayAdapter<>(view.getContext(), R.layout.vh_new_popup_item,
                possibleAutoCompletes, GlobalSettings.MAX_AUTO_COMPLETION_COUNT);
        inputExerciseName.setAdapter(adapter);
        inputExerciseName.dismissDropDown();
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
                R.id.vh_separator,
                R.id.btn_delete,
                R.id.btn_close
        };
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void tryToSave(SaveListener saveListener) {
        AutoCompleteTextView inputNameView = view.findViewById(R.id.input_exercise_name);
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

        boolean isSaveable = true;

        resetRequiredErrors();

        if (TextUtils.isEmpty(inputNameString)) {
            applyRequiredError(R.id.input_exercise_name_layout);
            isSaveable = false;
        }
        else if (isNameTooLong(inputNameString)) {
            applyNameTooLongError(R.id.input_exercise_name_layout);
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

        int inputNumSets = Integer.parseInt(inputNumSetsString);
        int inputNumReps = Integer.parseInt(inputNumRepsString);
        int inputNumWeight = Integer.parseInt(inputNumWeightString);
        int totalRestSeconds = Time.getSeconds(inputRestMinutesString, inputRestSecondsString);

        if (totalRestSeconds <= 0) {
            applyErrorBackground(R.id.input_rest_time_minutes_layout);
            applyBadNumberError(R.id.input_rest_time_seconds_layout);
            isSaveable = false;
        }


        if (!isSaveable) {
            saveListener.onFail();
            return;
        }

        if (!changesWereMade(inputNameString, inputDescriptionString,
                inputNumSets, inputNumReps, inputNumWeight, totalRestSeconds)) {
            saveListener.onNothingChanged();
            return;
        }

        Exercise builtExercise
                = buildExercise(inputNameString, inputDescriptionString, inputNumSets,
                inputNumReps, inputNumWeight, inputUnitsString,
                totalRestSeconds);
        saveListener.onSave(builtExercise);

    }

    private boolean changesWereMade(String inputNameString, String inputDescriptionString,
                                    int inputNumSets, int inputNumReps, int inputNumWeight, int totalRestSeconds) {

        if (!freeWeightExercise.getName().equals(inputNameString)) {
            return true;
        }

        if (!freeWeightExercise.getDescription().equals(inputDescriptionString)) {
            return true;
        }

        if (inputNumSets != freeWeightExercise.getNumberOfSets()) {
            return true;
        }

        if (inputNumReps != freeWeightExercise.getNumberOfRepetitions()) {
            return true;
        }

        if (inputNumWeight != freeWeightExercise.getAmountOfWeight()) {
            return true;
        }

        if (totalRestSeconds != freeWeightExercise.getRestTimeBetweenSets()) {
            return true;
        }

        return false;

    }

    private Exercise buildExercise(String inputNameString, String inputDescriptionString,
                                   int inputNumSets, int inputNumReps,
                                   int inputNumWeight, String inputUnitsString,
                                   int totalRestSeconds) {

        return new FreeWeightExercise.Builder()
                .setName(inputNameString)
                .setDescription(inputDescriptionString)
                .setNumberOfSets(inputNumSets)
                .setNumberOfReps(inputNumReps)
                .setAmountOfWeight(inputNumWeight)
                .setAmountOfWeightUnits(inputUnitsString)
                .setRestTimeBetweenSets(totalRestSeconds)
                .setUuid(freeWeightExercise.getUuid())
                .build();

    }
}
