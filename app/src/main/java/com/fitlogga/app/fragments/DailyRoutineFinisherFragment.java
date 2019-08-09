package com.fitlogga.app.fragments;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fitlogga.app.R;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.plan.PlanCreator;
import com.fitlogga.app.models.plan.PlanEditor;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.PlanSummary;
import com.fitlogga.app.models.plan.log.SQLLogWriter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class DailyRoutineFinisherFragment extends Fragment {

    private EnumMap<Day, List<Exercise>> dailyRoutineMap;
    private PlanSummary planSummary;

    public DailyRoutineFinisherFragment() {
        // Required empty public constructor
    }

    // Plan Summary will be non-null IF user is editing a plan, and not editing.
    public DailyRoutineFinisherFragment(EnumMap<Day, List<Exercise>> dailyRoutineMap, @Nullable PlanSummary planSummary) {
        this.dailyRoutineMap = dailyRoutineMap;
        this.planSummary = planSummary;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_routine_finisher, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preFillFields(view);
        initCreatePlanButton(view);

    }

    private void preFillFields(View view) {

        if (planSummary == null) {
            return;
        }

        TextInputEditText inputNameView = view.findViewById(R.id.input_plan_name);
        TextInputEditText inputDescView = view.findViewById(R.id.input_plan_description);
        inputNameView.setText(planSummary.getName());
        inputDescView.setText(planSummary.getDescription());

        String currentPlanName = PlanReader.getCurrentPlanName();
        if (planSummary.getName().equals(currentPlanName)) {
            disableSetAsActiveCheckbox(view);
        }
    }

    private void disableSetAsActiveCheckbox(View view) {
        CheckBox setAsActiveCheckBox = view.findViewById(R.id.cb_set_as_current_plan);
        setAsActiveCheckBox.setChecked(true);
        setAsActiveCheckBox.setEnabled(false);
    }

    private void initCreatePlanButton(View view) {
        Button createPlanButton = view.findViewById(R.id.btn_create_plan);
        createPlanButton.setOnClickListener(buttonView -> {
            resetErrors(view);
            tryToCreatePlan(view);
        });
    }

    private void resetErrors(View view) {
        TextInputLayout inputNameLayout = view.findViewById(R.id.input_plan_name_layout);
        TextInputLayout inputDescLayout = view.findViewById(R.id.input_plan_description_layout);
        inputNameLayout.setErrorEnabled(false);
        inputDescLayout.setErrorEnabled(false);
    }

    private void tryToCreatePlan(View view) {
        TextInputEditText inputNameView = view.findViewById(R.id.input_plan_name);
        TextInputEditText inputDescView = view.findViewById(R.id.input_plan_description);

        String inputNameString = inputNameView.getText().toString();
        String inputDescString = inputDescView.getText().toString();

        boolean canCreate = true;

        if (TextUtils.isEmpty(inputNameString)) {
            TextInputLayout inputNameLayout = view.findViewById(R.id.input_plan_name_layout);
            inputNameLayout.setError("* Required");
            canCreate = false;
        }
        else if (PlanReader.planExists(inputNameString) && planSummary == null) {
            Toast.makeText(getContext(),
                    getString(R.string.fragment_daily_routine_finisher_plan_error_plan_already_exists),
                    Toast.LENGTH_LONG)
                    .show();
            canCreate = false;
        }

        if (TextUtils.isEmpty(inputDescString)) {
            TextInputLayout inputDescLayout = view.findViewById(R.id.input_plan_description_layout);
            inputDescLayout.setError("* Required");
            canCreate = false;
        }

        if (!canCreate) {
            return;
        }

        CheckBox checkBox = view.findViewById(R.id.cb_set_as_current_plan);

        createPlan(view.getContext(), inputNameString, inputDescString, checkBox.isChecked());
    }

    private void createPlan(Context context, String inputNameString, String inputDescString, boolean setAsActivePlan) {

        String successMessage;

        // Plan is being edited
        if (planSummary != null) {
            removeAnyDeletedExercisesFromLogDatabase();
            deleteExistingPlan();
            // "Plan was updated"
            successMessage = getString(R.string.fragment_daily_routine_finisher_plan_was_updated);

            String originalPlanName = planSummary.getName();
            SQLLogWriter writer = new SQLLogWriter(originalPlanName);
            writer.updatePlanName(inputNameString);
        }
        else {
            // "Plan was created"
            successMessage = getString(R.string.fragment_daily_routine_finisher_plan_was_created);
        }

        long currentTime = new Date().getTime();
        PlanSummary planSummary = new PlanSummary(inputNameString, inputDescString, currentTime);
        PlanCreator.getBuilder()
                .setPlanSummary(planSummary, setAsActivePlan)
                .setDailyRoutineMap(dailyRoutineMap)
                .create();

        // todo:: handle new plans w/ same name of existing plan. This will overwrite it.

        //noinspection ConstantConditions
        getActivity().finish();
        Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show();


    }

    private void removeAnyDeletedExercisesFromLogDatabase() {

        /*
        This is how this works.
        Take the newly generated set of exercises, and retrieve their uuids.
        Take the old generated set of exercises, before editing the plan, remove their uuids.

        If the old set has uuids that the NEWLY created set does not have, then there's no need
        to keep them in the logging database, so just delete it.
         */

        // a1, a2, a3, a6
        Set<String> newUuids = new HashSet<>();
        for (List<Exercise> exerciseList : dailyRoutineMap.values()) {
            for (Exercise exercise : exerciseList) {
                String uuid = exercise.getUuid();
                newUuids.add(uuid);
            }
        }

        // a1, a2, a3, a4, a5
        Set<String> existingUuids = new HashSet<>();
        PlanReader reader = PlanReader.attachTo(planSummary.getName());
        EnumMap<Day, List<Exercise>> existingDailyRoutines = reader.getDailyRoutines();
        for (List<Exercise> exerciseList : existingDailyRoutines.values()) {
            for (Exercise exercise : exerciseList) {
                String uuid = exercise.getUuid();
                existingUuids.add(uuid);
            }
        }

        // keep everything, EXCEPT a4, a5
        existingUuids.removeAll(newUuids);

        for (String uuidsToBeRemoved : existingUuids) {
            SQLLogWriter.delete(uuidsToBeRemoved);
        }



    }

    private void deleteExistingPlan() {
        String currentPlanName = planSummary.getName();
        PlanEditor planEditor = new PlanEditor(getContext(), currentPlanName);
        planEditor.delete();

    }

}
