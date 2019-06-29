package com.fitlogga.app.adapters.plancreator;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.RestExercise;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NewRestViewHolder extends NewExerciseViewHolder {

    private View view;

    NewRestViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void manifest(Exercise exercise) {
        RestExercise restExercise = (RestExercise)exercise;

        int numSecsOfRestTotal = restExercise.getAmountOfTimeToRest();
        int numMinRest = numSecsOfRestTotal / 60;
        int numSecRest = numSecsOfRestTotal % 60;

        String numSecRestString;
        if (numSecRest < 10) {
            numSecRestString = "0" + numSecRest;
        }
        else {
            numSecRestString = String.valueOf(numSecRest);
        }

        setTitle("Rest Break");
        setSubtitle(numMinRest + ":" + numSecRestString + " rest break");
        setNumMinRestField(numMinRest);
        setNumSecRestField(numSecRestString);

    }

    private void setNumMinRestField(int numSecsOfRestTotal) {
        TextInputEditText inputNumMinRest = view.findViewById(R.id.input_rest_time_minutes);
        inputNumMinRest.setText(String.valueOf(numSecsOfRestTotal));
    }

    private void setNumSecRestField(String numSecRest) {
        TextInputEditText inputNumSecRest = view.findViewById(R.id.input_rest_time_seconds);
        inputNumSecRest.setText(String.valueOf(numSecRest));
    }

    @Override
    protected View[] getCollapsibleViews() {
        return new View[] {
                view.findViewById(R.id.input_rest_time_minutes_layout),
                view.findViewById(R.id.input_rest_time_seconds_layout),
                view.findViewById(R.id.btn_delete)
        };
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void tryToSave(SaveListener listener) {

        TextInputEditText inputRestMinutesView = view.findViewById(R.id.input_rest_time_minutes);
        TextInputEditText inputRestSecondsView = view.findViewById(R.id.input_rest_time_seconds);

        String inputRestMinutesString = inputRestMinutesView.getText().toString();
        String inputRestSecondsString = inputRestSecondsView.getText().toString();

        boolean isSaveable = true;

        if (TextUtils.isEmpty(inputRestMinutesString)) {
            TextInputLayout inputMinutesLayout = view.findViewById(R.id.input_rest_time_minutes_layout);
            inputMinutesLayout.setError("Missing minutes");
            isSaveable = false;
        }

        if (TextUtils.isEmpty(inputRestSecondsString)) {
            TextInputLayout inputMinutesLayout = view.findViewById(R.id.input_rest_time_seconds_layout);
            inputMinutesLayout.setError("Missing seconds");
            isSaveable = false;
        }

        if (!isSaveable) {
            listener.onFail();
            return;
        }

        Exercise builtExercise = buildExercise(inputRestMinutesString, inputRestSecondsString);
        listener.onSave(builtExercise);

    }

    private Exercise buildExercise(String inputRestMinutesString, String inputRestSecondsString) {
        int restMinutes = Integer.parseInt(inputRestMinutesString);
        int restSeconds = Integer.parseInt(inputRestSecondsString);
        int restTotal = (restMinutes * 60) + restSeconds;
        return new RestExercise(restTotal);
    }
}
