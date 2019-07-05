package com.fitlogga.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fitlogga.app.R;
import com.fitlogga.app.activities.TrainingActivity;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.plan.PlanReader;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PowerupFragment extends Fragment {

    private static class DayStringAdapterMapper {
        private final List<String> dayStrings;
        private final SparseIntArray dayAdapterMapper;

        private DayStringAdapterMapper(List<String> dayStrings, SparseIntArray dayAdapterMapper) {
            this.dayStrings = dayStrings;
            this.dayAdapterMapper = dayAdapterMapper;
        }

        private List<String> getDayStrings() {
            return dayStrings;
        }

        private Day getDayFromAdapterPos(int adapterPos) {
            int dayValue = dayAdapterMapper.get(adapterPos);
            return Day.fromValue(dayValue);
        }

    }

    private PlanReader planReader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_powerup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPowerupButton(view);
        initSelectDailyRoutineButton(view);
        planReader = new PlanReader(view.getContext());
    }

    private void initPowerupButton(View view) {
        Button powerupButton = view.findViewById(R.id.btn_powerup);
        powerupButton.setOnClickListener(buttonView -> tryOpeningTrainingActivity(view));
    }

    private void tryOpeningTrainingActivity(View view) {

        String currentPlanName = planReader.getCurrentPlanName();

        if (currentPlanName == null) {
            showNoPlanToast();
            return;
        }

        if (planReader.isDayEmpty(currentPlanName, Day.getToday())) {
            Toast.makeText(getContext(), "You got nothing to do today.", Toast.LENGTH_SHORT).show();
            return;
        }

        startTrainingActivity(view.getContext(), Day.getToday());

    }

    private void showNoPlanToast() {
        Toast.makeText(getContext(), "You do not have a plan.", Toast.LENGTH_SHORT).show();
    }

    private void startTrainingActivity(Context context, Day day) {
        Intent intent = new Intent(context, TrainingActivity.class);
        intent.putExtra(TrainingActivity.DAY_NUM_KEY, day.getValue());
        intent.putExtra(TrainingActivity.PLAN_NAME_KEY, planReader.getCurrentPlanName());
        startActivity(intent);
    }

    private void initSelectDailyRoutineButton(View view) {
        ImageButton selectDailyRoutineButton = view.findViewById(R.id.btn_select_daily_routine);
        selectDailyRoutineButton.setOnClickListener(buttonView ->
                openAvailableDailyRoutinesDialog(view));
    }

    private void openAvailableDailyRoutinesDialog(View view) {

        String currentPlanName = planReader.getCurrentPlanName();

        if (currentPlanName == null) {
            showNoPlanToast();
            return;
        }

        PlanReader planReader = new PlanReader(view.getContext());
        EnumMap<Day, List<Exercise>> dailyRoutines
                = planReader.getDailyRoutines(currentPlanName);


        DayStringAdapterMapper availableDayStringAdapterMapper =
                getAvailableDayStringAdapterMapper(dailyRoutines, view);
        List<String> availableDayStrings = availableDayStringAdapterMapper.getDayStrings();

        new LovelyChoiceDialog(view.getContext())
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Choose a routine")
                .setItems(availableDayStrings, (position, item) -> {
                    Day selectedDay = availableDayStringAdapterMapper.getDayFromAdapterPos(position);
                    startTrainingActivity(view.getContext(), selectedDay);
                })
                .show();


    }

    private DayStringAdapterMapper getAvailableDayStringAdapterMapper(
            EnumMap<Day, List<Exercise>> dailyRoutines, View view
    ) {

        SparseIntArray dayIntegerMap = new SparseIntArray();
        List<String> availableDailyRoutineStrings = new ArrayList<>();


        int i = 0;

        for (Map.Entry<Day, List<Exercise>> entry : dailyRoutines.entrySet()) {

            Day day = entry.getKey();
            List<Exercise> exerciseList = entry.getValue();

            if (exerciseList.size() == 0) {
                continue;
            }

            String dayString = Day.getStringRepresentation(view.getContext(), day);
            availableDailyRoutineStrings.add(dayString);
            dayIntegerMap.put(i, day.getValue());
            i++;

        }

        return new DayStringAdapterMapper(availableDailyRoutineStrings, dayIntegerMap);

    }


}
