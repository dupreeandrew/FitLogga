package com.fitlogga.app.models.plan;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fitlogga.app.models.ApplicationContext;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.DayCopierExercise;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseTranslator;
import com.fitlogga.app.utils.CollectionSortHelper;
import com.fitlogga.app.utils.GsonHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PlanReader implements PlanSource {

    private static final String PLAN_SUMMARY_PREF_STRING = "registered_plans";

    private SharedPreferences planNamePref;
    private String planName;

    private PlanReader(SharedPreferences planNamePref, String planName) {
        this.planNamePref = planNamePref;
        this.planName = planName;
    }

    @Nullable
    public static PlanReader attachTo(String planName) {

        if (planName == null) {
            return null;
        }

        Context context = ApplicationContext.getInstance();
        if (!planExists(planName)) {
            return null;
        }

        String planNamePrefString = PlanIOUtils.getIOSafeFileID(planName);
        SharedPreferences planNamePref
                = context.getSharedPreferences(planNamePrefString, Context.MODE_PRIVATE);

        return new PlanReader(planNamePref, planName);

    }

    public static boolean planExists(String planName) {
        String planNamePrefString = PlanIOUtils.getIOSafeFileID(planName);
        SharedPreferences planSummaryPref = getPlanSummaryPref();
        return planSummaryPref.contains(planNamePrefString);
    }

    @Nullable
    public static PlanReader attachToCurrentPlan() {
        String currentPlanName = PlanReader.getCurrentPlanName();
        if (currentPlanName == null) {
            return null;
        }
        return PlanReader.attachTo(currentPlanName);
    }

    private static SharedPreferences getPlanSummaryPref() {
        Context context = ApplicationContext.getInstance();
        return context.getSharedPreferences(PLAN_SUMMARY_PREF_STRING, Context.MODE_PRIVATE);
    }

    /**
     * Quicker way of calling #getDailyRoutine() 7x.
     */
    @Override
    public EnumMap<Day, DailyRoutine> getDailyRoutines() {
        EnumMap<Day, DailyRoutine> dailyRoutineMap = new EnumMap<>(Day.class);
        for (Day day : Day.values()) {
            DailyRoutine dailyRoutine = getDailyRoutine(day);
            dailyRoutineMap.put(day, dailyRoutine);
        }

        return dailyRoutineMap;
    }

    /**
     * Acquire a daily routine.
     * This will always at the very minimum return an empty list.
     */
    @NonNull
    public DailyRoutine getDailyRoutine(Day day) {

        final String NO_DAILY_ROUTINE_STRING = "empty";

        String dayValueString = String.valueOf(day.getValue());
        String exerciseListJson = planNamePref.getString(dayValueString, NO_DAILY_ROUTINE_STRING);

        if (NO_DAILY_ROUTINE_STRING.equals(exerciseListJson)) {
            return new DailyRoutine();
        }

        return getDailyRoutine(exerciseListJson);

    }

    private DailyRoutine getDailyRoutine(String dailyRoutineJson) {
        Map<String, Object> jsonMap = GsonHelper.getMapFromJson(dailyRoutineJson);
        List<Map<String, Object>> rawExercises = (List<Map<String, Object>>) jsonMap.get("exercises");

        DailyRoutine dailyRoutine = new DailyRoutine();
        for (Map<String, Object> exerciseMap : rawExercises) {
            Exercise exercise = ExerciseTranslator.toExercise(exerciseMap);
            dailyRoutine.getExercises().add(exercise);
        }

        String dailyRoutineName = (String) jsonMap.get("name");
        dailyRoutine.setName(dailyRoutineName);


        return dailyRoutine;
    }

    @Nullable
    public static String getCurrentPlanName() {
        try {
            return getPlanSummaries().get(0).getName();
        }
        catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public static List<PlanSummary> getPlanSummaries() {
        SharedPreferences summaryPref = getPlanSummaryPref();
        List<PlanSummary> planSummaries = new ArrayList<>();
        Gson gson = new Gson();

        for (Object o : summaryPref.getAll().values()) {
            String json = (String)o;
            PlanSummary planSummary = gson.fromJson(json, PlanSummary.class);
            planSummaries.add(planSummary);
        }

        Collections.sort(planSummaries, (p1, p2)
                -> CollectionSortHelper.byDescending(p1.getLastUsed(), p2.getLastUsed()));

        return planSummaries;

    }

    public boolean isDayEmpty(Day day) {
        String dayValueString = String.valueOf(day.getValue());
        return !planNamePref.contains(dayValueString);
    }

    @Nullable
    public DayCopierExercise getDayCopier(Day day) {
        Exercise exercise = getDailyRoutine(day).getExercises().get(0);
        if (exercise instanceof DayCopierExercise) {
            return (DayCopierExercise)exercise;
        }
        else {
            return null;
        }
    }

    @Nullable
    @Override
    public PlanSummary getPlanSummary() {
        SharedPreferences summaryPref = getPlanSummaryPref();
        String planNamePrefString = PlanIOUtils.getIOSafeFileID(planName);
        String summaryPlanString = summaryPref.getString(planNamePrefString, "should-impossible");
        return new Gson().fromJson(summaryPlanString, PlanSummary.class);
    }

    public String getPlanName() {
        return this.planName;
    }

    public static int getNumberOfPlans() {
        SharedPreferences sharedPref = getPlanSummaryPref();
        return sharedPref.getAll().size();
    }


}
