package com.fitlogga.app.adapters.plancreator;

import android.content.res.Resources;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.DayCopierExercise;
import com.fitlogga.app.models.exercises.Exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewCopyDayViewHolder extends NewExerciseViewHolder {

    private View view;
    private Day adapterDay;
    private CopierDays copierDays;
    private Resources resources;
    private List<String> availableDays;
    private SparseIntArray availableDaysIntMapper = new SparseIntArray();

    NewCopyDayViewHolder(@NonNull View itemView, Day day, CopierDays copierDays) {
        super(itemView);
        this.view = itemView;
        this.adapterDay = day;
        this.copierDays = copierDays;
        this.resources = view.getResources();
    }

    @Override
    public void manifest(Exercise exercise) {
        DayCopierExercise dayCopierExercise = (DayCopierExercise)exercise;
        Day day = dayCopierExercise.getDayBeingCopied();

        String title = resources.getString(R.string.vh_copy_day_copy_day_exercise);
        String subtitle = getSubtitle(day);

        setTitle(title);
        setSubtitle(subtitle);

        initAvailableDaysList();
        initDropdownBox(day);

    }

    private String getSubtitle(Day day) {
        if (day == null) {
            return resources.getString(R.string.vh_copy_day_copy_day_exercise);
        }
        else {
            return Day.getStringRepresentation(view.getContext(), day);
        }
    }

    private void initAvailableDaysList() {
        availableDays = new ArrayList<>(
                Arrays.asList(resources.getStringArray(R.array.global_days_of_week))
        );

        int availableDaysMaxIndex = 0;
        for (Day day : Day.values()) {
            if (copierDays.isDayCopier(day) || day == adapterDay) {
                int copiedDayIndex = getAdapterIndexOfDay(day);
                availableDays.remove(copiedDayIndex);
            }
            else {
                // it's an available day
                availableDaysIntMapper.put(availableDaysMaxIndex, day.getValue());
                availableDaysMaxIndex++;
            }
        }
    }

    private int getAdapterIndexOfDay(Day day) {
        String dayName = Day.getStringRepresentation(view.getContext(), day);
        return availableDays.indexOf(dayName);
    }

    private void initDropdownBox(Day day) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                view.getContext(), R.layout.vh_new_popup_item, availableDays);

        Spinner spinner = view.findViewById(R.id.spinner_day);
        spinner.setAdapter(adapter);

        if (day != null) {
            int dayIndex = getAdapterIndexOfDay(day);
            spinner.setSelection(dayIndex);
        }

    }

    @Override
    protected void tryToSave(SaveListener listener, String uuid) {
        Spinner spinner = view.findViewById(R.id.spinner_day);
        int selectedIndex = spinner.getSelectedItemPosition();
        int dayValue = availableDaysIntMapper.get(selectedIndex);
        Day day = Day.fromValue(dayValue);
        listener.onSave(new DayCopierExercise(day));
        copierDays.setDayAsCopier(adapterDay, day);
    }

    @Override
    protected int[] getCollapsibleViewResourceIds() {
        return new int[] {
                R.id.spinner_day,
                R.id.divider_one,
                R.id.iv_info,
                R.id.tv_description,
                R.id.divider_two,
                R.id.btn_close,
                R.id.btn_delete
        };
    }
}
