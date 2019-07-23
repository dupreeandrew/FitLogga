package com.fitlogga.app.models.plan.log;

public class SQLSchema {

    // Plan Table
    public static final String TABLE_PLANS = "planTable";
    public static final String COLUMN_PLAN_ID = "plan_id";
    public static final String COLUMN_PLAN_NAME = "plan_name";

    // Exercises Table
    public static final String TABLE_EXERCISES = "exerciseTable";
    public static final String COLUMN_EXERCISE_ID = "exercise_id";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_TYPE_NUM = "type_num";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DAY_NUM = "day_num";

    // Exercise Log Table
    public static final String TABLE_LOG = "exerciseLogTable";
    public static final String COLUMN_LOG_ID = "log_id";
    public static final String COLUMN_NUM_SETS = "num_sets";
    public static final String COLUMN_NUM_REPS = "num_reps";
    public static final String COLUMN_NUM_WEIGHT = "num_weight";
    public static final String COLUMN_COMMON_VALUE = "common_value";
    public static final String COLUMN_TIMESTAMP = "timestamp";
}
