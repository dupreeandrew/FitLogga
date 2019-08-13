package com.fitlogga.app.adapters.plancreator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.Event;
import com.fitlogga.app.R;
import com.fitlogga.app.adapters.collapsible.CollapsibleRecyclerAdapter;
import com.fitlogga.app.adapters.drag.DragListener;
import com.fitlogga.app.adapters.drag.ItemTouchAdapter;
import com.fitlogga.app.models.Day;
import com.fitlogga.app.models.exercises.DayCopierExercise;
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
    private Day day;
    private CopierDays copierDays;
    private RecyclerView recyclerView;

    public NewDailyRoutineAdapter(List<Exercise> exerciseList, DragListener listener,
                                  ViewPagerPlus.Controller viewPagerController,
                                  FabController fabController, Day day, CopierDays copierDays) {
        this.exerciseList = exerciseList;
        this.dragListener = listener;
        this.viewPagerController = viewPagerController;
        this.fabController = fabController;
        this.day = day;
        this.copierDays = copierDays;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
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
            case COPIER:
                viewHolderLayout = inflater.inflate(R.layout.vh_new_copy_day, parent, false);
                return new NewCopyDayViewHolder(viewHolderLayout, day, copierDays);
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

        viewHolder.setCloseButtonClickListener(view ->
                viewHolder.simulateCollapseButtonClick());

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

        collapsePresentExpandedViewHolder();

        Exercise exercise = exerciseList.get(adapterPosition);
        if (exercise instanceof DayCopierExercise) {
            fabController.setEnabled(true);
            copierDays.setDayAsCopier(day, null);
        }

        //SQLLogWriter.delete(exercise.getUuid());
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
        int adapterPosition = viewHolder.getAdapterPosition();
        viewHolder.tryToSave(new NewExerciseViewHolder.SaveListener() {
            @Override
            public void onSave(Exercise savedExercise) {
                exerciseList.set(adapterPosition, savedExercise);
                lockViewHolderFocus(false);

                // Removes any focus/keyboards
                viewHolder.removeAnyFocuses();

            }

            @Override
            public void onNothingChanged() {
                lockViewHolderFocus(false);
                viewHolder.removeAnyFocuses();
            }

            @Override
            public void onFail() {
                event.setCancelled(true);
                String errorMessage =  context.getString(R.string.please_finish_making_changes_first);
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void lockViewHolderFocus(boolean b) {
        viewPagerController.setPagingEnabled(!b);

        boolean firstExerciseIsCopier =
                exerciseList.size() > 0 && exerciseList.get(0) instanceof DayCopierExercise;
        if (firstExerciseIsCopier) {
            fabController.setEnabled(false);
        }
        else {
            fabController.setEnabled(!b);
        }
    }

    @Override
    protected void onViewHolderExpanded(int adapterPos) {
        fabController.setEnabled(false);
        viewPagerController.setPagingEnabled(false);
        lockViewHolderFocus(true);
        recyclerView.scrollToPosition(adapterPos);
    }

    public void tryToSaveCurrentViewHolder(NewExerciseViewHolder.SaveListener saveListener) {

        if (getExpandedViewHolder() == null) {
            saveListener.onNothingChanged();
            return;
        }

        getExpandedViewHolder().tryToSave(saveListener);


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
