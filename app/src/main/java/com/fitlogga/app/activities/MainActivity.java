package com.fitlogga.app.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.MainPagerAdapter;
import com.fitlogga.app.models.PremiumApp;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureTitle();
        configureTabs();
        debug();

        /*
        PlanExchanger.exportPlan("mynewplanyay", new PlanExchanger.RequestListener() {
            @Override
            public void onSuccess(String planId) {
                Log.d("planIdKey", planId);
            }

            @Override
            public void onFail() {
                Log.d("planIdKey", "failure");
            }
        });
        */

    }

    private void configureTitle() {
        TextView appTitle = findViewById(R.id.tv_app_title);
        if (PremiumApp.isEnabled()) {
            String title = getResources().getString(R.string.global_app_name_premium);
            appTitle.setText(title);
        }
        else {
            String title = getResources().getString(R.string.global_app_name);
            appTitle.setText(title);
        }
    }

    private void configureTabs() {
        MainPagerAdapter mainPagerAdapter  = new MainPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(mainPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
    }

    // private void


    private void debug() {

        /*
        PlanWriter planWriter = new PlanWriter(this);

        PlanSummary planSummary = new PlanSummary("My Fitness Plan 4", "description",
                1559601736808L);
        PlanSummary planSummary2 = new PlanSummary("My Fitness Plan 3", "description",
                1559609458836L);

        EnumMap<Day, List<Exercise>> planDetails = new EnumMap<>(Day.class);

        List<Exercise> exerciseList = new ArrayList<>();
        exerciseList.add(new RestExercise(30));

        FreeWeightExercise freeWeightExercise = new FreeWeightExercise.Builder()
                .setName("Front Squats")
                .setDescription("Do some squats bro.")
                .setNumberOfSets(3)
                .setNumberOfReps(5)
                .setAmountOfWeight(135)
                .setAmountOfWeightUnits("lb")
                .setRestTimeBetweenSets(120)
                .build();

        exerciseList.add(freeWeightExercise);

        exerciseList.add(new RestExercise(60));

        planDetails.put(Day.MONDAY, exerciseList);


        planWriter.fullWrite(planSummary, planDetails);
        planWriter.fullWrite(planSummary2, planDetails);

        */



        /*

        PlanWriter writer = new PlanWriter(getApplicationContext());

        List<Exercise> exerciseList = new ArrayList<>();

        MeterRunExercise meterRunExercise = new MeterRunExercise(50);
        TimedRunExercise timedRunExercise = new TimedRunExercise(30);

        RepetitionExercise repetitionExercise = new RepetitionExercise.Builder()
                .setName("repetition exercise")
                .setDescription("another description")
                .setNumberOfSets(4)
                .setNumberOfReps(15)
                .setRestTimeBetweenSets(60)
                .build();

        FreeWeightExercise freeWeightExercise = new FreeWeightExercise.Builder()
                .setName("free weight exercise")
                .setDescription("a description")
                .setAmountOfWeight(100)
                .setNumberOfSets(3)
                .setNumberOfReps(10)
                .setRestTimeBetweenSets(60)
                .build();

        RestExercise restExercise = new RestExercise(60);




        exerciseList.add(meterRunExercise);
        exerciseList.add(timedRunExercise);
        exerciseList.add(repetitionExercise);
        exerciseList.add(freeWeightExercise);
        exerciseList.add(restExercise);


        String jsonParsed = writer.generateExerciseListJson(exerciseList);
        Log.d("Testtt", jsonParsed);
        Log.d("Testtt", "***");
        Log.d("Testtt", "***");
        Log.d("Testtt", "***");

        List<Exercise> exerciseListDeserialized = writer.generateExerciseListFromJson(jsonParsed);
        String jsonParsedTwice = writer.generateExerciseListJson(exerciseListDeserialized);
        Log.d("Testtt", jsonParsedTwice);

        */
    }

}
