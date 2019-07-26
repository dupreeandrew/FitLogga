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
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.log.SQLLogReader;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class PlanLogActivity extends AppCompatActivity {

    public static final String PLAN_NAME_KEY = "planNameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_log);
        enableBackButton();

        String planName = "hiiii";

        List<Day> nonEmptyDays = PlanReader.attachTo(planName).getNonEmptyDays();

        initViewPager(planName, nonEmptyDays);
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

    private void initViewPager(String planName, List<Day> dayList) {
        SQLLogReader reader = new SQLLogReader(planName);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new GraphLogDayAdapter(getSupportFragmentManager(), reader, dayList));
    }

    private void initTabs(List<Day> nonEmptyDays) {
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

    private void addDayNameTabs(TabLayout tabLayout, List<Day> nonEmptyDays) {
        for (Day day : nonEmptyDays) {
            TabLayout.Tab tab = tabLayout.newTab();
            String dayName = Day.getStringRepresentation(this, day);
            tab.setText(dayName);
            tabLayout.addTab(tab);
        }
    }

    private void addDayAbbrevTabs(TabLayout tabLayout, List<Day> nonEmptyDays) {
        for (Day day : nonEmptyDays) {
            TabLayout.Tab tab = tabLayout.newTab();
            String dayAbbrev = day.getDayAbbrev();
            tab.setText(dayAbbrev);
            tabLayout.addTab(tab);
        }
    }




}
