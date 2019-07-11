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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.fitlogga.app.R;
import com.fitlogga.app.activities.TrainingActivity;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.DaySuffix;
import com.fitlogga.app.models.exercises.DayCopierDetector;
import com.fitlogga.app.models.exercises.DayCopierExercise;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.plan.PlanEditor;
import com.fitlogga.app.models.plan.PlanReader;
import com.google.android.material.snackbar.Snackbar;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PowerupFragment extends Fragment {

    /**
     * Correlates day strings & their adapter position with their actual numeric day value.
     */
    private static class DayStringAdapterMapper {
        private final List<String> dayStrings;
        private final SparseIntArray dayAdapterMapper;

        /**
         * @param dayAdapterMapper K: adapterPos, V: dayValue
         */
        private DayStringAdapterMapper(List<String> availableDays, SparseIntArray dayAdapterMapper) {
            this.dayStrings = availableDays;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_powerup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCalendarView(view);
        initPowerupButton(view);
        initSelectDailyRoutineButton(view);
    }

    private void initCalendarView(View view) {

        Calendar calendar = Calendar.getInstance();
        String monthName = new SimpleDateFormat("LLLL", Locale.getDefault())
                .format(calendar.getTime());
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        String monthDayAndSuffix = DaySuffix.getDaySuffix(monthDay);

        String monthDayText = monthName + " " + monthDayAndSuffix;
        String dayNameText = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

        TextView monthDayTextView = view.findViewById(R.id.tv_month_day);
        monthDayTextView.setText(monthDayText);

        TextView dayNameTextView = view.findViewById(R.id.tv_day_name);
        dayNameTextView.setText(dayNameText);

    }

    private void initPowerupButton(View view) {
        Button powerupButton = view.findViewById(R.id.btn_powerup);
        powerupButton.setOnClickListener(buttonView -> tryOpeningTodaysTrainingActivity(view));
    }

    private void tryOpeningTodaysTrainingActivity(View view) {

        String currentPlanName = PlanReader.getCurrentPlanName();

        if (currentPlanName == null) {
            showNoPlanSnackbar(view);
            return;
        }

        PlanReader planReader = PlanReader.attachTo(currentPlanName);
        if (planReader != null && planReader.isDayEmpty(Day.getToday())) {
            showNoRoutinesSnackbar(view, currentPlanName,
                    // "You have nothing to do today"
                    R.string.powerup_you_have_nothing_to_do_today);
            return;
        }

        startTrainingActivity(view.getContext(), Day.getToday());

    }

    private void showNoRoutinesSnackbar(View view, String planName, @StringRes int messageStringId) {
        Snackbar snackbar = Snackbar.make(view, messageStringId, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.powerup_create_routine,
                snackBarView -> PlanEditor.openGUI(view.getContext(), planName));
        snackbar.show();
    }

    private void showNoPlanSnackbar(View view) {
        Snackbar snackbar = Snackbar.make(view, "You do not have a plan.", Snackbar.LENGTH_LONG);
        snackbar.setAction("Create plan",
                snackBarView -> PlansFragment.promptCreatePlanDialog(view));
        snackbar.show();
    }

    private void startTrainingActivity(Context context, Day day) {
        Intent intent = new Intent(context, TrainingActivity.class);
        intent.putExtra(TrainingActivity.DAY_NUM_KEY, day.getValue());
        intent.putExtra(TrainingActivity.PLAN_NAME_KEY, PlanReader.getCurrentPlanName());
        startActivity(intent);
    }

    private void initSelectDailyRoutineButton(View view) {
        ImageButton selectDailyRoutineButton = view.findViewById(R.id.btn_select_daily_routine);
        selectDailyRoutineButton.setOnClickListener(buttonView ->
                openAvailableDailyRoutinesDialog(view));
    }

    private void openAvailableDailyRoutinesDialog(View view) {


        PlanReader planReader = PlanReader.attachToCurrentPlan();
        if (planReader == null) {
            showNoPlanSnackbar(view);
            return;
        }

        EnumMap<Day, List<Exercise>> dailyRoutines = planReader.getDailyRoutines();
        DayStringAdapterMapper availableDayStringAdapterMapper =
                getAvailableDayStringAdapterMapper(dailyRoutines, view);
        List<String> availableDayStrings = availableDayStringAdapterMapper.getDayStrings();

        if (availableDayStrings.size() == 0) {
            showNoRoutinesSnackbar(view, planReader.getPlanName(),
                    // "You do not have a routine"
                    R.string.powerup_you_do_not_have_a_routine);
            return;
        }

        // "Choose a routine"
        String chooseARoutineString = view.getResources()
                .getString(R.string.powerup_choose_a_routine);
        new LovelyChoiceDialog(view.getContext())
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle(chooseARoutineString)
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


            int dayValue;
            if (DayCopierDetector.isDayCopier(exerciseList)) {
                Exercise firstExercise = exerciseList.get(0);
                DayCopierExercise dayCopierExercise = (DayCopierExercise) firstExercise;
                Day dayBeingCopied = dayCopierExercise.getDayBeingCopied();

                List<Exercise> copiedDailyRoutine = dailyRoutines.get(dayBeingCopied);
                if (copiedDailyRoutine.size() == 0) {
                    continue;
                }

                dayValue = dayBeingCopied.getValue();


            }
            else {
                dayValue = day.getValue();
            }

            dayIntegerMap.put(i, dayValue);
            String dayString = Day.getStringRepresentation(view.getContext(), day);
            availableDailyRoutineStrings.add(dayString);

            i++;

        }

        return new DayStringAdapterMapper(availableDailyRoutineStrings, dayIntegerMap);

    }


}
