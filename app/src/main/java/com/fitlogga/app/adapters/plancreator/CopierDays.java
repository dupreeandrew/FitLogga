package com.fitlogga.app.adapters.plancreator;

import androidx.annotation.Nullable;

import com.fitlogga.app.models.Day;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * This class allows you to mark a day as a copier.
 * A CopierDay is a day that has no exercises, but only copies off another day.
 */
public class CopierDays {

    private EnumMap<Day, Day> copierDayMap = new EnumMap<>(Day.class);

    CopierDays() {
        for (Day day : Day.values()) {
            copierDayMap.put(day, null);
        }
    }

    public void setDayAsCopier(Day dayCopier, @Nullable Day dayBeingCopied) {
        copierDayMap.put(dayCopier, dayBeingCopied);
    }

    public boolean isDayCopier(Day day) {
        return copierDayMap.get(day) != null;
    }

    /**
     * @return A list of days that are copying some other day.
     */
    public List<Day> getCopierDays() {
        List<Day> copiedDays = new ArrayList<>();
        for (Map.Entry<Day, Day> entry : copierDayMap.entrySet()) {
            boolean isCopied = entry.getValue() != null;
            if (isCopied) {
                Day day = entry.getKey();
                copiedDays.add(day);
            }
        }
        return copiedDays;

    }

    public boolean isDayBeingCopied(Day day) {
        for (Day iDay : copierDayMap.values()) {
            if (iDay == day) {
                return true;
            }
        }
        return false;
    }

}
