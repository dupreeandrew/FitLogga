package com.fitlogga.app.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.Event;
import com.fitlogga.app.R;
import com.fitlogga.app.adapters.drag.DefaultSimpleCallback;
import com.fitlogga.app.adapters.plancreator.NewDailyRoutineAdapter;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.viewmods.FabController;
import com.fitlogga.app.viewmods.ViewPagerPlus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.List;

import static com.fitlogga.app.models.exercises.BlankExerciseGenerator.getFreeWeight;
import static com.fitlogga.app.models.exercises.BlankExerciseGenerator.getMeterRun;
import static com.fitlogga.app.models.exercises.BlankExerciseGenerator.getRepetition;
import static com.fitlogga.app.models.exercises.BlankExerciseGenerator.getRest;
import static com.fitlogga.app.models.exercises.BlankExerciseGenerator.getTimedRun;

public class DailyRoutineCreatorFragment extends Fragment {

    private List<Exercise> exerciseList;
    private ViewPagerPlus.Controller viewPagerController;
    private ItemTouchHelper itemTouchHelper;
    private NewDailyRoutineAdapter adapter;

    public DailyRoutineCreatorFragment() {
        // Required public empty constructor
    }

    public DailyRoutineCreatorFragment(List<Exercise> exerciseList, ViewPagerPlus.Controller viewPagerController) {
        this.exerciseList = exerciseList;
        this.viewPagerController = viewPagerController;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_routine_creator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_exercise);
        fab.setOnClickListener(fabView -> promptAddExercise(view, this.adapter));
        FabController fabController = new FabController(fab);

        this.adapter = new NewDailyRoutineAdapter(exerciseList,
                viewHolder -> itemTouchHelper.startDrag(viewHolder), viewPagerController, fabController);
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
        final int REPETITION_EXERCISE = 2;
        final int FREE_WEIGHT_EXERCISE = 3;
        final int REST_BREAK = 4;

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
                        case REPETITION_EXERCISE:
                            exerciseList.add(getRepetition());
                            break;
                        case FREE_WEIGHT_EXERCISE:
                            exerciseList.add(getFreeWeight());
                            break;
                        case REST_BREAK:
                            exerciseList.add(getRest());
                            break;
                    }

                    adapter.notifyItemInserted(exerciseList.size() - 1);
                    adapter.expandViewHolder(exerciseList.size() - 1);

                })
                .show();
    }

    public void notifyFragmentFocusLost(Event event) {
        adapter.notifyFragmentFocusLost(event, getContext());
    }



}
