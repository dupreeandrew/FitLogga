package com.fitlogga.app.adapters.plancreator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fitlogga.app.Event;
import com.fitlogga.app.R;
import com.fitlogga.app.adapters.collapsible.CollapsibleRecyclerAdapter;
import com.fitlogga.app.adapters.drag.DragListener;
import com.fitlogga.app.adapters.drag.ItemTouchAdapter;
import com.fitlogga.app.models.exercises.Exercise;
import com.fitlogga.app.models.exercises.ExerciseType;
import com.fitlogga.app.viewmods.FabController;
import com.fitlogga.app.viewmods.ViewPagerPlus;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.Collections;
import java.util.List;

public class NewDailyRoutineAdapter extends CollapsibleRecyclerAdapter<NewExerciseViewHolder>
        implements ItemTouchAdapter {

    private List<Exercise> exerciseList;
    private DragListener dragListener;
    private ViewPagerPlus.Controller viewPagerController;
    private FabController fabController;
    private Event lastEvent;

    public NewDailyRoutineAdapter(List<Exercise> exerciseList, DragListener listener,
                                  ViewPagerPlus.Controller viewPagerController,
                                  FabController fabController) {
        this.exerciseList = exerciseList;
        this.dragListener = listener;
        this.viewPagerController = viewPagerController;
        this.fabController = fabController;
    }

    @Override
    public int getItemViewType(int position) {
        ExerciseType exerciseType = exerciseList.get(position).getExerciseType();
        return exerciseType.getExerciseTypeValue();
    }

    @NonNull
    @Override
    public NewExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ExerciseType exerciseType = ExerciseType.fromInteger(viewType);

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewHolderLayout;
        switch (exerciseType) {
            case TIMED_RUN:
                viewHolderLayout = inflater.inflate(R.layout.vh_new_timed_run, parent, false);
                return new NewTimedRunViewHolder(viewHolderLayout);
            case METER_RUN:
                viewHolderLayout = inflater.inflate(R.layout.vh_new_meter_run, parent, false);
                return new NewMeterRunViewHolder(viewHolderLayout);
            case REPETITION_EXERCISE:
                viewHolderLayout = inflater.inflate(R.layout.vh_new_repetition, parent, false);
                return new NewRepetitionViewHolder(viewHolderLayout);
            case FREE_WEIGHT_EXERCISE:
                viewHolderLayout = inflater.inflate(R.layout.vh_new_free_weight, parent, false);
                return new NewFreeWeightViewHolder(viewHolderLayout);
            case REST:
                viewHolderLayout = inflater.inflate(R.layout.vh_new_rest, parent, false);
                return new NewRestViewHolder(viewHolderLayout);
            default:
                throw new IllegalArgumentException();
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onPostConfigBindViewHolder(NewExerciseViewHolder viewHolder, int position) {
        Exercise exercise = exerciseList.get(position);
        viewHolder.manifest(exercise);

        viewHolder.resetRequiredErrors();

        viewHolder.setDragHandleTouchListener((view, motionEvent) -> {

            if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                dragListener.onDragStart(viewHolder);
            }

            return false;
        });

        viewHolder.setDeleteButtonClickListener(view ->
                promptDelete(view, viewHolder.getAdapterPosition()));

    }

    private void promptDelete(View view, int adapterPosition) {
        new LovelyStandardDialog(view.getContext())
                .setTopColorRes(R.color.colorWarning)
                .setTitle("Are you sure?")
                .setMessage("Are you sure you want to delete this exercise?")
                .setPositiveButton("Delete", posView -> delete(adapterPosition))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void delete(int adapterPosition) {
        exerciseList.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        notifyItemRangeChanged(adapterPosition, exerciseList.size());
        lockViewHolderFocus(false);
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    @Override
    protected void onViewHolderCollapsed(Event event, NewExerciseViewHolder viewHolder, Context context) {
        viewHolder.tryToSave(new NewExerciseViewHolder.SaveListener() {
            @Override
            public void onSave(Exercise savedExercise) {
                int adapterPosition = viewHolder.getAdapterPosition();
                exerciseList.set(adapterPosition, savedExercise);
                lockViewHolderFocus(false);
            }

            @Override
            public void onFail() {
                Log.d("heh", event.toString());
                event.setCancelled(true);
                String errorMessage =  context.getString(R.string.please_finish_making_changes_first);
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        this.lastEvent = event;




    }

    private void lockViewHolderFocus(boolean b) {
        viewPagerController.setPagingEnabled(!b);
        fabController.setEnabled(!b);
    }

    @Override
    protected void onViewHolderExpanded(NewExerciseViewHolder expandedViewHolder) {
        fabController.setEnabled(false);
        viewPagerController.setPagingEnabled(false);
        lockViewHolderFocus(true);
    }

    public void notifyFragmentFocusLost(Event event, Context context) {

        String tag = "sup";

        Log.d(tag, "NOTIFYING..");

        NewExerciseViewHolder expandedViewHolder = getExpandedViewHolder();

        if (expandedViewHolder == null) {
            Log.d(tag, "NOTHING IS EXPANDED.");
            return;
        }

        onViewHolderCollapsed(event, expandedViewHolder, context);
        Log.d(tag, "NOTIFIED SUCCESS.");


    }

    @Override
    public void onMove(int sourcePos, int targetPos) {
        Collections.swap(exerciseList, sourcePos, targetPos);
        notifyItemMoved(sourcePos, targetPos);
        considerDataSetSwapped(sourcePos, targetPos);
    }


    @Override
    public void onSwipe() {
        // do nothing
    }

    @Override
    public void onDragFinished(int sourcePos, int targetPos) {
        considerDataSetMoved(targetPos, exerciseList.size());
    }
}
