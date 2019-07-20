package com.fitlogga.app.models.plan.log;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.fitlogga.app.models.ApplicationContext;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseType;
import com.fitlogga.app.models.plan.PlanIOUtils;
import com.fitlogga.app.models.plan.log.Historics.FreeWeightHistory;
import com.fitlogga.app.models.plan.log.Historics.History;
import com.fitlogga.app.models.plan.log.Historics.RepetitionHistory;
import com.fitlogga.app.models.plan.log.Historics.StandardExerciseHistory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class PlanLogReader {

    private SharedPreferences sharedPreferences;
    //private Gson gson = new Gson();

    public PlanLogReader(String planName) {
        planName += " log";
        String prefName = PlanIOUtils.getIOSafeFileID(planName);
        sharedPreferences = ApplicationContext.getInstance()
                .getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public Set<History> getHistories(Day day) {
        String dayKeyValue = String.valueOf(day.getValue());
        Set<String> historicalKeysOfDay
                = sharedPreferences.getStringSet(dayKeyValue, new HashSet<>());
        return getHistorySetFromKeys(historicalKeysOfDay);
    }

    private Set<History> getHistorySetFromKeys(Set<String> historicalKeys) {
        Set<History> historyList = new HashSet<>();
        for (String historicalKey : historicalKeys) {
            History history = getHistoricalFromItsKey(historicalKey);
            historyList.add(history);
        }
        return historyList;
    }

    @Nullable
    private History getHistoricalFromItsKey(String historicalKey) {

        String historicalJson = sharedPreferences.getString(historicalKey, "");

        if (TextUtils.isEmpty(historicalJson)) {
            return null;
        }

        ExerciseType historicalExerciseType = HistoricalKey.readExerciseType(historicalKey);
        Type type;

        switch (historicalExerciseType) {
            case METER_RUN:
            case TIMED_RUN:
            case REST:
                type = new TypeToken<StandardExerciseHistory>(){}.getType();
                break;
            case REPETITION_EXERCISE:
                type = new TypeToken<RepetitionHistory>(){}.getType();
                break;
            case FREE_WEIGHT_EXERCISE:
                type = new TypeToken<FreeWeightHistory>(){}.getType();
                break;
            default:
                throw new IllegalArgumentException();
        }

        Gson gson = new Gson();
        return gson.fromJson(historicalJson, type);
    }

    @Nullable
    public <T extends History> T getHistory(Exercise exercise) {
        String historicalKey = HistoricalKey.get(exercise);
        return (T) getHistoricalFromItsKey(historicalKey);
    }

}
