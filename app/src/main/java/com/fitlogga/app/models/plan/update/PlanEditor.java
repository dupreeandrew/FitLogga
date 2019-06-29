package com.fitlogga.app.models.plan.update;

import android.content.Context;

import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.PlanSummary;
import com.fitlogga.app.models.plan.PreferenceNamer;

import java.util.Date;
import java.util.List;


public class PlanEditor {

    public interface PlanSummaryListener {
        void onSummmariesUpdated();
    }

    private Context context;
    private PlanSummary planSummary;
    private boolean deleted = false;
    private final String planName;


    public PlanEditor(Context context, String planName) {
        this.context = context;
        this.planName = planName;

        PlanReader planReader = new PlanReader(context);
        planSummary = planReader.getPlanSummaryFromName(planName);

        if (planSummary == null) {
            throw new NullPointerException("Plan could not be found.");
        }
    }

    public void delete() {
        context
                .getSharedPreferences("registered_plans", Context.MODE_PRIVATE)
                .edit()
                .remove(PreferenceNamer.fromPlanName(planName))
                .apply();

        String preferenceName = PreferenceNamer.fromPlanName(planName);
        context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        deleted = true;

    }

    public void updateDailyRoutine(Day day, List<Exercise> dailyRoutine) {

        checkForDeleted();

        String exerciseListJson = PlanWritingUtils.getExerciseListJson(dailyRoutine);

        String preferenceName = PreferenceNamer.fromPlanName(planName);
        context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit()
                .putString(day.toString(), exerciseListJson)
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
        PlanWritingUtils.writePlanSummary(context, editedPlanSummary);
    }

}
