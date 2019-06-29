package com.fitlogga.app.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.plancreator.NewExercisePagerAdapter;
import com.fitlogga.app.models.plan.PlanSummary;
import com.fitlogga.app.viewmods.ViewPagerPlus;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

public class PlanCreatorActivity extends AppCompatActivity {

    public static String PREFILLED_PLAN_SUMMARY_JSON_KEY = "preFilledPlanName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_creator);
        enableBackButton();

        String planSummaryJson = getIntent().getStringExtra(PREFILLED_PLAN_SUMMARY_JSON_KEY);
        PlanSummary planSummary = new Gson().fromJson(planSummaryJson, PlanSummary.class);
        configureTabs(planSummary);
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

    private void configureTabs(PlanSummary planSummary) {

        ViewPagerPlus viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        NewExercisePagerAdapter pagerAdapter  = new NewExercisePagerAdapter(getSupportFragmentManager(),
                getApplicationContext(), planSummary, viewPager.getController(tabLayout));

        viewPager.setAdapter(pagerAdapter);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }
}
