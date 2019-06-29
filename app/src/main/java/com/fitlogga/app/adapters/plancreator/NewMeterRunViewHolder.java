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

    NewMeterRunViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void manifest(Exercise exercise) {
        MeterRunExercise meterRunExercise = (MeterRunExercise)exercise;

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
        TextInputEditText inputDistance = view.findViewById(R.id.input_run_distance);
        inputDistance.setText(String.valueOf(distance));
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
    protected View[] getCollapsibleViews() {
        return new View[]{
                view.findViewById(R.id.input_run_distance_layout),
                view.findViewById(R.id.input_run_units_layout),
                view.findViewById(R.id.input_exercise_description_layout),
                view.findViewById(R.id.btn_delete)
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

        Exercise builtExercise
                = buildExercise(inputDistanceString, inputUnitsString, inputDescriptionString);
        saveListener.onSave(builtExercise);

    }

    private Exercise buildExercise(String inputDistanceString, String inputUnitsString, String inputDescriptionString) {
        int inputDistance = Integer.parseInt(inputDistanceString);
        return new MeterRunExercise(inputDescriptionString, inputDistance, inputUnitsString);
    }

}
