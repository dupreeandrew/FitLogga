package com.fitlogga.app.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.plancreator.NewExercisePagerAdapter;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.viewmods.ViewPagerPlus;
import com.google.android.material.tabs.TabLayout;

public class PlanCreatorActivity extends AppCompatActivity {

    public static String PREFILLED_PLAN_NAME_JSON_KEY = "preFilledPlanName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_creator);
        enableBackButton();

        String planName = getIntent().getStringExtra(PREFILLED_PLAN_NAME_JSON_KEY);
        configureTabs(planName);
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

    private void configureTabs(String planName) {

        ViewPagerPlus viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        PlanReader planReader = PlanReader.attachTo(planName);
        NewExercisePagerAdapter pagerAdapter  = new NewExercisePagerAdapter(
                getSupportFragmentManager(), planReader, viewPager.getController(tabLayout));

        viewPager.setAdapter(pagerAdapter);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }
}
