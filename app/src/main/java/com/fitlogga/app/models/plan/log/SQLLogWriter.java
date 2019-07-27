package com.fitlogga.app.models.plan.log;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseType;
import com.fitlogga.app.models.exercises.FreeWeightExercise;
import com.fitlogga.app.models.exercises.RepetitionExercise;

import java.util.List;

import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_DAY_NUM;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_EXERCISE_ID;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NAME;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_PLAN_ID;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_PLAN_NAME;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_TYPE_NUM;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_UUID;
import static com.fitlogga.app.models.plan.log.SQLSchema.TABLE_EXERCISES;
import static com.fitlogga.app.models.plan.log.SQLSchema.TABLE_LOG;
import static com.fitlogga.app.models.plan.log.SQLSchema.TABLE_PLANS;

public class SQLLogWriter extends SQLLog {

    private String planName;

    public SQLLogWriter(String planName) {
        super(planName);
        this.planName = planName;
    }

    /**
     * Append a list using database transaction performance
     */
    public void append(List<Exercise> exerciseList, Day day) {
        database.beginTransaction();
        for (Exercise exercise : exerciseList) {
            append(exercise, day);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void append(Exercise exercise, Day day) {
        ensureExerciseIsInExercisesTable(exercise, day.getValue());
        long primaryKey = getPrimaryKeyFromUuid(exercise.getUuid());
        considerSettingNameToTheTable(exercise, primaryKey);
        appendExerciseToLogTable(exercise, primaryKey);
    }

    private void ensureExerciseIsInExercisesTable(Exercise exercise, int dayValue) {
        ContentValues exerciseContentValues = new ContentValues();
        exerciseContentValues.put(COLUMN_UUID, exercise.getUuid());
        exerciseContentValues.put(COLUMN_PLAN_ID, getPlanPrimaryKey());
        exerciseContentValues.put(COLUMN_TYPE_NUM, exercise.getExerciseType().getExerciseTypeValue());
        exerciseContentValues.put(COLUMN_DAY_NUM, dayValue);

        database.insertWithOnConflict(TABLE_EXERCISES, null,
                exerciseContentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void considerSettingNameToTheTable(Exercise exercise, long primaryKey) {
        ExerciseType exerciseType = exercise.getExerciseType();
        if (exerciseType == ExerciseType.REPETITION_EXERCISE) {
            String name = ((RepetitionExercise)exercise).getName();
            addNameToExercise(name, primaryKey);
        }
        else if (exerciseType == ExerciseType.FREE_WEIGHT_EXERCISE) {
            String name = ((FreeWeightExercise)exercise).getName();
            addNameToExercise(name, primaryKey);
        }
    }

    private void addNameToExercise(String name, long primaryKey) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);

        String selection = COLUMN_EXERCISE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(primaryKey)};
        database.update(
                TABLE_EXERCISES,
                contentValues,
                selection,
                selectionArgs
        );

        // querying by primary key is blazing fast compared to uuid search

    }

    private long getPrimaryKeyFromUuid(String uuid) {
        String selection = COLUMN_UUID + " = ?";
        String[] selectionArgs = new String[]{uuid};
        Cursor cursor = database.query(TABLE_EXERCISES, new String[]{COLUMN_EXERCISE_ID},
                selection, selectionArgs,
                null, null, null);

        cursor.moveToNext();
        int columnNum = cursor.getColumnIndex(COLUMN_EXERCISE_ID);
        long primaryKey = cursor.getLong(columnNum);
        cursor.close();
        return primaryKey;
    }

    private void appendExerciseToLogTable(Exercise exercise, long primaryKey) {
        ContentValues logContentValues = SQLContentLogGen.from(exercise);
        logContentValues.put(COLUMN_EXERCISE_ID, primaryKey);
        database.insert(TABLE_LOG, null, logContentValues);
    }

    public static void delete(String uuid) {
        String whereClause = COLUMN_UUID + " = ?";
        new SQLLog(){}.database
                .delete(
                        TABLE_EXERCISES,
                        whereClause,
                        new String[]{uuid}
                        );
    }

    public void updatePlanName(String newPlanName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PLAN_NAME, newPlanName);
        String selection = COLUMN_PLAN_NAME + " = ?";
        database.update(TABLE_PLANS, contentValues, selection, new String[]{planName});
    }
}
