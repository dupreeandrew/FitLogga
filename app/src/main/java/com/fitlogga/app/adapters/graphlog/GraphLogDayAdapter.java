package com.fitlogga.app.adapters.graphlog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fitlogga.app.fragments.GraphFragment;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.plan.log.SQLLogReader;

import java.util.List;

public class GraphLogDayAdapter extends FragmentPagerAdapter {

    private SQLLogReader reader;
    private List<Day> dayList;

    public GraphLogDayAdapter(FragmentManager fm, SQLLogReader reader, List<Day> dayList) {
        super(fm);
        this.reader = reader;
        this.dayList = dayList;
    }

    @Override
    public Fragment getItem(int position) {
        Day day = dayList.get(position);
        return new GraphFragment(reader, day);
    }

    @Override
    public int getCount() {
        return dayList.size();
    }
}
