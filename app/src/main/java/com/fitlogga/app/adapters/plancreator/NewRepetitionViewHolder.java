package com.fitlogga.app.adapters.plancreator;

import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.LimitedArrayAdapter;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.RepetitionExercise;
import com.fitlogga.app.utils.Time;
import com.google.android.material.textfield.TextInputEditText;

public class NewRepetitionViewHolder extends NewExerciseViewHolder {

    private View view;

    NewRepetitionViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void manifest(Exercise exercise) {
        RepetitionExercise repetitionExercise = (RepetitionExercise)exercise;

        String name = repetitionExercise.getName();
        String description = repetitionExercise.getDescription();
        int numSets = repetitionExercise.getNumberOfSets();
        int numReps = repetitionExercise.getNumberOfRepetitions();
        int numSecsRestTotal = repetitionExercise.getRestTimeBetweenSets();
        int numMinRest = numSecsRestTotal / 60;
        int numSecsRest = numSecsRestTotal % 60;

        setTitle(name);
        setSubtitle(numSets + "x" + numReps + " repetition exercise");
        setExerciseName(name);
        setDescription(description);
        setNumSets(numSets);
        setNumReps(numReps);
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
        TextInputEditText inputSets = view.findViewById(R.id.input_num_sets);
        inputSets.setText(String.valueOf(numSets));
    }

    private void setNumReps(int numReps) {
        TextInputEditText inputNumReps = view.findViewById(R.id.input_num_reps);
        inputNumReps.setText(String.valueOf(numReps));
    }

    private void setNumMinRest(int numMinRest) {
        TextInputEditText inputNumSecsRest = view.findViewById(R.id.input_rest_time_minutes);
        inputNumSecsRest.setText(String.valueOf(numMinRest));
    }

    private void setNumSecsRest(int numSecsRest) {
        TextInputEditText inputNumSecsRest = view.findViewById(R.id.input_rest_time_seconds);
        inputNumSecsRest.setText(String.valueOf(numSecsRest));
    }

    private void initExerciseNameAutoComplete() {
        String[] possibleAutoCompletes = view.getResources()
                .getStringArray(R.array.repetition_execises);
        AutoCompleteTextView inputExerciseName = view.findViewById(R.id.input_exercise_name);

        ArrayAdapter<String> adapter = new LimitedArrayAdapter<>(view.getContext(), R.layout.vh_new_popup_item,
                possibleAutoCompletes, GlobalSettings.MAX_AUTO_COMPLETION_COUNT);
        inputExerciseName.setAdapter(adapter);
    }

    @Override
    protected int[] getCollapsibleViewResourceIds() {
        return new int[]{
                R.id.input_exercise_name_layout,
                R.id.input_exercise_description_layout,
                R.id.input_num_sets_layout,
                R.id.input_num_reps_layout,
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
        TextInputEditText inputRestMinutesView = view.findViewById(R.id.input_rest_time_minutes);
        TextInputEditText inputRestSecondsView = view.findViewById(R.id.input_rest_time_seconds);

        String inputNameString = inputNameView.getText().toString();
        String inputDescriptionString = inputDescriptionView.getText().toString();
        String inputNumSetsString = inputNumSetsView.getText().toString();
        String inputNumRepsString = inputNumRepsView.getText().toString();
        String inputRestMinutesString = inputRestMinutesView.getText().toString();
        String inputRestSecondsString = inputRestSecondsView.getText().toString();
        int totalRestSeconds = Time.getSeconds(inputRestMinutesString, inputRestSecondsString);

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

        if (totalRestSeconds <= 0) {
            applyErrorBackground(R.id.input_rest_time_minutes_layout);
            applyBadNumberError(R.id.input_rest_time_seconds_layout);
            isSaveable = false;
        }

        if (!isSaveable) {
            saveListener.onFail();
            return;
        }

        Exercise builtExercise
                = buildExercise(inputNameString, inputDescriptionString, inputNumSetsString,
                inputNumRepsString, inputRestMinutesString, inputRestSecondsString);
        saveListener.onSave(builtExercise);

    }

    private Exercise buildExercise(String inputNameString, String inputDescriptionString, String inputNumSetsString,
                                   String inputNumRepsString, String inputRestMinutesString,
                                   String inputRestSecondsString) {

        int numSets = Integer.parseInt(inputNumSetsString);
        int numReps = Integer.parseInt(inputNumRepsString);
        int numRestMinutes = Integer.parseInt(inputRestMinutesString);
        int numRestSeconds = Integer.parseInt(inputRestSecondsString);
        int numRestTotal = (numRestMinutes * 60) + numRestSeconds;

        return new RepetitionExercise.Builder()
                .setName(inputNameString)
                .setDescription(inputDescriptionString)
                .setNumberOfSets(numSets)
                .setNumberOfReps(numReps)
                .setRestTimeBetweenSets(numRestTotal)
                .build();

    }


}
