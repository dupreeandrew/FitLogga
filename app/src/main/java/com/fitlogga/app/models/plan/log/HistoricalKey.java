package com.fitlogga.app.models.plan.log;

import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseType;

public class HistoricalKey {

    public static String get(Exercise exercise) {
        int exerciseTypeValue = exercise.getExerciseType().getExerciseTypeValue();
        return exerciseTypeValue + "-" + exercise.getUuid();
    }

    public static String readUuid(String key) {
        verifyValidKey(key);
        return key.substring(2);
    }

    private static void verifyValidKey(String key) {

        /*
        Recall that a key is 38 characters.
        The first character is the exercise type value, followed by a "-",
        and then the exercise's Uuid
         */

        final int KEY_LENGTH = 38; // uuid is 36 characters. The first charact
        if (key.length() != KEY_LENGTH) {
            throwIllegalKeyException(key);
        }

        char firstChar = key.charAt(0);
        if (!Character.isDigit(firstChar)) {
            throwIllegalKeyException(key);
        }

        char secondChar = key.charAt(1);
        if (secondChar != '-') {
            throwIllegalKeyException(key);
        }

    }

    private static void throwIllegalKeyException(String key) {
        throw new IllegalArgumentException("Illegal key received: " + key);
    }

    public static ExerciseType readExerciseType(String key) {
        verifyValidKey(key);
        char firstChar = key.charAt(0);
        int exerciseTypeValue = Integer.parseInt(String.valueOf(firstChar));
        return ExerciseType.fromInteger(exerciseTypeValue);
    }

}
