package com.fitlogga.app.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.plancreator.NewExercisePagerAdapter;
import com.fitlogga.app.adapters.plancreator.NewExerciseViewHolder;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.plan.PlanExchanger;
import com.fitlogga.app.models.plan.PlanReader;
import com.fitlogga.app.models.plan.PlanSource;
import com.fitlogga.app.viewmods.ViewPagerPlus;
import com.google.android.material.tabs.TabLayout;

public class PlanCreatorActivity extends AppCompatActivity {

    public static String PREFILLED_PLAN_NAME_JSON_KEY = "preFilledPlanName";
    public static String PREFILLED_EXPORT_PLAN_PAYLOAD = "prefilledExportPlanPayload";

    private NewExercisePagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    private ViewPagerPlus viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_creator);
        configureTabs();
        enableBackButton();
    }

    private void enableBackButton() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (pagerAdapter.changesWereMade()) {
                showSaveChangesFirstDialog();
                return true;
            }
            else {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSaveChangesFirstDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("You have unsaved changes.");

        builder.setPositiveButton("SAVE CHANGES", (dialog, which) -> {
            pagerAdapter.tryToSaveCurrentViewHolder(new NewExerciseViewHolder.SaveListener() {
                @Override
                public void onSave(Exercise exercise) {
                    goToFinisherFragment();
                    dialog.dismiss();
                }

                @Override
                public void onNothingChanged() {
                    goToFinisherFragment();
                    dialog.dismiss();
                }

                @Override
                public void onFail() {

                }
            });

        });

        builder.setNegativeButton("EXIT", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void goToFinisherFragment() {
        tabLayout.setScrollPosition(NewExercisePagerAdapter.FINISH_FRAGMENT_POSITION, 0f, true);
        viewPager.setCurrentItem(NewExercisePagerAdapter.FINISH_FRAGMENT_POSITION);
    }

    private void configureTabs() {

        this.viewPager = findViewById(R.id.view_pager);
        this.tabLayout = findViewById(R.id.tab_layout);

        PlanSource planSource = getPlanSource();

        this.pagerAdapter = new NewExercisePagerAdapter(
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
