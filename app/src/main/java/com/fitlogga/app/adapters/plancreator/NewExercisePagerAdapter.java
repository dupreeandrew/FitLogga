package com.fitlogga.app.adapters.plancreator;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fitlogga.app.fragments.DailyRoutineCreatorFragment;
import com.fitlogga.app.fragments.DailyRoutineFinisherFragment;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.DayCopierExercise;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseType;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.PlanSummary;
import com.fitlogga.app.viewmods.ViewPagerPlus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class NewExercisePagerAdapter extends FragmentPagerAdapter {

    @Nullable
    private PlanSummary planSummary;
    private ViewPagerPlus.Controller viewPagerController;
    private EnumMap<Day, List<Exercise>> dailyRoutineMap;
    private Fragment currentFragment;
    private CopierDays copierDays = new CopierDays();

    public NewExercisePagerAdapter(FragmentManager fm, @Nullable PlanReader planReader,
                                   ViewPagerPlus.Controller viewPagerController) {
        super(fm);

        this.viewPagerController = viewPagerController;

        if (planReader != null) {
            this.planSummary = planReader.getPlanSummary();
            this.dailyRoutineMap = planReader.getDailyRoutines();
            fillCopierDays();
        }
        else {
            fillDailyRoutineMap();
        }
    }

    private void fillCopierDays() {
        for (Map.Entry<Day, List<Exercise>> entry : dailyRoutineMap.entrySet()) {
            Day day = entry.getKey();
            List<Exercise> exerciseList = entry.getValue();

            for (Exercise exercise : exerciseList) {
                if (exercise.getExerciseType() == ExerciseType.COPIER) {
                    DayCopierExercise dayCopierExercise = (DayCopierExercise) exercise;
                    Day dayBeingCopied = dayCopierExercise.getDayBeingCopied();
                    copierDays.setDayAsCopier(day, dayBeingCopied);
                }
            }
        }
    }

    private void fillDailyRoutineMap() {
        dailyRoutineMap = new EnumMap<>(Day.class);
        for (Day day : Day.values()) {
            dailyRoutineMap.put(day, new ArrayList<>());
        }
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 7) {
            return new DailyRoutineFinisherFragment(dailyRoutineMap, planSummary);
        }

        Day dayOfFragment = Day.fromValue(position);
        return new DailyRoutineCreatorFragment(dailyRoutineMap.get(dayOfFragment),
                viewPagerController, dayOfFragment, copierDays);
    }

    @Override
    public int getCount() {
        return 8;
    }


    /*
    Before, the activity will ask the view holder to permit the fragment change
    Now, view holder has to explicitly tell.
     */

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (currentFragment != object) {
            currentFragment = (Fragment)object;
        }
        super.setPrimaryItem(container, position, object);
    }

}
