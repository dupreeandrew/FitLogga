package com.fitlogga.app.models.plan;

import android.content.Context;
import android.content.SharedPreferences;

import com.fitlogga.app.models.ApplicationContext;
import com.google.gson.Gson;

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


    static String getDailyRoutineJson(DailyRoutine dailyRoutine) {
        Gson gson = new Gson();
        return gson.toJson(dailyRoutine);
    }



}
