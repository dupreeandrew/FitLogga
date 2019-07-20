package com.fitlogga.app.models.plan.log;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.fitlogga.app.models.ApplicationContext;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseType;
import com.fitlogga.app.models.exercises.FreeWeightExercise;
import com.fitlogga.app.models.exercises.RepetitionExercise;
import com.fitlogga.app.models.plan.PlanIOUtils;
import com.fitlogga.app.models.plan.log.Historics.FreeWeightHistory;
import com.fitlogga.app.models.plan.log.Historics.History;
import com.fitlogga.app.models.plan.log.Historics.RepetitionHistory;
import com.fitlogga.app.models.plan.log.Historics.StandardExerciseHistory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unchecked")
public class PlanLogWriter {

    private static class EditingParameter {
        private String uuidDayBankKey;
        private String historicalKey;
        private Editor editor;

        public EditingParameter(String uuidDayBankKey, String historicalKey, Editor editor) {
            this.uuidDayBankKey = uuidDayBankKey;
            this.historicalKey = historicalKey;
            this.editor = editor;
        }

    }

    private String planName;
    private SharedPreferences sharedPreferences;

    private PlanLogWriter(String planName) {
        this.planName = planName;
        String prefName = PlanIOUtils.getIOSafeFileID(planName + " log");

        this.sharedPreferences = ApplicationContext.getInstance()
                .getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public static PlanLogWriter attachTo(String planName) {
        return new PlanLogWriter(planName);
    }

    public void append(Day day, Exercise exercise) {

        String uuidDayBankKey = String.valueOf(day.getValue());
        String historicalKey = HistoricalKey.get(exercise);
        Editor editor = sharedPreferences.edit();

        EditingParameter parameter = new EditingParameter(uuidDayBankKey, historicalKey, editor);

        registerExerciseUnderSpecificDay(parameter);
        appendSnapshotToHistory(parameter, exercise);

        editor.apply();

    }

    private void registerExerciseUnderSpecificDay(EditingParameter parameter) {

        Editor editor = parameter.editor;
        String uuidDayBankKey = parameter.uuidDayBankKey;

        Set<String> uuidsOfDay = sharedPreferences.getStringSet(uuidDayBankKey, new HashSet<>());
        uuidsOfDay.add(parameter.historicalKey);
        editor.putStringSet(uuidDayBankKey, uuidsOfDay);

    }

    // not safe for all exercises
    private void appendSnapshotToHistory(EditingParameter parameter, Exercise exercise) {

        Editor editor = parameter.editor;
        String historicalKey = parameter.historicalKey;

        Object snapshot = SnapshotBuilder.from(exercise);
        List snapshots = getExistingSnapshots(exercise);
        snapshots.add(snapshot);

        History history = generateNewHistory(exercise, snapshots);
        String historyJson = new Gson().toJson(history);
        editor.putString(historicalKey, historyJson);


    }

    private History generateNewHistory(Exercise exercise, List snapshots) {
        switch (exercise.getExerciseType()) {
            case METER_RUN:
                return new StandardExerciseHistory(ExerciseType.METER_RUN, snapshots);
            case TIMED_RUN:
                return new StandardExerciseHistory(ExerciseType.TIMED_RUN, snapshots);
            case REPETITION_EXERCISE:
                RepetitionExercise repetitionExercise = (RepetitionExercise) exercise;
                return new RepetitionHistory(repetitionExercise.getName(), snapshots);
            case FREE_WEIGHT_EXERCISE:
                FreeWeightExercise freeWeightExercise = (FreeWeightExercise) exercise;
                return new FreeWeightHistory(freeWeightExercise.getName(), snapshots);
            case REST:
                return new StandardExerciseHistory(ExerciseType.REST, snapshots);
        }
        return null;
    }


    // safe for all exercises.
    private <T> List<T> getExistingSnapshots(Exercise exercise) {
        PlanLogReader reader = new PlanLogReader(planName);
        History history = reader.getHistory(exercise);
        if (history != null) {
            return (List<T>) history.getSnapshots();
        } else {
            return new ArrayList<>();
        }
    }

}
