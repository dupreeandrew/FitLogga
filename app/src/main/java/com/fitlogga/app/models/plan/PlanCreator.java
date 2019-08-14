package com.fitlogga.app.models.plan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fitlogga.app.models.ApplicationContext;
import com.fitlogga.app.models.Day;

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

    public static class Builder {
        private PlanSummary planSummary;
        private EnumMap<Day, DailyRoutine> dailyRoutineMap;

        private Builder() {
            // empty constructor
        }

        /**
         * @param setAsActive Modify the timestamp of the PlanSummary, making it the active plan.
         */
        public Builder setPlanSummary(PlanSummary planSummary, boolean setAsActive) {

            if (!setAsActive) {
                this.planSummary = planSummary;
                return this;
            }

            String name = planSummary.getName();
            String description = planSummary.getDescription();
            long timestamp = System.currentTimeMillis() + 300;
            this.planSummary = new PlanSummary(name, description, timestamp);
            return this;
        }

        public Builder setDailyRoutineMap(EnumMap<Day, DailyRoutine> dailyRoutineMap) {
            this.dailyRoutineMap = dailyRoutineMap;
            return this;
        }


        /**
         * Writes a Fitness Plan onto file.
         * If the specified Fitness Plan Name already exists, it will be overwritten.
         *
         * By default, if an active plan exists, it's lastUsed property is updated to 1ms in
         * the future. Adjust the given PlanSummary to compensate if needed.
         */
        public void create() {
            new PlanCreator(planSummary, dailyRoutineMap).write();
        }
    }

    private PlanSummary planSummary;
    private EnumMap<Day, DailyRoutine> dailyRoutineMap;
    private SharedPreferences.Editor editor;
    private static PlanCreationListener planCreationListener;

    public static Builder getBuilder() {
        return new Builder();
    }

    @SuppressLint("CommitPrefEdits")
    private PlanCreator(PlanSummary planSummary, EnumMap<Day, DailyRoutine> dailyRoutineMap) {
        this.planSummary = planSummary;
        this.dailyRoutineMap = dailyRoutineMap;

        Context context = ApplicationContext.getInstance();
        String preferenceName = PlanIOUtils.getIOSafeFileID(planSummary.getName());
        editor = context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .edit()
                .clear();
    }

    private void write() {

        updateActivePlanLastUsedMillis();

        Set<Day> usedDays = dailyRoutineMap.keySet();

        // 0 = Sunday. 6 = Saturday.
        for (Day usedDay : usedDays) {
            DailyRoutine dailyRoutine = dailyRoutineMap.get(usedDay);

            if (dailyRoutine == null || dailyRoutine.getExercises().size() == 0) {
                continue;
            }

            int dayNumber = usedDay.ordinal();

            String dailyRoutineJson = PlanWritingUtils.getDailyRoutineJson(dailyRoutine);
            editor.putString(String.valueOf(dayNumber), dailyRoutineJson);
            Log.d("boo11", dailyRoutineJson);

        }


        PlanWritingUtils.writePlanSummary(planSummary);

        editor.apply();

        if (planCreationListener != null) {
            planCreationListener.onCreated();
        }

    }

    private void updateActivePlanLastUsedMillis() {

        List<PlanSummary> planSummaryList = PlanReader.getPlanSummaries();

        if (planSummaryList.size() == 0) {
            return;
        }

        PlanSummary activePlanSummary = planSummaryList.get(0);

        PlanSummary editedActivePlanSummary = new PlanSummary(
                activePlanSummary.getName(),
                activePlanSummary.getDescription(),
                new Date().getTime() + 1
        );

        PlanWritingUtils.writePlanSummary(editedActivePlanSummary);

    }

    /**
     * Sets the listener that is called whenever a plan is ADDED or REMOVED.
     */
    public static void setPlanCreationListener(PlanCreationListener listener) {
        planCreationListener = listener;
    }


}
