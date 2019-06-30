package com.fitlogga.app.adapters.plancreator;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fitlogga.app.Event;
import com.fitlogga.app.fragments.DailyRoutineCreatorFragment;
import com.fitlogga.app.fragments.DailyRoutineFinisherFragment;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.PlanSummary;
import com.fitlogga.app.viewmods.ViewPagerPlus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class NewExercisePagerAdapter extends FragmentPagerAdapter {

    private @Nullable PlanSummary planSummary;
    private ViewPagerPlus.Controller viewPagerController;
    private EnumMap<Day, List<Exercise>> dailyRoutineMap;
    private Fragment currentFragment;

    public NewExercisePagerAdapter(FragmentManager fm, Context context, @Nullable PlanSummary planSummary,
                                   ViewPagerPlus.Controller viewPagerController) {
        super(fm);

        this.planSummary = planSummary;
        this.viewPagerController = viewPagerController;

        PlanReader planReader = new PlanReader(context);

        if (planSummary != null) {
            dailyRoutineMap = planReader.getDailyRoutines(planSummary.getName());
        }
        else {
            fillDailyRoutineMap();
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
        return new DailyRoutineCreatorFragment(dailyRoutineMap.get(dayOfFragment), viewPagerController);
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

    private boolean permitFragmentChange() {
        if (!(currentFragment instanceof DailyRoutineCreatorFragment)) {
            return true;
        }

        Event event = new Event(false);
        DailyRoutineCreatorFragment fragmentToNotify = (DailyRoutineCreatorFragment)currentFragment;
        fragmentToNotify.notifyFragmentFocusLost(event);

        return !event.isCancelled();


    }
}