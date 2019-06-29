package com.fitlogga.app.models.exercises;

public class MeterRunExercise extends Exercise {

    private int distance;
    private String distanceUnits;
    private String description;

    public MeterRunExercise(String description, int distance, String distanceUnits) {
        super(ExerciseType.METER_RUN);
        this.description = description;
        this.distance = distance;
        this.distanceUnits = distanceUnits;
    }

    public MeterRunExercise(String description, int distance, String units, boolean completed) {
        this(description, distance, units);
        setCompleted(completed);
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
