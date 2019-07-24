package com.fitlogga.app.adapters.graphlog.viewholders;

import android.util.Log;

import com.fitlogga.app.models.exercises.ExerciseType;
import com.fitlogga.app.models.plan.log.Historics.FreeWeightHistory;
import com.fitlogga.app.models.plan.log.Historics.History;
import com.fitlogga.app.models.plan.log.Historics.RepetitionHistory;
import com.fitlogga.app.models.plan.log.Historics.StandardExerciseHistory;
import com.fitlogga.app.viewmods.datelinechart.DateLineCharter;

import java.util.ArrayList;
import java.util.List;

public class LineChartUnitGen {

    public static DateLineCharter.Unit get(History history) {
        Log.d("we are", "returning a unit");
        ExerciseType exerciseType = history.getExerciseType();
        switch (exerciseType) {
            case TIMED_RUN:
            case METER_RUN:
            case REST:
                StandardExerciseHistory standardExerciseHistory = (StandardExerciseHistory)history;
                return get(standardExerciseHistory);
            case REPETITION_EXERCISE:
                RepetitionHistory repetitionHistory = (RepetitionHistory)history;
                return get(repetitionHistory);
            case FREE_WEIGHT_EXERCISE:
                FreeWeightHistory freeWeightHistory = (FreeWeightHistory)history;
                return get(freeWeightHistory);
            default:
                throw new IllegalArgumentException();
        }
    }

    private static DateLineCharter.Unit get(StandardExerciseHistory history) {
        List<Long> timestamps = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        for (StandardExerciseHistory.Snapshot snapshot : history.getSnapshots()) {
            int value = snapshot.getValue();
            valueList.add(value);

            long timestamp = snapshot.getTimestamp();
            timestamps.add(timestamp);
        }

        String label = "units";
        DateLineCharter.Data data = new DateLineCharter.Data(label, valueList);

        return new DateLineCharter.Unit(timestamps, data);
    }

    private static DateLineCharter.Unit get(RepetitionHistory history) {
        List<Long> timestamps = new ArrayList<>();
        List<Integer> setList = new ArrayList<>();
        List<Integer> repList = new ArrayList<>();
        for (RepetitionHistory.Snapshot snapshot : history.getSnapshots()) {
            int sets = snapshot.getSets();
            int reps = snapshot.getReps();
            long timestamp = snapshot.getTimestamp();

            setList.add(sets);
            repList.add(reps);
            timestamps.add(timestamp);
        }

        String setsLabel = "sets";
        DateLineCharter.Data setsData = new DateLineCharter.Data(setsLabel, setList);

        String repLabel = "reps";
        DateLineCharter.Data repData = new DateLineCharter.Data(repLabel, repList);

        return new DateLineCharter.Unit(timestamps, setsData, repData);
    }

    private static DateLineCharter.Unit get(FreeWeightHistory history) {
        List<Long> timestamps = new ArrayList<>();
        List<Integer> valueList = new ArrayList<>();
        for (FreeWeightHistory.Snapshot snapshot : history.getSnapshots()) {
            int value = snapshot.getWeight();
            valueList.add(value);

            long timestamp = snapshot.getTimestamp();
            timestamps.add(timestamp);
        }

        String label = "units";
        DateLineCharter.Data data = new DateLineCharter.Data(label, valueList);

        return new DateLineCharter.Unit(timestamps, data);
    }



}