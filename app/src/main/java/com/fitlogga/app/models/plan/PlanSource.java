package com.fitlogga.app.models.plan;

import com.fitlogga.app.models.Day;

import java.util.EnumMap;

public interface PlanSource {
    PlanSummary getPlanSummary();
    EnumMap<Day, DailyRoutine> getDailyRoutines();
}
