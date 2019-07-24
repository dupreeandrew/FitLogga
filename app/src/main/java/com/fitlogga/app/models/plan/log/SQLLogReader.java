package com.fitlogga.app.models.plan.log;

import android.database.Cursor;

import androidx.annotation.IntRange;

import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.ExerciseType;
import com.fitlogga.app.models.plan.log.Historics.FreeWeightHistory;
import com.fitlogga.app.models.plan.log.Historics.History;
import com.fitlogga.app.models.plan.log.Historics.RepetitionHistory;
import com.fitlogga.app.models.plan.log.Historics.StandardExerciseHistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_COMMON_VALUE;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_DAY_NUM;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_EXERCISE_ID;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NAME;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NUM_REPS;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NUM_SETS;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NUM_WEIGHT;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_PLAN_ID;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_TIMESTAMP;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_TYPE_NUM;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_UUID;
import static com.fitlogga.app.models.plan.log.SQLSchema.TABLE_EXERCISES;
import static com.fitlogga.app.models.plan.log.SQLSchema.TABLE_LOG;

public class SQLLogReader extends SQLLog {

    public SQLLogReader(String planName) {
        super(planName);
    }

    /**
     * Returns the history of all exercises under a specified day.
     * All history objects return snapshots sorted by their timestamp, in descended order.
     */
    public List<History> getHistoryList(Day day, @IntRange(from = 0, to = 500) int maxSize) {

        List<History> historyList = new ArrayList<>();
        Set<String> exerciseUuids = getExerciseUuids(day.getValue());
        for (String uuid : exerciseUuids) {
            History history = getHistory(uuid, maxSize);
            historyList.add(history);
        }

        return historyList;
    }

    private Set<String> getExerciseUuids(int dayNum) {
        String selection = COLUMN_DAY_NUM + " = ? AND " + COLUMN_PLAN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(dayNum), String.valueOf(getPlanPrimaryKey())};
        Cursor cursor = database.query(
                TABLE_EXERCISES,
                new String[]{COLUMN_UUID},
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Set<String> exerciseUuids = new HashSet<>();
        while (cursor.moveToNext()) {
            int columnNum = cursor.getColumnIndex(COLUMN_UUID);
            String uuid = cursor.getString(columnNum);
            exerciseUuids.add(uuid);
        }

        cursor.close();
        return exerciseUuids;
    }

    private <T extends History> T getHistory(String uuid, int limit) {
        Map<String, Object> exerciseInfo = getExerciseInfo(uuid);
        List<Map<String, Object>> rowsOfExercise = getLogRowsOfExercise(uuid, limit);
        return generateHistoryObject(exerciseInfo, rowsOfExercise);
    }

