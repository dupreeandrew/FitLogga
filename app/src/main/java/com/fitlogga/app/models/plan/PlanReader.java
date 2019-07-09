package com.fitlogga.app.models.plan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseTranslator;
import com.fitlogga.app.models.exercises.ExerciseType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PlanReader {

    // todo:: apply a more OOP-themed approach.

    private Context context;

    public PlanReader(Context context) {
        this.context = context;
    }

    public List<PlanSummary> getPlanSummaries() {
        SharedPreferences prefs = context.getSharedPreferences("registered_plans", Context.MODE_PRIVATE);

        List<PlanSummary> planSummaries = new ArrayList<>();
        Gson gson = new Gson();

        for (Object o : prefs.getAll().values()) {

            String json = (String)o;
            PlanSummary planSummary = gson.fromJson(json, PlanSummary.class);
            planSummaries.add(planSummary);

        }

        // Sort by last used.
        Collections.sort(planSummaries, (t1, t2) -> {
            if (t1.getLastUsed() == t2.getLastUsed()) {
                return 0;
            }

            return (t1.getLastUsed() > t2.getLastUsed()) ? -1 : 1;

        });

        return planSummaries;

    }


    /**
     * Acquire a daily routine.
     * This will always at the very minimum return an empty list.
     */
    @NonNull
    public List<Exercise> getDailyRoutine(String planName, Day day) {

        String preferenceName = PlanIOUtils.getIOSafeFileID(planName);

        Log.d("aaaa", "Reading from " + preferenceName + ", Day: " + day.ordinal());

        String exerciseListJson = context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .getString(String.valueOf(day.ordinal()), "empty");


        if ("empty".equals(exerciseListJson))
            return new ArrayList<>();

        Day copiedDay = checkForCopierDay(exerciseListJson);
        if (copiedDay != null) {
            return getDailyRoutine(planName, copiedDay);
        }


        Gson gson = new Gson();

        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        Map<String, Map<String, Object>> jsonMap = gson.fromJson(exerciseListJson, type);

        List<Exercise> exerciseList = new ArrayList<>();
        for (Map<String, Object> exerciseMap : jsonMap.values()) {
            Exercise exercise = getExerciseFromMap(exerciseMap);
            exerciseList.add(exercise);
        }

        return exerciseList;


    }

    private Day checkForCopierDay(String exerciseListJson) {
        try {
            int dayValue = Integer.parseInt(exerciseListJson);
            return Day.fromValue(dayValue);
        }
        catch (Exception ex) {
            return null;
        }
    }

    /**
     * Quicker way of calling #getDailyRoutine() 7x.
     */
    @Nullable
    public EnumMap<Day, List<Exercise>> getDailyRoutines(String planName) {

        if (!planExists(planName)) {
            return null;
        }

        EnumMap<Day, List<Exercise>> dailyRoutineMap = new EnumMap<>(Day.class);
        for (Day day : Day.values()) {
            List<Exercise> dailyRoutine = getDailyRoutine(planName, day);
            dailyRoutineMap.put(day, dailyRoutine);
        }

        return dailyRoutineMap;
    }

    private boolean planExists(String planName) {
        String planFileName = PlanIOUtils.getIOSafeFileID(planName);
        return context
                .getSharedPreferences("registered_plans", Context.MODE_PRIVATE)
                .contains(planFileName);
    }

    private Exercise getExerciseFromMap(Map<String, Object> exerciseMap) {
        int exerciseTypeInteger = Integer.parseInt((String)exerciseMap.get("exerciseType"));
        ExerciseType exerciseType = ExerciseType.fromInteger(exerciseTypeInteger);
        return ExerciseTranslator.toExercise(exerciseType, exerciseMap);
    }

    @Nullable
    public String getCurrentPlanName() {
        try {
            return getPlanSummaries().get(0).getName();
        }
        catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public boolean isDayEmpty(@NonNull String planName, Day day) {
        String preferenceName = PlanIOUtils.getIOSafeFileID(planName);
        return !context
                .getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                .contains(String.valueOf(day.ordinal()));
    }

    @Nullable
    PlanSummary getPlanSummaryFromName(String planName) {
        for (PlanSummary planSummary : getPlanSummaries()) {
            if (planSummary.getName().equals(planName)) {
                return planSummary;
            }
        }
        return null;
    }


}
