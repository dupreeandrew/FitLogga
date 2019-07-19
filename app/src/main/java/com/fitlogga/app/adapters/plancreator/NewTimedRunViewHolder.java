package com.fitlogga.app.adapters.plancreator;

import android.view.View;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.TimedRunExercise;
import com.fitlogga.app.utils.Time;
import com.google.android.material.textfield.TextInputEditText;

public class NewTimedRunViewHolder extends NewExerciseViewHolder {

    private View view;

    NewTimedRunViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    @Override
    public void manifest(Exercise exercise) {
        TimedRunExercise timedRunExercise = (TimedRunExercise)exercise;

        int numSecondsTotal = timedRunExercise.getSeconds();
        String subtitle = Time.toHHMMFormat(numSecondsTotal) + " timed run";
        String description = timedRunExercise.getDescription();


        setTitle("Timed Run Exercise");
        setSubtitle(subtitle);
        setDescription(description);
        setTimeFields(numSecondsTotal);

    }

    private void setDescription(String description) {
        TextInputEditText inputDescription = view.findViewById(R.id.input_exercise_description);
        inputDescription.setText(description);
    }

    private void setTimeFields(int numSecondsTotal) {

        int numMinutes = numSecondsTotal / 60;
        int numSeconds = numSecondsTotal % 60;

        TextInputEditText inputMinutes = view.findViewById(R.id.input_run_minutes);
        TextInputEditText inputSeconds = view.findViewById(R.id.input_run_seconds);

        inputMinutes.setText(String.valueOf(numMinutes));
        inputSeconds.setText(String.valueOf(numSeconds));

    }

    @Override
    protected int[] getCollapsibleViewResourceIds() {
        return new int[]{
                R.id.input_run_minutes_layout,
                R.id.input_run_seconds_layout,
                R.id.input_exercise_description_layout,
                R.id.vh_separator,
                R.id.btn_delete,
                R.id.btn_close
        };
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    protected void tryToSave(SaveListener saveListener, String uuid) {
        TextInputEditText inputMinutesView = view.findViewById(R.id.input_run_minutes);
        TextInputEditText inputSecondsView = view.findViewById(R.id.input_run_seconds);
        TextInputEditText inputDescriptionView = view.findViewById(R.id.input_exercise_description);

        String inputMinutesString = inputMinutesView.getText().toString();
        String inputSecondsString = inputSecondsView.getText().toString();
        String inputDescriptionString = inputDescriptionView.getText().toString();

        boolean isSaveable = true;

        int totalRunSeconds = Time.getSeconds(inputMinutesString, inputSecondsString);

        if (totalRunSeconds <= 0) {
            applyBadNumberError(R.id.input_run_seconds_layout);
            applyErrorBackground(R.id.input_run_minutes_layout);
            isSaveable = false;
        }

        if (!isSaveable) {
            saveListener.onFail();
            return;
        }

        Exercise builtExercise
                = buildExercise(totalRunSeconds, inputDescriptionString, uuid);
        saveListener.onSave(builtExercise);

    }

    private Exercise buildExercise(int totalRunSeconds, String inputDescriptionString, String uuid) {
        return new TimedRunExercise(inputDescriptionString, totalRunSeconds, false,  uuid);
    }


}
