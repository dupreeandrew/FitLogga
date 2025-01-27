package com.fitlogga.app.models.plan;

import android.content.Context;
import android.content.Intent;

import com.fitlogga.app.activities.PlanCreatorActivity;
import com.fitlogga.app.models.Day;

import java.io.File;
import java.util.Date;


public class PlanEditor {

    private Context context;
    private PlanSummary planSummary;
    private boolean deleted = false;
    private final String planName;


    public PlanEditor(Context context, String planName) throws NullPointerException {
        this.context = context;
        this.planName = planName;

        PlanReader planReader = PlanReader.attachTo(planName);
        if (planReader == null) {
            throw new NullPointerException("Plan could not be found.");
        }

        this.planSummary = planReader.getPlanSummary();
    }

    public void delete() {

        String preferenceName = PlanIOUtils.getIOSafeFileID(planName);

        context
                .getSharedPreferences("registered_plans", Context.MODE_PRIVATE)
                .edit()
                .remove(preferenceName)
                .apply();

        context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        deleteFileFromStorage(planName);
        deleted = true;
    }

    private void deleteFileFromStorage(String planName) {
        File prefFile = PlanIOUtils.getSharedPrefFile(planName);
        prefFile.delete();
    }

    public void updateDailyRoutine(Day day, DailyRoutine dailyRoutine) {

        checkForDeleted();

        String preferenceName = PlanIOUtils.getIOSafeFileID(planName);
        String exerciseListJson = PlanWritingUtils.getDailyRoutineJson(dailyRoutine);

        context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit()
                .putString(String.valueOf(day.getValue()), exerciseListJson)
                .apply();
    }

    private void checkForDeleted() {
        if (deleted) {
            throw new NullPointerException("This plan was deleted.");
        }
    }

    public void setAsCurrentPlan() {

        checkForDeleted();

        PlanSummary editedPlanSummary = new PlanSummary(
                planSummary.getName(),
                planSummary.getDescription(),
                new Date().getTime() + 2000);
        PlanWritingUtils.writePlanSummary(editedPlanSummary);
    }

    public static void openGUI(Context context, String planName) {
        Intent intent = new Intent(context, PlanCreatorActivity.class);
        intent.putExtra(PlanCreatorActivity.PREFILLED_PLAN_NAME_JSON_KEY, planName);
        context.startActivity(intent);
    }

}
