package com.fitlogga.app.activities;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.training.TrainingRecyclerAdapter;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.plan.PlanReader;

import java.util.List;

public class TrainingActivity extends AppCompatActivity {

    public static final String PLAN_NAME_KEY = "planNameKey";
    public static final String DAY_NUM_KEY = "dayNumKey";
    public static final int NO_DAY_NUM_VALUE = -123456789;
    private String planNameValue;
    private Day day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        initPlanName();
        initDay();
        initRecyclerView();
    }

    private void initPlanName() {
        planNameValue = getIntent().getStringExtra(PLAN_NAME_KEY);
        if (planNameValue == null) {
            throw new IllegalStateException("A plan name must be provided.");
        }
    }

    private void initDay() {
        int dayNumValue = getIntent().getIntExtra(DAY_NUM_KEY, NO_DAY_NUM_VALUE);
        if (dayNumValue == NO_DAY_NUM_VALUE) {
            throw new IllegalStateException("A day must be provided.");
        }
        this.day = Day.fromValue(dayNumValue);
    }

    private void initRecyclerView() {
        RecyclerView exerciseRecyclerView = findViewById(R.id.rv_exercises);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        exerciseRecyclerView.setHasFixedSize(true);
        exerciseRecyclerView.setAdapter(getAdapter());
    }

    private TrainingRecyclerAdapter getAdapter() {
        PlanReader planReader = new PlanReader(this);
        List<Exercise> exerciseList = planReader.getDailyRoutine(planNameValue, day);

        ViewGroup viewGroup = findViewById(android.R.id.content);
        return new TrainingRecyclerAdapter.Builder()
                .setContext(this)
                .setViewGroup(viewGroup)
                .setPlanName(planNameValue)
                .setExerciseList(exerciseList)
                .setDay(day)
                .build();
    }
}
