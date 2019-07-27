package com.fitlogga.app.adapters.graphlog;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.fitlogga.app.fragments.GraphFragment;
import com.fitlogga.app.models.plan.log.Historics.History;

import java.util.List;

public class GraphLogDayAdapter extends FragmentPagerAdapter {

    private List<List<History>> historyLists;

    public GraphLogDayAdapter(FragmentManager fm, List<List<History>> historyLists) {
        super(fm);
        this.historyLists = historyLists;
    }

    @Override
    public Fragment getItem(int position) {
        List<History> historyList = historyLists.get(position);
        return new GraphFragment(historyList);
    }

    @Override
    public int getCount() {
        return historyLists.size();
    }
}
