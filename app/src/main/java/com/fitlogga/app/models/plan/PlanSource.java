package com.fitlogga.app.models.plan;

import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;

import java.util.EnumMap;
import java.util.List;

public interface PlanSource {
    PlanSummary getPlanSummary();
    EnumMap<Day, List<Exercise>> getDailyRoutines();
}
