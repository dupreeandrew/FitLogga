package com.fitlogga.app.models.plan;

import androidx.annotation.Nullable;

import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseTranslator;
import com.fitlogga.app.utils.GsonHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * PlanExchanger is a class used primarily to make plans shareable by other users.
 * Not much validation is done here, because it's all handled through the backend.
 */
public class PlanExchanger {

    public static class Plan {
        private PlanSummary planSummary;
        private EnumMap<Day, List<Exercise>> dailyRoutines;

        public Plan(PlanSummary planSummary, EnumMap<Day, List<Exercise>> dailyRoutines) {
            this.planSummary = planSummary;
            this.dailyRoutines = dailyRoutines;
        }

        public PlanSummary getPlanSummary() {
            return planSummary;
        }

        public EnumMap<Day, List<Exercise>> getDailyRoutineMap() {
            return dailyRoutines;
        }
    }

    private static final int PLAN_SUMMARY_INDEX = 0;
    private static final int DAILY_ROUTINE_MAP_INDEX = 1;
    private static final String DELIMITER = "!%@%@%!";


    public static @Nullable String exportPlan(String planName) {
        PlanReader planReader = PlanReader.attachTo(planName);
        if (planReader == null) {
            return null;
        }

        PlanSummary planSummary = planReader.getPlanSummary();
        EnumMap<Day, List<Exercise>> dailyRoutines = planReader.getDailyRoutines();

        for (Map.Entry<Day, List<Exercise>> entry : dailyRoutines.entrySet()) {
            List<Exercise> dailyRoutine = entry.getValue();
            if (dailyRoutine.isEmpty()) {
                Day day = entry.getKey();
                dailyRoutines.remove(day);
            }
        }

        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        String planSummaryJson = gson.toJson(planSummary);
        String dailyRoutinesJson = gson.toJson(dailyRoutines);

        return planSummaryJson
                + DELIMITER
                + dailyRoutinesJson;

    }

    public static boolean importPlan(String exportPlanJson) {
        Plan plan = getPlan(exportPlanJson);
        PlanSummary planSummary = plan.getPlanSummary();

        String planName = planSummary.getName();
        if (duplicatePlanName(planName)) {
            return false;
        }

        EnumMap<Day, List<Exercise>> dailyRoutineMap = plan.getDailyRoutineMap();
        PlanCreator.getBuilder()
                .setPlanSummary(planSummary, true)
                .setDailyRoutineMap(dailyRoutineMap)
                .create();

        return true;
    }

    public static Plan getPlan(String exportPlanJson) {
        String[] planPieces = exportPlanJson.split(DELIMITER);
        String planSummaryJson = planPieces[PLAN_SUMMARY_INDEX];
        String dailyRoutinesMapJson = planPieces[DAILY_ROUTINE_MAP_INDEX];

        PlanSummary planSummary = new Gson().fromJson(planSummaryJson, PlanSummary.class);

        Map<String, List<Map<String, Object>>> dailyRoutineRawMap
                = GsonHelper.getMapOfListsOfMaps(dailyRoutinesMapJson);
        EnumMap<Day, List<Exercise>> dailyRoutineMap = new EnumMap<>(Day.class);

        /*
        entry.key = Day # as string
        entry.value = List of exercises, written in a Map.
         */
        for (Map.Entry<String, List<Map<String, Object>>> entry : dailyRoutineRawMap.entrySet()) {
            int dayValue = Integer.parseInt(entry.getKey());
            Day day = Day.fromValue(dayValue);

            List<Exercise> exerciseList = new ArrayList<>();
            for (Map<String, Object> exerciseMap : entry.getValue()) {
                Exercise exercise = ExerciseTranslator.toExercise(exerciseMap);
                exerciseList.add(exercise);
            }

            dailyRoutineMap.put(day, exerciseList);

        }

        return new Plan(planSummary, dailyRoutineMap);



    }

    private static boolean duplicatePlanName(String name) {
        PlanReader planReader = PlanReader.attachTo(name);
        return (planReader != null);
    }




}