    /**
     * Returns:
     * (nullable) COLUMN_TYPE_NUM -> ExerciseType INSTANCE
     * (nullable) COLUMN_NAME -> String
     */
    private Map<String, Object> getExerciseInfo(String uuid) {
        String selection = COLUMN_UUID + " = ?";
        String[] selectionArgs = {uuid};
        Cursor cursor = database.query(
                TABLE_EXERCISES,
                new String[]{COLUMN_TYPE_NUM, COLUMN_NAME},
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        cursor.moveToNext();

        int typeColumnNum = cursor.getColumnIndex(COLUMN_TYPE_NUM);
        int nameColumnNum = cursor.getColumnIndex(COLUMN_NAME);

        int type = cursor.getInt(typeColumnNum);
        String name = cursor.getString(nameColumnNum);

        cursor.close();

        Map<String, Object> exerciseInfo = new HashMap<>();
        exerciseInfo.put(COLUMN_TYPE_NUM, ExerciseType.fromInteger(type));
        exerciseInfo.put(COLUMN_NAME, name);

        return exerciseInfo;
    }

    @SuppressWarnings("unchecked")
    private <T extends History> T generateHistoryObject(Map<String, Object> exerciseInfo,
                                                        List<Map<String, Object>> rowsOfExercise) {

        ExerciseType exerciseType = (ExerciseType) exerciseInfo.get(COLUMN_TYPE_NUM);
        String name = (String) exerciseInfo.get(COLUMN_NAME);

        switch (exerciseType) {
            case METER_RUN:
            case TIMED_RUN:
            case REST:
                List<StandardExerciseHistory.Snapshot> standardSnapshots
                        = getStandardSnapshotList(rowsOfExercise);
                return (T) new StandardExerciseHistory(exerciseType, standardSnapshots);
            case REPETITION_EXERCISE:
                List<RepetitionHistory.Snapshot> repetitionSnapshots
                        = getRepetitionSnapshotList(rowsOfExercise);
                return (T) new RepetitionHistory(name, repetitionSnapshots);
            case FREE_WEIGHT_EXERCISE:
                List<FreeWeightHistory.Snapshot> freeWeightSnapshots
                        = getFreeWeightSnapshotList(rowsOfExercise);
                return (T) new FreeWeightHistory(name, freeWeightSnapshots);
            default:
                throw new IllegalArgumentException();

        }
    }

    private List<Map<String, Object>> getLogRowsOfExercise(String uuid, int limit) {
        String query = "SELECT "
                + COLUMN_COMMON_VALUE + ", "
                + COLUMN_NUM_SETS + ", "
                + COLUMN_NUM_REPS + ", "
                + COLUMN_NUM_WEIGHT + ", "
                + COLUMN_TIMESTAMP + " "
                + "FROM " + TABLE_EXERCISES + " "
                + "INNER JOIN " + TABLE_LOG + " AS '" + TABLE_LOG + "'"
                + "ON " + TABLE_EXERCISES + "." + COLUMN_EXERCISE_ID + " = " + TABLE_LOG + "." + COLUMN_EXERCISE_ID + " "
                + "WHERE " + COLUMN_UUID + "=? "
                + "ORDER BY " + COLUMN_TIMESTAMP + " DESC "
                + "LIMIT " + limit;

        Cursor cursor = database.rawQuery(query, new String[]{uuid});
        List<Map<String, Object>> rowsOfExercise = new ArrayList<>();
        while (cursor.moveToNext()) {
            int commonValueIndex = cursor.getColumnIndex(COLUMN_COMMON_VALUE);
            int numSetsIndex = cursor.getColumnIndex(COLUMN_NUM_SETS);
            int numRepsIndex = cursor.getColumnIndex(COLUMN_NUM_REPS);
            int numWeightIndex = cursor.getColumnIndex(COLUMN_NUM_WEIGHT);
            int timestampIndex = cursor.getColumnIndex(COLUMN_TIMESTAMP);

            int commonValue = cursor.getInt(commonValueIndex);
            int numSets = cursor.getInt(numSetsIndex);
            int numReps = cursor.getInt(numRepsIndex);
            int numWeight = cursor.getInt(numWeightIndex);
            long timestamp = cursor.getLong(timestampIndex);

            Map<String, Object> exerciseLogEntryMap = new HashMap<>();
            exerciseLogEntryMap.put(COLUMN_COMMON_VALUE, commonValue);
            exerciseLogEntryMap.put(COLUMN_NUM_SETS, numSets);
            exerciseLogEntryMap.put(COLUMN_NUM_REPS, numReps);
            exerciseLogEntryMap.put(COLUMN_NUM_WEIGHT, numWeight);
            exerciseLogEntryMap.put(COLUMN_TIMESTAMP, timestamp);

            rowsOfExercise.add(exerciseLogEntryMap);

        }
        cursor.close();

        return rowsOfExercise;



    }

    private List<StandardExerciseHistory.Snapshot> getStandardSnapshotList(
            List<Map<String, Object>> exerciseLogEntryMap) {

        List<StandardExerciseHistory.Snapshot> snapshotList = new ArrayList<>();
        for (Map<String, Object> exerciseLogEntry : exerciseLogEntryMap){
            long timestamp = (long) exerciseLogEntry.get(COLUMN_TIMESTAMP);
            int commonValue = (int) exerciseLogEntry.get(COLUMN_COMMON_VALUE);
            snapshotList.add(new StandardExerciseHistory.Snapshot(timestamp, commonValue));
        }

        return snapshotList;

    }

    private List<RepetitionHistory.Snapshot> getRepetitionSnapshotList(
            List<Map<String, Object>> exerciseLogEntryMap) {

        List<RepetitionHistory.Snapshot> snapshotList = new ArrayList<>();
        for (Map<String, Object> exerciseLogEntry : exerciseLogEntryMap){
            int sets = (int) exerciseLogEntry.get(COLUMN_NUM_SETS);
            int reps = (int) exerciseLogEntry.get(COLUMN_NUM_REPS);
            long timestamp = (long) exerciseLogEntry.get(COLUMN_TIMESTAMP);
            snapshotList.add(new RepetitionHistory.Snapshot(sets, reps, timestamp));
        }

        return snapshotList;

    }

    private List<FreeWeightHistory.Snapshot> getFreeWeightSnapshotList(
            List<Map<String, Object>> exerciseLogEntryMap) {

        List<FreeWeightHistory.Snapshot> snapshotList = new ArrayList<>();
        for (Map<String, Object> exerciseLogEntry : exerciseLogEntryMap){
            int sets = (int) exerciseLogEntry.get(COLUMN_NUM_SETS);
            int reps = (int) exerciseLogEntry.get(COLUMN_NUM_REPS);
            int weight = (int) exerciseLogEntry.get(COLUMN_NUM_WEIGHT);
            long timestamp = (long) exerciseLogEntry.get(COLUMN_TIMESTAMP);
            snapshotList.add(new FreeWeightHistory.Snapshot(sets, reps, weight, timestamp));
        }

        return snapshotList;

    }

}
