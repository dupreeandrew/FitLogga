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
    private RepetitionExercise repetitionExercise;

    NewRepetitionViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void manifest(Exercise exercise) {
        this.repetitionExercise = (RepetitionExercise)exercise;

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

        int inputNumSets = Integer.parseInt(inputNumSetsString);
        int inputNumReps = Integer.parseInt(inputNumRepsString);
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
                inputNumSets, inputNumReps, totalRestSeconds)) {
            saveListener.onNothingChanged();
            return;
        }

        Exercise builtExercise
                = buildExercise(inputNameString, inputDescriptionString, inputNumSets,
                inputNumReps, totalRestSeconds);
        saveListener.onSave(builtExercise);

    }

    private boolean changesWereMade(String inputNameString, String inputDescriptionString,
                                    int inputNumSets, int inputNumReps, int totalRestSeconds) {

        if (!repetitionExercise.getName().equals(inputNameString)) {
            return true;
        }

        if (!repetitionExercise.getDescription().equals(inputDescriptionString)) {
            return true;
        }

        if (inputNumSets != repetitionExercise.getNumberOfSets()) {
            return true;
        }

        if (inputNumReps != repetitionExercise.getNumberOfRepetitions()) {
            return true;
        }

        if (totalRestSeconds != repetitionExercise.getRestTimeBetweenSets()) {
            return true;
        }

        return false;

    }

    private Exercise buildExercise(String inputNameString, String inputDescriptionString, int inputNumSets,
                                   int inputNumReps, int totalRestSeconds) {


        return new RepetitionExercise.Builder()
                .setName(inputNameString)
                .setDescription(inputDescriptionString)
                .setNumberOfSets(inputNumSets)
                .setNumberOfReps(inputNumReps)
                .setRestTimeBetweenSets(totalRestSeconds)
                .setUuid(repetitionExercise.getUuid())
                .build();

    }


}
