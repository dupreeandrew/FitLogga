package com.fitlogga.app.models.plan.update;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.PlanSummary;
import com.fitlogga.app.models.plan.PreferenceNamer;

import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;


public class PlanCreator {

    public interface PlanCreationListener {
        /**
         * Called whenever a plan is created.
         */
        void onCreated();
    }

    private static class Builder {
        private Context context;
        private PlanSummary planSummary;
        private EnumMap<Day, List<Exercise>> dailyRoutineMap;

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setPlanSummary(PlanSummary planSummary) {
            this.planSummary = planSummary;
            return this;
        }

        public Builder setDailyRoutineMap(EnumMap<Day, List<Exercise>> dailyRoutineMap) {
            this.dailyRoutineMap = dailyRoutineMap;
            return this;
        }

        /**
         * Writes a Fitness Plan onto file.
         * If the specified Fitness Plan Name already exists, it will be overwritten.
         */
        public void create() {
            new PlanCreator(context, planSummary, dailyRoutineMap).write();
        }
    }

    private Context context;
    private PlanSummary planSummary;
    private EnumMap<Day, List<Exercise>> dailyRoutineMap;
    private SharedPreferences.Editor editor;
    private static PlanCreationListener planCreationListener;

    public static Builder getBuilder() {
        return new Builder();
    }

    @SuppressLint("CommitPrefEdits")
    private PlanCreator(Context context, PlanSummary planSummary, EnumMap<Day, List<Exercise>> dailyRoutineMap) {
        this.context = context;
        this.planSummary = planSummary;
        this.dailyRoutineMap = dailyRoutineMap;

        String preferenceName = PreferenceNamer.fromPlanName(planSummary.getName());
        editor = context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit();
    }

    private void write() {

        updateActivePlanLastUsedMillis();

        Set<Day> usedDays = dailyRoutineMap.keySet();

        // 0 = Sunday. 6 = Saturday.
        for (Day usedDay : usedDays) {
            List<Exercise> dailyRoutine = dailyRoutineMap.get(usedDay);

            if (dailyRoutine == null) {
                return;
            }

            int dayNumber = usedDay.ordinal();
            String exerciseListJson = PlanWritingUtils.getExerciseListJson(dailyRoutine);
            editor.putString(String.valueOf(dayNumber), exerciseListJson);
        }


        PlanWritingUtils.writePlanSummary(context, planSummary);

        editor.apply();

        if (planCreationListener != null) {
            planCreationListener.onCreated();
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

        PlanWritingUtils.writePlanSummary(context, editedActivePlanSummary);

    }

    /**
     * Sets the listener that is called whenever a plan is ADDED or REMOVED.
     */
    public static void setPlanCreationListener(PlanCreationListener listener) {
        planCreationListener = listener;
    }


}
