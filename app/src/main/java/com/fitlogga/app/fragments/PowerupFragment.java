package com.fitlogga.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fitlogga.app.R;
import com.fitlogga.app.activities.TrainingActivity;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.plan.PlanReader;

public class PowerupFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_powerup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPowerupButton(view);
    }

    private void initPowerupButton(View view) {
        Button powerupButton = view.findViewById(R.id.btn_powerup);
        powerupButton.setOnClickListener(buttonView -> tryOpeningTrainingActivity(view));
    }

    private void tryOpeningTrainingActivity(View view) {
        PlanReader planReader = new PlanReader(view.getContext());
        String currentPlanName = planReader.getCurrentPlanName();

        if (currentPlanName == null) {
            Toast.makeText(getContext(), "You do not have a plan.", Toast.LENGTH_SHORT).show();
            // show error message or disable button, something.
            return;
        }

        if (planReader.isDayEmpty(currentPlanName, Day.getToday())) {
            Toast.makeText(getContext(), "You got nothing to do today.", Toast.LENGTH_SHORT).show();
            return;
        }



        Intent intent = new Intent(view.getContext(), TrainingActivity.class);
        intent.putExtra(TrainingActivity.DAY_NUM_KEY, Day.getToday().getValue());
        intent.putExtra(TrainingActivity.PLAN_NAME_KEY, currentPlanName);
        startActivity(intent);


    }



}
