package com.fitlogga.app.adapters.plancreator;

import android.view.View;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.RestExercise;
import com.fitlogga.app.utils.Time;
import com.google.android.material.textfield.TextInputEditText;

public class NewRestViewHolder extends NewExerciseViewHolder {

    private View view;

    NewRestViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void manifest(Exercise exercise) {
        RestExercise restExercise = (RestExercise)exercise;

        int numSecsOfRestTotal = restExercise.getSecondsOfRest();
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
    protected int[] getCollapsibleViewResourceIds() {
        return new int[] {
                R.id.input_rest_time_minutes_layout,
                R.id.input_rest_time_seconds_layout,
                R.id.btn_delete,
                R.id.btn_close
        };
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void tryToSave(SaveListener listener) {

        TextInputEditText inputRestMinutesView = view.findViewById(R.id.input_rest_time_minutes);
        TextInputEditText inputRestSecondsView = view.findViewById(R.id.input_rest_time_seconds);

        String inputRestMinutesString = inputRestMinutesView.getText().toString();
        String inputRestSecondsString = inputRestSecondsView.getText().toString();
        int totalRestSeconds = Time.getSeconds(inputRestMinutesString, inputRestSecondsString);

        boolean isSaveable = true;

        if (totalRestSeconds <= 0) {
            applyErrorBackground(R.id.input_rest_time_minutes_layout);
            applyBadNumberError(R.id.input_rest_time_seconds_layout);
            isSaveable = false;
        }

        if (!isSaveable) {
            listener.onFail();
            return;
        }

        Exercise builtExercise = buildExercise(totalRestSeconds);
        listener.onSave(builtExercise);

    }

    private Exercise buildExercise(int totalRestSeconds) {
        return new RestExercise(totalRestSeconds);
    }
}
