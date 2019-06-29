package com.fitlogga.app.models.plan;

import android.content.Context;
import android.content.SharedPreferences;

import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.google.gson.Gson;

import java.util.Date;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*

To the developer:
There is one god file,
registered_plans.

registered_plans represents Plan Summaries.
Every key represents the preference name of the plan, which gives the dailyroutines of the plan.
Every value is a json of the plan summary.


 */

public class PlanWriter {

    public interface PlanSummaryListener {
        void onSummmariesUpdated();
    }

    private Context context;
    private static PlanSummaryListener planSummaryListener;

    public PlanWriter(Context context) {
        this.context = context;
    }


    public void fullWrite(PlanSummary planSummary, EnumMap<Day, List<Exercise>> dailyRoutineMap) {

        updateActivePlanLastUsedMillis();

        String preferenceName = PreferenceNamer.fromPlanName(planSummary.getName());
        SharedPreferences.Editor editor =  context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit();


        Set<Day> usedDays = dailyRoutineMap.keySet();

        // 0 = Sunday. 6 = Saturday.
        for (Day usedDay : usedDays) {
            List<Exercise> dailyRoutine = dailyRoutineMap.get(usedDay);
            int dayNumber = usedDay.ordinal();

            String exerciseListJson = generateExerciseListJson(dailyRoutine);
            editor.putString(String.valueOf(dayNumber), exerciseListJson);
        }

        editor.apply();
        writePlanSummary(planSummary);

        if (planSummaryListener != null) {
            planSummaryListener.onSummmariesUpdated();
        }

    }

    private void updateActivePlanLastUsedMillis() {
        PlanReader planReader = new PlanReader(context);

        List<PlanSummary> planSummaryList = planReader.getPlanSummaries();

        if (planSummaryList.size() == 0) {
            return;
        }

        PlanSummary activePlanSummary = planReader.getPlanSummaries().get(0);

        PlanSummary editedActivePlanSummary = new PlanSummary(
                activePlanSummary.getName(),
                activePlanSummary.getDescription(),
                new Date().getTime() + 1
        );

        writePlanSummary(editedActivePlanSummary);

    }


    private void writePlanSummary(PlanSummary plan) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences("registered_plans", Context.MODE_PRIVATE)
                .edit();



        String keyNameOfPlanData = PreferenceNamer.fromPlanName(plan.getName());
        Gson gson = new Gson();
        String planSummaryJson = gson.toJson(plan);
        editor.putString(keyNameOfPlanData, planSummaryJson);
        editor.apply();

    }

    public void deleteFitnessPlan(String planName) {
        context
                .getSharedPreferences("registered_plans", Context.MODE_PRIVATE)
                .edit()
                .remove(PreferenceNamer.fromPlanName(planName))
                .apply();

        context
                .getSharedPreferences(PreferenceNamer.fromPlanName(planName), Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

    }

    public void updateDailyRoutine(String planName, Day day, List<Exercise> dailyRoutine) {

        String exerciseListJson = generateExerciseListJson(dailyRoutine);

        String preferenceName = PreferenceNamer.fromPlanName(planName);
        context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit()
                .putString(day.toString(), exerciseListJson)
                .apply();
    }

    private String generateExerciseListJson(List<Exercise> listOfExercises) {

        Gson gson = new Gson();

        Map<String, Object> exerciseMap = new LinkedHashMap<>();
        for (int i = 0; i < listOfExercises.size(); i++) {
            Exercise exercise = listOfExercises.get(i);
            exerciseMap.put(String.valueOf(i), exercise);
        }

        return gson.toJson(exerciseMap);
    }

    public void setAsCurrentPlan(PlanSummary planSummary) {
        PlanSummary editedPlanSummary = new PlanSummary(
                planSummary.getName(),
                planSummary.getDescription(),
                new Date().getTime() + 2000);
        writePlanSummary(editedPlanSummary);
    }

    /**
     * Sets the listener that is called whenever a plan is ADDED.
     */
    public static void setPlanSummaryListener(PlanSummaryListener listener) {
        planSummaryListener = listener;
    }









}
