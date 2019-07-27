package com.fitlogga.app.models.plan.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fitlogga.app.models.ApplicationContext;

import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_COMMON_VALUE;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_DAY_NUM;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_EXERCISE_ID;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_LOG_ID;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NAME;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NUM_REPS;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NUM_SETS;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_NUM_WEIGHT;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_PLAN_ID;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_PLAN_NAME;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_TIMESTAMP;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_TYPE_NUM;
import static com.fitlogga.app.models.plan.log.SQLSchema.COLUMN_UUID;
import static com.fitlogga.app.models.plan.log.SQLSchema.TABLE_EXERCISES;
import static com.fitlogga.app.models.plan.log.SQLSchema.TABLE_LOG;
import static com.fitlogga.app.models.plan.log.SQLSchema.TABLE_PLANS;

/*
Plan Table
planId (primary key) | plan_name |
1, My Fitness Plan

Exercises Table
primaryKey | planId | uuid | typeNum |
3, 1, ABCD-EFG3-LAK9, 3

Exercise Log Table
primary key | (exercises-table primaryKey)1 | num_sets | num_reps | | value | | timestamp |
1, 3, 3, 10, null, 52424222
*/
public abstract class SQLLog {

    private static final String databaseName = "exercise-log";

    static SQLiteDatabase database;

    private long planPrimaryKey;

    /**
     * Do not use unless you absolutely do not need a plan name
     */
    SQLLog() {
        ensureDatabaseIsInitialized();
    }

    private void ensureDatabaseIsInitialized() {
        if (SQLLog.database == null) {
            Context context = ApplicationContext.getInstance();
            SQLLog.database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        }
        createTables();
    }

    private void createTables() {
        createPlanTable();
        createExercisesTable();
        createLoggingTable();
    }

    private void createPlanTable() {
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_PLANS + " ("
                        + COLUMN_PLAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                        + COLUMN_PLAN_NAME + " TEXT UNIQUE);");
        database.execSQL(
                "CREATE INDEX IF NOT EXISTS planNameIndex ON "
                        + TABLE_PLANS + "(" + COLUMN_PLAN_NAME + ")");
    }

    private void createExercisesTable() {
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_EXERCISES + " ("
                        + COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                        + COLUMN_PLAN_ID + " INTEGER REFERENCES " + TABLE_PLANS + "(" + COLUMN_PLAN_ID + "),"
                        + COLUMN_UUID + " TEXT UNIQUE,"
                        + COLUMN_TYPE_NUM + " INTEGER,"
                        + COLUMN_NAME + " TEXT,"
                        + COLUMN_DAY_NUM + " INTEGER);");
        database.execSQL(
                "CREATE INDEX IF NOT EXISTS planNameIndex ON "
                        + TABLE_EXERCISES + "(" + COLUMN_UUID + ")");
        database.execSQL(
                "CREATE INDEX IF NOT EXISTS planNameIndex ON "
                        + TABLE_EXERCISES + "(" + COLUMN_DAY_NUM + ")");

    }

    private void createLoggingTable() {
        database.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_LOG + " ("
                        + COLUMN_LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                        + COLUMN_EXERCISE_ID + " INTEGER REFERENCES " + TABLE_EXERCISES + "(" + COLUMN_EXERCISE_ID + "),"
                        + COLUMN_NUM_SETS + " INTEGER,"
                        + COLUMN_NUM_REPS + " INTEGER,"
                        + COLUMN_NUM_WEIGHT + " INTEGER,"
                        + COLUMN_COMMON_VALUE + " INTEGER,"
                        + COLUMN_TIMESTAMP + " TEXT);");
    }

    SQLLog(String planName) {
        ensureDatabaseIsInitialized();
        insertPlanIntoPlanTable(planName);
        initPlanPrimaryKey(planName);
    }

    private void insertPlanIntoPlanTable(String planName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PLAN_NAME, planName);
        database.insertWithOnConflict(TABLE_PLANS, null,
                contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void initPlanPrimaryKey(String planName) {
        String selection = COLUMN_PLAN_NAME + " = ?";
        String[] selectionArgs = {planName};
        Cursor cursor = database.query(
                TABLE_PLANS,
                new String[]{COLUMN_PLAN_ID},
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        cursor.moveToNext();
        int columnNum = cursor.getColumnIndex(COLUMN_PLAN_ID);
        this.planPrimaryKey = cursor.getLong(columnNum);
        cursor.close();
    }

    public final long getPlanPrimaryKey() {
        return this.planPrimaryKey;
    }

}
