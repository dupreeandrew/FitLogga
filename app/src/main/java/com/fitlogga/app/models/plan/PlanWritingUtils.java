package com.fitlogga.app.models.plan;

import android.content.Context;
import android.content.SharedPreferences;

import com.fitlogga.app.models.ApplicationContext;
import com.fitlogga.app.models.exercises.Exercise;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class PlanWritingUtils {

    static void writePlanSummary(PlanSummary planSummary) {
        Context context = ApplicationContext.getInstance();
        SharedPreferences.Editor editor = context
                .getSharedPreferences("registered_plans", Context.MODE_PRIVATE)
                .edit();

        String planName = planSummary.getName();
        String keyNameOfPlanData = PlanIOUtils.getIOSafeFileID(planName);
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



}
