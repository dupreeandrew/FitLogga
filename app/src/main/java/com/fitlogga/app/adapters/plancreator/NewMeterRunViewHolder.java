package com.fitlogga.app.adapters.plancreator;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.MeterRunExercise;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NewMeterRunViewHolder extends NewExerciseViewHolder {

    private View view;
    private MeterRunExercise meterRunExercise;

    NewMeterRunViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void manifest(Exercise exercise) {
        this.meterRunExercise = (MeterRunExercise)exercise;

        String description = meterRunExercise.getDescription();
        int distance = meterRunExercise.getDistance();
        String units = meterRunExercise.getDistanceUnits();

        setTitle("Meter Run Exercise");
        setSubtitle(distance + " " + units);
        setDistanceField(distance);
        setDistanceUnitsField(units);
        setDescriptionField(description);
    }

    private void setDistanceField(int distance) {
        if (distance != 0) {
            TextInputEditText inputDistance = view.findViewById(R.id.input_run_distance);
            inputDistance.setText(String.valueOf(distance));
        }
    }

    private void setDistanceUnitsField(String units) {
        TextInputEditText inputUnits = view.findViewById(R.id.input_run_units);
        inputUnits.setText(units);
    }

    private void setDescriptionField(String description) {
        TextInputEditText inputDescription = view.findViewById(R.id.input_exercise_description);
        inputDescription.setText(description);
    }

    @Override
    protected int[] getCollapsibleViewResourceIds() {
        return new int[]{
                R.id.input_run_distance_layout,
                R.id.input_run_units_layout,
                R.id.input_exercise_description_layout,
                R.id.vh_separator,
                R.id.btn_delete,
                R.id.btn_close
        };
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void tryToSave(SaveListener saveListener) {
        TextInputEditText inputDistanceView = view.findViewById(R.id.input_run_distance);
        TextInputEditText inputUnitsView = view.findViewById(R.id.input_run_units);
        TextInputEditText inputDescriptionView = view.findViewById(R.id.input_exercise_description);

        String inputDistanceString = inputDistanceView.getText().toString();
        String inputUnitsString = inputUnitsView.getText().toString();
        String inputDescriptionString = inputDescriptionView.getText().toString();

        boolean isSaveable = true;

        if (TextUtils.isEmpty(inputDistanceString)) {
            TextInputLayout inputMinutesLayout = view.findViewById(R.id.input_run_distance_layout);
            inputMinutesLayout.setError("Missing distance");
            isSaveable = false;
        }

        if (TextUtils.isEmpty(inputUnitsString)) {
            TextInputLayout inputMinutesLayout = view.findViewById(R.id.input_run_units_layout);
            inputMinutesLayout.setError("Missing units");
            isSaveable = false;
        }

        if (!isSaveable) {
            saveListener.onFail();
            return;
        }

        int inputDistance = Integer.parseInt(inputDistanceString);

        if (!changesWereMade(inputDistance, inputUnitsString, inputDescriptionString)) {
            saveListener.onNothingChanged();
            return;
        }

        Exercise builtExercise
                = buildExercise(inputDistance, inputUnitsString, inputDescriptionString, meterRunExercise.getUuid());
        saveListener.onSave(builtExercise);

    }

    private boolean changesWereMade(int inputDistance, String inputUnitsString,
                                    String inputDescriptionString) {
        if (meterRunExercise.getDistance() != inputDistance) {
            return true;
        }

        if (!meterRunExercise.getDistanceUnits().equals(inputUnitsString)) {
            return true;
        }

        if (!meterRunExercise.getDescription().equals(inputDescriptionString)) {
            return true;
        }

        return false;

    }

    private Exercise buildExercise(int inputDistance, String inputUnitsString, String inputDescriptionString, String uuid) {
        return new MeterRunExercise(inputDescriptionString, inputDistance, inputUnitsString, false, uuid);
    }

}
