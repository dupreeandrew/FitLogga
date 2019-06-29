package com.fitlogga.app.models.plan.update;

import android.content.Context;

import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.PlanSummary;
import com.fitlogga.app.models.plan.PreferenceNamer;

import java.util.Date;
import java.util.List;

public class PlanWriterTwo {


    public interface PlanSummaryListener {
        void onSummmariesUpdated();
    }

    private Context context;
    private final String planName;

    public PlanWriterTwo(Context context, String planName) {
        this.context = context;
        this.planName = planName;
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

    }

    public void updateDailyRoutine(Day day, List<Exercise> dailyRoutine) {

        String exerciseListJson = PlanWritingUtils.getExerciseListJson(dailyRoutine);

        String preferenceName = PreferenceNamer.fromPlanName(planName);
        context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit()
                .putString(day.toString(), exerciseListJson)
                .apply();
    }

    public void setAsCurrentPlan() {
        PlanReader planReader = new PlanReader(context);
        PlanSummary planSummary = planReader.getPlanSummaryFromName(planName);
        PlanSummary editedPlanSummary = new PlanSummary(
                planSummary.getName(),
                planSummary.getDescription(),
                new Date().getTime() + 2000);
        PlanWritingUtils.writePlanSummary(context, editedPlanSummary);
    }
}
