<?php

require_once('settings.php');

/**
 * Class ExerciseValidator
 * See android app -- .models.exercises.ExerciseTranslator for correlation
 */
class ExerciseValidator
{

    public static function verifyExercise($exerciseArray) : bool {
        if (!self::verifyCommonExerciseProperties($exerciseArray)) {
            return false;
        }

        $exerciseTypeNum = $exerciseArray['exerciseType'];
        switch ($exerciseTypeNum) {
            case 0:
                return self::verifyMeterRun($exerciseArray);
            case 1:
                return self::verifyTimedRun($exerciseArray);
            case 2:
                return self::verifyRepetition($exerciseArray);
            case 3:
                return self::verifyFreeWeight($exerciseArray);
            case 4:
                return self::verifyRest($exerciseArray);
            case 5:
                return self::verifyDayCopier($exerciseArray);
            default:
                return false;
        }
    }

    /**
     * This will verify a given exercise array's basic exercise properties.
     * Basic Exercise Properties include a UUID & completion status & exercise type & possibly description.
     */
    private static function verifyCommonExerciseProperties($exerciseArray) : bool {
        if (!array_key_exists("uuid", $exerciseArray)) {
            return false;
        }

        if (!array_key_exists("completed", $exerciseArray)) {
            return false;
        }

        if (!array_key_exists("exerciseType", $exerciseArray)) {
            return false;
        }

        // verify v4 uuid
        $exerciseUuid = $exerciseArray['uuid'];
        $regexUuidv4 = '/^[0-9A-F]{8}-[0-9A-F]{4}-4[0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$/i';
        if (!preg_match($regexUuidv4, $exerciseUuid)) {
            return false;
        }

        // Verify valid exercise type.
        $exerciseTypeNum = $exerciseArray['exerciseType'];
        if (!is_numeric($exerciseTypeNum)) {
            return false;
        }

        // Verify boolean
        $completed = $exerciseArray['completed'];
        if (!is_bool($completed)) {
            return false;
        }

        // Verify max description length
        if (array_key_exists("description", $exerciseArray)) {
            $description = $exerciseArray['description'];
            if (strlen($description) > SETTING_MAX_DESCRIPTION_LENGTH) {
                return false;
            }
        }

        return true;

    }

    private static function verifyMeterRun($exerciseArray) : bool {
        $fields = ["description", "distance", "distanceUnits"];

        if (!self::verifyStrictExercisesIndexes($exerciseArray, $fields)) {
            return false;
        }

        $distance = $exerciseArray['distance'];
        if (!self::verifyGoodNumber($distance)) {
            return false;
        }

        $distanceUnits = $exerciseArray['distanceUnits'];
        if (strlen($distanceUnits) > SETTING_MAX_UNITS_LENGTH) {
            return false;
        }

        return true;
    }

    /**
     * This ensures that a given array only has the exercise field indexes specified.
     * Do not include uuid / exerciseType / completed, as the fields not unique to any exercises.
     */
    private static function verifyStrictExercisesIndexes($exerciseArray, array $indexes) : bool {

        $givenArraySize = count($exerciseArray) - 3; // -3 uuid & exerciseType & completed is already checked.
        $givenIndexesSize = count($indexes);

        if ($givenIndexesSize != $givenArraySize) {
            return false;
        }

        foreach ($indexes as $index) {
            if (!array_key_exists($index, $exerciseArray)) {
                return false;
            }
        }

        return true;
    }

    /**
     * A "good number" = 0 -> 100,000.
     * There should be no reason anyone needs to use a number higher, unless it's inside a description.
     */
    private static function verifyGoodNumber(...$numbers) : bool {

        foreach ($numbers as $number) {

            if (!is_numeric($number)) {
                return false;
            }

            if ($number < 0 || $number > 100000) {
                return false;
            }
        }

        return true;
    }

    private static function verifyTimedRun($exerciseArray) : bool {
        $fields = ["description", "seconds"];
        if (!self::verifyStrictExercisesIndexes($exerciseArray, $fields)) {
            return false;
        }

        $seconds = $exerciseArray['seconds'];
        if (!self::verifyGoodNumber($seconds)) {
            return false;
        }

        return true;
    }

    private static function verifyRepetition($exerciseArray) : bool {
        $fields = ["name", "description", "numberOfSets", "numberOfRepetitions", "restTimeBetweenSets"];
        if (!self::verifyStrictExercisesIndexes($exerciseArray, $fields)) {
            return false;
        }

        $name = $exerciseArray['name'];
        if (strlen($name) > SETTING_MAX_EXERCISE_NAME_LENGTH) {
            return false;
        }

        $numSets = $exerciseArray['numberOfSets'];
        $numReps = $exerciseArray['numberOfRepetitions'];
        $restTime = $exerciseArray['restTimeBetweenSets'];
        if (!self::verifyGoodNumber($numSets, $numReps, $restTime)) {
            return false;
        }

        return true;
    }

    private static function verifyFreeWeight($exerciseArray) : bool {

        $fields = ["name", "description", "numberOfSets", "numberOfRepetitions",
            "restTimeBetweenSets", "amountOfWeight", "amountOfWeightUnits"];

        if (!self::verifyStrictExercisesIndexes($exerciseArray, $fields)) {
            return false;
        }

        $name = $exerciseArray['name'];
        if (strlen($name) > SETTING_MAX_EXERCISE_NAME_LENGTH) {
            return false;
        }

        $numWeight = $exerciseArray['amountOfWeight'];
        $numSets = $exerciseArray['numberOfSets'];
        $numReps = $exerciseArray['numberOfRepetitions'];
        $restTime = $exerciseArray['restTimeBetweenSets'];
        if (!self::verifyGoodNumber($numWeight, $numSets, $numReps, $restTime)) {
            return false;
        }

        $numWeightUnits = $exerciseArray['amountOfWeightUnits'];
        if (strlen($numWeightUnits) > SETTING_MAX_UNITS_LENGTH) {
            return false;
        }

        return true;
    }

    private static function verifyRest($exerciseArray) : bool {
        $fields = ["amountOfTimeToRest"];
        if (!self::verifyStrictExercisesIndexes($exerciseArray, $fields)) {
            return false;
        }

        $restTime = $exerciseArray['amountOfTimeToRest'];
        if (!self::verifyGoodNumber($restTime)) {
            return false;
        }

        return true;
    }

    private static function verifyDayCopier($exerciseArray) : bool {
        $fields = ["dayBeingCopied"];
        if (!self::verifyStrictExercisesIndexes($exerciseArray, $fields)) {
            return false;
        }

        $dayNum = $exerciseArray['dayBeingCopied'];
        if (!is_numeric($dayNum)) {
            return false;
        }

        // Verify valid day #
        if ($dayNum < 0 || $dayNum > 6) {
            return false;
        }

        return true;

    }

}