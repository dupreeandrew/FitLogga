package com.fitlogga.app.models.plan;

import androidx.annotation.NonNull;

public class PlanSummary {

    private String name;
    private String description;
    private long lastUsed;

    public PlanSummary(@NonNull String name, String description, long lastUsed) {
        this.name = name;
        this.description = description;
        this.lastUsed = lastUsed;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getLastUsed() {
        return lastUsed;
    }
}
