<?php

require_once('settings.php');
require_once('ExerciseValidator.php');

//$payload = $_GET['payload'];
$payload = "{\"description\":\"hey\",\"lastUsed\":1565295604969,\"name\":\"myplanlol\"}!%@%@%!{\"0\":[{\"description\":\"Some description\",\"seconds\":60,\"completed\":false,\"exerciseType\":\"1\",\"uuid\":\"1df2c824-c4e2-408d-aa08-4559759ab1e5\"},{\"description\":\"Some description\",\"distance\":200,\"distanceUnits\":\"meters\",\"completed\":false,\"exerciseType\":\"0\",\"uuid\":\"13ee4c41-c1c7-49b4-b378-40f51a182741\"},{\"description\":\"Some description\",\"name\":\"Ab Crunches\",\"numberOfRepetitions\":5,\"numberOfSets\":3,\"restTimeBetweenSets\":120,\"completed\":false,\"exerciseType\":\"2\",\"uuid\":\"611d97c9-1a0d-4065-bfaa-33991b25a50c\"},{\"amountOfWeight\":135,\"amountOfWeightUnits\":\"lb\",\"description\":\"\",\"name\":\"Front Barbell Squat\",\"numberOfRepetitions\":7,\"numberOfSets\":3,\"restTimeBetweenSets\":150,\"completed\":false,\"exerciseType\":\"3\",\"uuid\":\"e84d5657-db9a-4b45-9022-8712eab57fc5\"},{\"amountOfTimeToRest\":240,\"completed\":false,\"exerciseType\":\"4\",\"uuid\":\"479b83f0-0d4a-4802-84b9-644a6e284e3e\"}],\"3\":[{\"dayBeingCopied\":\"0\",\"completed\":false,\"exerciseType\":\"5\",\"uuid\":\"1a57011b-833a-4726-9720-745b3525727c\"}],\"6\":[{\"description\":\"\",\"distance\":10,\"distanceUnits\":\"meters\",\"completed\":false,\"exerciseType\":\"0\",\"uuid\":\"ab7a2807-8fd0-449e-949c-19baca39f934\"}]}";

$planPieces = explode("!%@%@%!", $payload);
$planSummaryData = $planPieces[0];
$dailyRoutineMap = $planPieces[1];

if (jsonIsUnhealthy($planSummaryData, $dailyRoutineMap)) {
    die();
}

//[0] == planSummaryData, [1] = dailyRoutineMap. Nothing else.
if (count($planPieces) != 2) {
    die();
}

if (!receivedValidPlanSummary($planSummaryData)) {
    die();
}

if (!receivedValidDailyRoutineMap($dailyRoutineMap)) {
    die();
}

echo "aK39kZ"; // generate unique identifier for export.

function jsonIsUnhealthy(...$jsonArgs) : bool {
    foreach ($jsonArgs as $jsonArg) {
        $minifiedJson = json_encode(json_decode($jsonArg));
        if ($minifiedJson != $jsonArg) {
            return true;
        }
    }
    return false;
}

function receivedValidPlanSummary($planSummaryData) : bool {

    /*
     * Sample planSummaryData:
     * {"description":"hey","lastUsed":1565249512346,"name":"heresMyPlan"}
     */

    $result = json_decode($planSummaryData, true);

    // Ensure only 3 indexes exist.
    if (count($result) != 3) {
        return false;
    }

    // Ensure required indexes exist.
    if (!array_key_exists("description", $result)
        || !array_key_exists("lastUsed", $result)
        || !array_key_exists("name", $result)) {
        return false;
    }

    // Ensure proper size plan name length
    $planName = $result[INDEX_NAME];
    $planNameLength = strlen($planName);
    if ($planNameLength < SETTING_MIN_PLAN_NAME_LENGTH || $planNameLength > SETTING_MAX_PLAN_NAME_LENGTH) {
        return false;
    }

    // Ensure timestamp is a number
    $lastUsed = $result[INDEX_LAST_USED];
    if (!is_numeric($lastUsed)) {
        // Timestamp doesn't really need to be validated. The app will adjust it to the present upon import.
        return false;
    }

    // Ensure description is no greater than the max description length
    $description = $result[INDEX_DESCRIPTION];
    if (strlen($description) > SETTING_MAX_DESCRIPTION_LENGTH) {
        return false;
    }

    return true;

}

function receivedValidDailyRoutineMap($dailyRoutineMap) : bool {
    $result = json_decode($dailyRoutineMap, true);

    // Ensure keys all represent a day number. 0 = SUNDAY, 6 = SATURDAY
    $dayKeys = array_keys($result);
    foreach ($dayKeys as $dayKey) {
        if (!is_numeric($dayKey)) {
            return false;
        }
        if ($dayKey < 0 || $dayKey > 6) {
            return false;
        }
    }

    // Ensure max indexes is 7, as there are 7 days of the week.
    if (count($dayKeys) > 7) {
        return false;
    }

    foreach ($result as $exerciseArrayList) {
        foreach ($exerciseArrayList as $exerciseArray) {
            if (!ExerciseValidator::verifyExercise($exerciseArray)) {
                return false;
            }
        }
    }

    return true;

}

include('database.php');
//$dbc = getDatabase();