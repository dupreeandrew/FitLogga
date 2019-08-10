package com.fitlogga.app.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.plancreator.NewExercisePagerAdapter;
import com.fitlogga.app.models.plan.PlanExchanger;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.PlanSource;
import com.fitlogga.app.viewmods.ViewPagerPlus;
import com.google.android.material.tabs.TabLayout;

public class PlanCreatorActivity extends AppCompatActivity {

    public static String PREFILLED_PLAN_NAME_JSON_KEY = "preFilledPlanName";
    public static String PREFILLED_EXPORT_PLAN_PAYLOAD = "prefilledExportPlanPayload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_creator);
        enableBackButton();

        configureTabs();
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

    private void configureTabs() {

        ViewPagerPlus viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        PlanSource planSource = getPlanSource();


        NewExercisePagerAdapter pagerAdapter  = new NewExercisePagerAdapter(
                getSupportFragmentManager(), planSource, viewPager.getController(tabLayout));

        viewPager.setAdapter(pagerAdapter);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    private @Nullable PlanSource getPlanSource() {

        String exportPlanPayload = getIntent().getStringExtra(PREFILLED_EXPORT_PLAN_PAYLOAD);
        if (!TextUtils.isEmpty(exportPlanPayload)) {
            return PlanExchanger.convertExportPlanPayloadToPlan(exportPlanPayload);
        }

        String planName = getIntent().getStringExtra(PREFILLED_PLAN_NAME_JSON_KEY);
        if (!TextUtils.isEmpty(planName)) {
            return PlanReader.attachTo(planName);
        }

        return null;

    }
}
