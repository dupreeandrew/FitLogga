package com.fitlogga.app.models.plan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseType;

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
        private Context context;
        private PlanSummary planSummary;
        private EnumMap<Day, List<Exercise>> dailyRoutineMap;

        private Builder() {
            // empty constructor
        }

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
         *
         * By default, if an active plan exists, it's lastUsed property is updated to 1ms in
         * the future. Adjust the given PlanSummary to compensate if needed.
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
            List<Exercise> dailyRoutine = dailyRoutineMap.get(usedDay);

            if (dailyRoutine == null || dailyRoutine.size() == 0) {
                continue;
            }

            int dayNumber = usedDay.ordinal();

            /*
            if (dayIsCopierExercise(dailyRoutine)) {
                DayCopierExercise dayCopierExercise = (DayCopierExercise) dailyRoutine.get(0);
                Day day = dayCopierExercise.getDayBeingCopied();
                String copierMessage = String.valueOf(day.getValue());
                editor.putString(String.valueOf(dayNumber), copierMessage);
            }
            else {
            */
                String exerciseListJson = PlanWritingUtils.getExerciseListJson(dailyRoutine);
                editor.putString(String.valueOf(dayNumber), exerciseListJson);
                Log.d("boo11", exerciseListJson);
            //}

        }


        PlanWritingUtils.writePlanSummary(context, planSummary);

        editor.apply();

        if (planCreationListener != null) {
            planCreationListener.onCreated();
        }

    }

    private boolean dayIsCopierExercise(List<Exercise> dailyRoutine) {
        return (
                dailyRoutine.size() == 1
                        &&
                dailyRoutine.get(0).getExerciseType() == ExerciseType.COPIER
        );
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
