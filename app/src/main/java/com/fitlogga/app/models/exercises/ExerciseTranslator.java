package com.fitlogga.app.models.exercises;

import com.fitlogga.app.models.Day;

import java.util.Map;

public class ExerciseTranslator {

    public static Exercise toExercise(Map<String, Object> exerciseMap) {

        int exerciseTypeInteger = Integer.parseInt((String)exerciseMap.get("exerciseType"));
        ExerciseType exerciseType = ExerciseType.fromInteger(exerciseTypeInteger);

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
        String uuid = getUuid(exerciseMap);
        return new MeterRunExercise(description, distance, units, completed, uuid);
    }

    private static String getUuid(Map<String, Object> exerciseMap) {
        return (String)exerciseMap.get("uuid");
    }

    private static TimedRunExercise toTimedRun(Map<String, Object> exerciseMap) {
        String description = (String)exerciseMap.get("description");
        int seconds = quickCastToInt(exerciseMap.get("seconds"));
        boolean completed = (boolean)exerciseMap.get("completed");
        String uuid = (String)exerciseMap.get("uuid");
        return new TimedRunExercise(description, seconds, completed, uuid);
    }

    private static RepetitionExercise toRepetition(Map<String, Object> exerciseMap) {
        String name = (String)exerciseMap.get("name");
        String description = (String)exerciseMap.get("description");
        int numberOfSets = quickCastToInt(exerciseMap.get("numberOfSets"));
        int numberOfRepetitions = quickCastToInt(exerciseMap.get("numberOfRepetitions"));
        int restTimeInBetweenSets = quickCastToInt(exerciseMap.get("restTimeBetweenSets"));
        boolean completed = (boolean)exerciseMap.get("completed");
        String uuid = getUuid(exerciseMap);

        return new RepetitionExercise.Builder()
                .setName(name)
                .setDescription(description)
                .setNumberOfSets(numberOfSets)
                .setNumberOfReps(numberOfRepetitions)
                .setRestTimeBetweenSets(restTimeInBetweenSets)
                .setCompleted(completed)
                .setUuid(uuid)
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
        String uuid = getUuid(exerciseMap);

        return new FreeWeightExercise.Builder()
                .setName(name)
                .setDescription(description)
                .setAmountOfWeight(amountOfWeight)
                .setAmountOfWeightUnits(amountOfWeightUnits)
                .setNumberOfSets(numberOfSets)
                .setNumberOfReps(numberOfRepetitions)
                .setRestTimeBetweenSets(restTimeInBetweenSets)
                .setCompleted(completed)
                .setUuid(uuid)
                .build();

    }

    private static RestExercise toRest(Map<String, Object> exerciseMap) {
        int amountOfTimeToRest = quickCastToInt(exerciseMap.get("amountOfTimeToRest"));
        boolean completed = (boolean)exerciseMap.get("completed");
        String uuid = getUuid(exerciseMap);
        return new RestExercise(amountOfTimeToRest, completed, uuid);
    }

    private static DayCopierExercise toCopier(Map<String, Object> exerciseMap) {
        int dayNum = Integer.parseInt(String.valueOf(exerciseMap.get("dayBeingCopied")));
        Day day = Day.fromValue(dayNum);
        return new DayCopierExercise(day);
    }
}
