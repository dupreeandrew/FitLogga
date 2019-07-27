package com.fitlogga.app.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.graphlog.GraphLogDayAdapter;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.plan.log.Historics.History;
import com.fitlogga.app.models.plan.log.SQLLogReader;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

public class PlanLogActivity extends AppCompatActivity {

    public static final String PLAN_NAME_KEY = "planNameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_log);
        enableBackButton();

        String planName = getIntent().getStringExtra(PLAN_NAME_KEY);

        EnumMap<Day, List<History>> dayListEnumMap = getMapOfLoggedDays(planName);
        Set<Day> nonEmptyDays = dayListEnumMap.keySet();
        initViewPager(dayListEnumMap);
        initTabs(nonEmptyDays);

    }


    private void enableBackButton() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private EnumMap<Day, List<History>> getMapOfLoggedDays(String planName) {
        EnumMap<Day, List<History>> dayListEnumMap = new EnumMap<>(Day.class);
        SQLLogReader reader = new SQLLogReader(planName);
        for (Day day : Day.values()) {
            List<History> historyList = reader.getHistoryList(day, 250);
            if (historyList.size() != 0) {
                dayListEnumMap.put(day, historyList);
            }
        }
        return dayListEnumMap;
    }

    private void initViewPager(EnumMap<Day, List<History>> dayListEnumMap) {
        List<List<History>> historyLists = new ArrayList<>(dayListEnumMap.values());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new GraphLogDayAdapter(getSupportFragmentManager(), historyLists));
    }

    private void initTabs(Set<Day> nonEmptyDays) {
        int nonEmptyDaysSize = nonEmptyDays.size();
        TabLayout tabLayout = findViewById(R.id.tab_layout_days_of_logging);
        if (nonEmptyDaysSize <= 3) {
            addDayNameTabs(tabLayout, nonEmptyDays);
        }
        else {
            addDayAbbrevTabs(tabLayout, nonEmptyDays);
        }

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }

    private void addDayNameTabs(TabLayout tabLayout, Set<Day> nonEmptyDays) {
        for (Day day : nonEmptyDays) {
            TabLayout.Tab tab = tabLayout.newTab();
            String dayName = Day.getStringRepresentation(this, day);
            tab.setText(dayName);
            tabLayout.addTab(tab);
        }
    }

    private void addDayAbbrevTabs(TabLayout tabLayout, Set<Day> nonEmptyDays) {
        for (Day day : nonEmptyDays) {
            TabLayout.Tab tab = tabLayout.newTab();
            String dayAbbrev = day.getDayAbbrev();
            tab.setText(dayAbbrev);
            tabLayout.addTab(tab);
        }
    }




}
