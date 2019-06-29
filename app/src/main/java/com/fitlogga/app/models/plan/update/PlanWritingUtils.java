package com.fitlogga.app.models.plan.update;

import android.content.Context;
import android.content.SharedPreferences;

import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.plan.PlanSummary;
import com.fitlogga.app.models.plan.PreferenceNamer;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlanWritingUtils {

    static void writePlanSummary(Context context, PlanSummary planSummary) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences("registered_plans", Context.MODE_PRIVATE)
                .edit();



        String keyNameOfPlanData = PreferenceNamer.fromPlanName(planSummary.getName());
        Gson gson = new Gson();
        String planSummaryJson = gson.toJson(planSummary);
        editor.putString(keyNameOfPlanData, planSummaryJson);

        editor.apply();

    }


    static String getExerciseListJson(List<Exercise> listOfExercises) {

        Gson gson = new Gson();

        Map<String, Object> exerciseMap = new LinkedHashMap<>();
        for (int i = 0; i < listOfExercises.size(); i++) {
            Exercise exercise = listOfExercises.get(i);
            exerciseMap.put(String.valueOf(i), exercise);
        }

        return gson.toJson(exerciseMap);
    }

    static String getIOSafeFileID(String planName) {
        return planName.replace(' ', '_');
    }

}
