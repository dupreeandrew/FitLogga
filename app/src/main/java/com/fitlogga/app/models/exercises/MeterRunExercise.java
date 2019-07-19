package com.fitlogga.app.models.exercises;

import androidx.annotation.Nullable;

public class MeterRunExercise extends Exercise {

    private int distance;
    private String distanceUnits;
    private String description;

    public MeterRunExercise(String description, int distance, String distanceUnits) {
        this(description, distance, distanceUnits, false, null);
    }

    public MeterRunExercise(String description, int distance, String units, boolean completed,
                            @Nullable String uuid) {
        super(ExerciseType.METER_RUN, uuid);
        this.description = description;
        this.distance = distance;
        this.distanceUnits = units;
        setCompleted(false);
    }

    public int getDistance() {
        return distance;
    }

    public String getDistanceUnits() {
        return distanceUnits;
    }

    public String getDescription() {
        return description;
    }


}
