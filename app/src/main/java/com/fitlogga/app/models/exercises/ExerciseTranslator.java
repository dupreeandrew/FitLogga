package com.fitlogga.app.models.exercises;

import com.fitlogga.app.models.Day;

import java.util.Map;

public class ExerciseTranslator {

    public static Exercise toExercise(ExerciseType exerciseType, Map<String, Object> exerciseMap) {
        switch (exerciseType) {
            case METER_RUN:
                return toMeterRun(exerciseMap);
            case TIMED_RUN:
                return toTimedRun(exerciseMap);
            case REPETITION_EXERCISE:
                return toRepetition(exerciseMap);
            case FREE_WEIGHT_EXERCISE:
                return toFreeWeight(exerciseMap);
            case REST:
                return toRest(exerciseMap);
            case COPIER:
                return toCopier(exerciseMap);
            default:
                return null;
        }
    }

    private static int quickCastToInt(Object o) {
        return ((Double)o).intValue();
    }

    private static MeterRunExercise toMeterRun(Map<String, Object> exerciseMap) {
        String description = (String)exerciseMap.get("description");
        int distance = quickCastToInt(exerciseMap.get("distance"));
        String units = (String)exerciseMap.get("distanceUnits");
        boolean completed = (boolean)exerciseMap.get("completed");
        return new MeterRunExercise(description, distance, units, completed);
    }

    private static TimedRunExercise toTimedRun(Map<String, Object> exerciseMap) {
        String description = (String)exerciseMap.get("description");
        int seconds = quickCastToInt(exerciseMap.get("seconds"));
        boolean completed = (boolean)exerciseMap.get("completed");
        return new TimedRunExercise(description, seconds, completed);
    }

    private static RepetitionExercise toRepetition(Map<String, Object> exerciseMap) {
        String name = (String)exerciseMap.get("name");
        String description = (String)exerciseMap.get("description");
        int numberOfSets = quickCastToInt(exerciseMap.get("numberOfSets"));
        int numberOfRepetitions = quickCastToInt(exerciseMap.get("numberOfRepetitions"));
        int restTimeInBetweenSets = quickCastToInt(exerciseMap.get("restTimeBetweenSets"));
        boolean completed = (boolean)exerciseMap.get("completed");

        return new RepetitionExercise.Builder()
                .setName(name)
                .setDescription(description)
                .setNumberOfSets(numberOfSets)
                .setNumberOfReps(numberOfRepetitions)
                .setRestTimeBetweenSets(restTimeInBetweenSets)
                .setCompleted(completed)
                .build();
    }

    private static FreeWeightExercise toFreeWeight(Map<String, Object> exerciseMap) {
        String name = (String)exerciseMap.get("name");
        String description = (String)exerciseMap.get("description");
        int amountOfWeight = quickCastToInt(exerciseMap.get("amountOfWeight"));
        String amountOfWeightUnits = (String)exerciseMap.get("amountOfWeightUnits");
        int numberOfSets = quickCastToInt(exerciseMap.get("numberOfSets"));
        int numberOfRepetitions = quickCastToInt(exerciseMap.get("numberOfRepetitions"));
        int restTimeInBetweenSets = quickCastToInt(exerciseMap.get("restTimeBetweenSets"));
        boolean completed = (boolean)exerciseMap.get("completed");

        return new FreeWeightExercise.Builder()
                .setName(name)
                .setDescription(description)
                .setAmountOfWeight(amountOfWeight)
                .setAmountOfWeightUnits(amountOfWeightUnits)
                .setNumberOfSets(numberOfSets)
                .setNumberOfReps(numberOfRepetitions)
                .setRestTimeBetweenSets(restTimeInBetweenSets)
                .setCompleted(completed)
                .build();

    }

    private static RestExercise toRest(Map<String, Object> exerciseMap) {
        int amountOfTimeToRest = quickCastToInt(exerciseMap.get("amountOfTimeToRest"));
        boolean completed = (boolean)exerciseMap.get("completed");
        return new RestExercise(amountOfTimeToRest, completed);
    }

    private static DayCopierExercise toCopier(Map<String, Object> exerciseMap) {
        String dayString = String.valueOf(exerciseMap.get("dayBeingCopied"));
        Day day = Day.valueOf(dayString);
        return new DayCopierExercise(day);
    }
}
