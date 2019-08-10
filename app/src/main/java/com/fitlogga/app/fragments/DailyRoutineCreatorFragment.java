package com.fitlogga.app.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.Event;
import com.fitlogga.app.R;
import com.fitlogga.app.adapters.drag.DefaultSimpleCallback;
import com.fitlogga.app.adapters.plancreator.CopierDays;
import com.fitlogga.app.adapters.plancreator.NewDailyRoutineAdapter;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.DayCopierExercise;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.viewmods.FabController;
import com.fitlogga.app.viewmods.ViewPagerPlus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.List;

import static com.fitlogga.app.models.exercises.BlankExerciseGenerator.getCopyDay;
import static com.fitlogga.app.models.exercises.BlankExerciseGenerator.getFreeWeight;
import static com.fitlogga.app.models.exercises.BlankExerciseGenerator.getMeterRun;
import static com.fitlogga.app.models.exercises.BlankExerciseGenerator.getRepetition;
import static com.fitlogga.app.models.exercises.BlankExerciseGenerator.getRest;
import static com.fitlogga.app.models.exercises.BlankExerciseGenerator.getTimedRun;

public class DailyRoutineCreatorFragment extends Fragment {

    private List<Exercise> exerciseList;
    private ViewPagerPlus.Controller viewPagerController;
    private Day day;
    private ItemTouchHelper itemTouchHelper;
    private NewDailyRoutineAdapter adapter;
    private CopierDays copierDays;
    private FabController fabController;

    public DailyRoutineCreatorFragment() {
        // Required public empty constructor
    }

    public DailyRoutineCreatorFragment(
            List<Exercise> exerciseList, ViewPagerPlus.Controller viewPagerController,
            Day day, CopierDays copierDays) {

        if (exerciseList == null) {
            exerciseList = new ArrayList<>();
        }


        this.exerciseList = exerciseList;
        this.viewPagerController = viewPagerController;
        this.day = day;
        this.copierDays = copierDays;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_routine_creator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_exercise);
        fab.setOnClickListener(fabView -> promptAddExercise(view, this.adapter));
        fabController = new FabController(fab);


        if (exerciseList.size() > 0 && exerciseList.get(0) instanceof DayCopierExercise) {
            fabController.setEnabled(false);
        }

        this.adapter = new NewDailyRoutineAdapter(exerciseList,
                viewHolder -> itemTouchHelper.startDrag(viewHolder), viewPagerController, fabController, day, copierDays);
        initRecyclerView(view);
    }

    private void initRecyclerView(View view) {
        RecyclerView dailyRoutineCreatorRecyclerView = view.findViewById(R.id.rv_daily_routine_creator);
        dailyRoutineCreatorRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        dailyRoutineCreatorRecyclerView.setHasFixedSize(true);
        dailyRoutineCreatorRecyclerView.setAdapter(this.adapter);
        itemTouchHelper = new ItemTouchHelper(new DefaultSimpleCallback(this.adapter));
        itemTouchHelper.attachToRecyclerView(dailyRoutineCreatorRecyclerView);
    }

    private void promptAddExercise(View view, NewDailyRoutineAdapter adapter) {
        String[] choices = view.getResources().getStringArray(R.array.daily_routine_creator_creation_options);
        final int TIMED_RUN_INDEX = 0;
        final int METER_RUN_INDEX = 1;
        final int REPETITION_EXERCISE_INDEX = 2;
        final int FREE_WEIGHT_EXERCISE_INDEX = 3;
        final int REST_BREAK_INDEX = 4;
        final int COPY_A_DAY_INDEX = 5;

        new LovelyChoiceDialog(view.getContext())
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Exercise Creation")
                .setMessage("What exercise would you like to add?")
                .setItems(choices, (position, item) -> {
                    switch (position) {
                        case TIMED_RUN_INDEX:
                            exerciseList.add(getTimedRun());
                            break;
                        case METER_RUN_INDEX:
                            exerciseList.add(getMeterRun());
                            break;
                        case REPETITION_EXERCISE_INDEX:
                            exerciseList.add(getRepetition());
                            break;
                        case FREE_WEIGHT_EXERCISE_INDEX:
                            exerciseList.add(getFreeWeight());
                            break;
                        case REST_BREAK_INDEX:
                            exerciseList.add(getRest());
                            break;
                        case COPY_A_DAY_INDEX:
                            handleCopyDay();
                            return;
                    }

                    adapter.notifyItemInserted(exerciseList.size() - 1);
                    adapter.expandViewHolder(exerciseList.size() - 1);

                })
                .show();
    }

    private void handleCopyDay() {

        if (copierDays.isDayBeingCopied(day)) {
            String error = getResources().getString(R.string.daily_routine_creator_error_copy_day);
            Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            return;
        }

        new LovelyStandardDialog(getContext())
                .setTopColorRes(R.color.colorWarning)
                .setTitle("Are you sure?")
                .setMessage("Copying another day will erase all of your exercises for today.")
                .setPositiveButton("Copy anyways", posView -> addCopyDayExercise())
                .setNegativeButton("Cancel", null)
                .show();

    }

    private void addCopyDayExercise() {

        exerciseList.clear();
        exerciseList.add(getCopyDay());

        adapter.notifyDataSetChanged();
        adapter.expandViewHolder(0);

        copierDays.setDayAsCopier(day, null);
        fabController.setEnabled(false);
    }

    public void notifyFragmentFocusLost(Event event) {
        adapter.notifyFragmentFocusLost(event, getContext());
    }



}
