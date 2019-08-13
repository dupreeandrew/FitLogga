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
import com.fitlogga.app.models.ObservedList;
import com.fitlogga.app.models.exercises.DayCopierExercise;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseType;
import com.fitlogga.app.models.plan.PlanExchanger;
import com.fitlogga.app.models.plan.PlanSource;
import com.fitlogga.app.models.plan.PlanSummary;
import com.fitlogga.app.viewmods.ViewPagerPlus;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class NewExercisePagerAdapter extends FragmentPagerAdapter {

    public static final int FINISH_FRAGMENT_POSITION = 7;

    @Nullable
    private PlanSummary planSummary;
    @Nullable
    private PlanSource planSource;
    private ViewPagerPlus.Controller viewPagerController;
    private EnumMap<Day, List<Exercise>> initialDailyRoutineMap;
    private EnumMap<Day, List<Exercise>> dailyRoutineMap;
    private Fragment currentFragment;
    private CopierDays copierDays = new CopierDays();

    public NewExercisePagerAdapter(FragmentManager fm, @Nullable PlanSource planSource,
                                   ViewPagerPlus.Controller viewPagerController) {
        super(fm);
        this.planSource = planSource;

        this.viewPagerController = viewPagerController;

        if (planSource != null) {
            this.planSummary = planSource.getPlanSummary();
            this.dailyRoutineMap = getDailyRoutines(planSource);
            fillCopierDays();
        }
        else {
            fillDailyRoutineMap();
        }
    }

    // Basically converts the received list into an Observed List.
    private EnumMap<Day, List<Exercise>> getDailyRoutines(PlanSource planSource) {
        EnumMap<Day, List<Exercise>> dailyRoutines = planSource.getDailyRoutines();
        EnumMap<Day, List<Exercise>> newDailyRoutines = new EnumMap<>(Day.class);

        for (Map.Entry<Day, List<Exercise>> entry : dailyRoutines.entrySet()) {
            Day key = entry.getKey();
            List<Exercise> value = entry.getValue();
            ObservedList<Exercise> observedList = new ObservedList<>(value);
            newDailyRoutines.put(key, observedList);
        }

        return newDailyRoutines;
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
            dailyRoutineMap.put(day, new ObservedList<>());
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (position == FINISH_FRAGMENT_POSITION) {
            // quick and dirty :). to-do: clean up code.
            boolean importedPlan = (planSource instanceof PlanExchanger.Plan);
            return new DailyRoutineFinisherFragment(dailyRoutineMap, planSummary, importedPlan);
        }

        Day dayOfFragment = Day.fromValue(position);
        return new DailyRoutineCreatorFragment(dailyRoutineMap.get(dayOfFragment),
                viewPagerController, dayOfFragment, copierDays);
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (currentFragment != object) {
            currentFragment = (Fragment)object;
        }
        super.setPrimaryItem(container, position, object);
    }

    public boolean changesWereMade() {
        for (List<Exercise> exerciseList : dailyRoutineMap.values()) {
            ObservedList<Exercise> observedExerciseList = (ObservedList<Exercise>) exerciseList;
            if (observedExerciseList.wereChangesMade()) {
                return true;
            }
        }
        return false;
    }

    public void tryToSaveCurrentViewHolder(NewExerciseViewHolder.SaveListener listener) {
        if (currentFragment instanceof DailyRoutineCreatorFragment) {
            DailyRoutineCreatorFragment creatorFragment
                    = (DailyRoutineCreatorFragment) currentFragment;
            creatorFragment.tryToSaveAnyOpenViewHolder(listener);
        }
    }
}
