package com.fitlogga.app.adapters.training;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.fitlogga.app.Event;
import com.fitlogga.app.R;
import com.fitlogga.app.adapters.collapsible.CollapsibleRecyclerAdapter;
import com.fitlogga.app.adapters.training.viewholders.ExerciseViewHolder;
import com.fitlogga.app.adapters.training.viewholders.FreeWeightViewHolder;
import com.fitlogga.app.adapters.training.viewholders.MeterRunViewHolder;
import com.fitlogga.app.adapters.training.viewholders.RepetitionViewHolder;
import com.fitlogga.app.adapters.training.viewholders.RestViewHolder;
import com.fitlogga.app.adapters.training.viewholders.TimedRunViewHolder;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseType;
import com.fitlogga.app.models.plan.PlanEditor;
import com.fitlogga.app.models.plan.log.SQLLogWriter;

import java.util.List;


public class TrainingRecyclerAdapter extends CollapsibleRecyclerAdapter<ExerciseViewHolder> {

    public static class Builder {
        private List<Exercise> exerciseList;
        private Context context;
        private ViewGroup viewGroup;
        private String planName;
        private Day day;

        public Builder setExerciseList(List<Exercise> exerciseList) {
            this.exerciseList = exerciseList;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setViewGroup(ViewGroup viewGroup) {
            this.viewGroup = viewGroup;
            return this;
        }

        public Builder setPlanName(String planName) {
            this.planName = planName;
            return this;
        }

        public Builder setDay(Day day) {
            this.day = day;
            return this;
        }

        public TrainingRecyclerAdapter build() {
            checkForNull();
            return new TrainingRecyclerAdapter(planName, exerciseList, day, context, viewGroup);
        }

        private void checkForNull() {
            if (exerciseList == null) {
                throw new NullPointerException("exercise list is null.");
            }

            if (context == null) {
                throw new NullPointerException("context is null.");
            }

            if (viewGroup == null) {
                throw new NullPointerException("view group is null.");
            }

            if (planName == null) {
                throw new NullPointerException("Plan name is null.");
            }

        }
    }


    private List<Exercise> exerciseList;
    private Context context;
    private ViewGroup viewGroup;
    private String planName;
    private Day day;
    private boolean alreadyCompleted = false;

    private TrainingRecyclerAdapter(String planName, List<Exercise> exerciseList, Day day, Context applicationContext, ViewGroup viewGroup) {
        this.planName = planName;
        this.exerciseList = exerciseList;
        this.context = applicationContext;
        this.viewGroup = viewGroup;
        this.day = day;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExerciseType exerciseType = ExerciseType.fromInteger(viewType);

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewHolderLayout;
        switch (exerciseType) {
            case TIMED_RUN:
                viewHolderLayout = inflater.inflate(R.layout.vh_timed_run, parent, false);
                return new TimedRunViewHolder(viewHolderLayout);
            case METER_RUN:
                viewHolderLayout = inflater.inflate(R.layout.vh_meter_run, parent, false);
                return new MeterRunViewHolder(viewHolderLayout);
            case REPETITION_EXERCISE:
                viewHolderLayout = inflater.inflate(R.layout.vh_repetition, parent, false);
                return new RepetitionViewHolder(viewHolderLayout);
            case FREE_WEIGHT_EXERCISE:
                viewHolderLayout = inflater.inflate(R.layout.vh_free_weight_new, parent, false);
                return new FreeWeightViewHolder(viewHolderLayout);
            case REST:
                viewHolderLayout = inflater.inflate(R.layout.vh_rest, parent, false);
                return new RestViewHolder(viewHolderLayout);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getItemViewType(int position) {
        ExerciseType exerciseType = exerciseList.get(position).getExerciseType();
        return exerciseType.getExerciseTypeValue();
    }

    @Override
    protected void onPostConfigBindViewHolder(ExerciseViewHolder viewHolder, int position) {
        Exercise exercise = exerciseList.get(position);
        viewHolder.manifest(exercise);
        viewHolder.setOnUpdateListener(() -> {
            viewHolder.setCheckmarkVisible(true);
            checkForDailyRoutineFinish();
        });
    }

    private void checkForDailyRoutineFinish() {

        if (alreadyCompleted) {
            return;
        }

        for (Exercise exercise : exerciseList) {
            if (!exercise.isCompleted()) {
                return;
            }
        }

        setupDailyRoutineFinish();

    }

    private void setupDailyRoutineFinish() {
        alreadyCompleted = true;
        sendDailyRoutineFinishAlert();
        updateDailyRoutineToStorage();
        logAllExercises();
    }

    private void sendDailyRoutineFinishAlert() {

        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_complete, viewGroup, false);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .show();

        Button dismissButton = dialogView.findViewById(R.id.btn_ok);
        dismissButton.setOnClickListener(view -> alertDialog.dismiss());

    }

    private void updateDailyRoutineToStorage() {
        PlanEditor planEditor = new PlanEditor(context, planName);
        planEditor.updateDailyRoutine(day, exerciseList);
    }

    private void logAllExercises() {
        SQLLogWriter writer = new SQLLogWriter(planName);
        writer.append(exerciseList, day);
    }

    @Override
    protected void onViewHolderCollapsed(Event event, ExerciseViewHolder viewHolder, Context context) {
        //viewHolder.safeCancelOperations();
    }

    @Override
    protected void onViewHolderExpanded(int adapterPos) {

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }
}
