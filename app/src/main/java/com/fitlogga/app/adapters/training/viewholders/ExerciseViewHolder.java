package com.fitlogga.app.adapters.training.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.collapsible.CollapsibleViewHolder;
import com.fitlogga.app.models.exercises.Exercise;

public abstract class ExerciseViewHolder extends CollapsibleViewHolder {

    public interface OnUpdateListener {
        void onUpdate();
    }

    private View view;
    private OnUpdateListener listener;

    public ExerciseViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
    }

    public void manifest(Exercise exercise) {
        setCheckmarkVisible(exercise.isCompleted());
        onManifest(exercise);
    }

    abstract void onManifest(Exercise exercise);


    public final void setOnUpdateListener(OnUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * This should be called when you need to explicitly notify the recycler adapter that the
     * data set changed.
     *
     * This is good for marking exercises as completed, so that the recycler adapter can determine
     * if all of the day's exercises are completed.
     *
     * As a reminder, the nature of CollapsibleRecyclerAdapter refreshes the data every close/open.
     */
    final void refreshItemInDataSet() {
        listener.onUpdate();
    }

    public void setCheckmarkVisible(boolean b) {
        ImageView checkmarkImage = view.findViewById(R.id.iv_finish);
        checkmarkImage.setVisibility(b ? View.VISIBLE : View.INVISIBLE);
    }

    final void setTitle(String title) {
        TextView titleView = view.findViewById(R.id.tv_exercise_title);
        titleView.setText(title);
    }

    final void setSubtitle(String subtitle) {
        TextView subtitleView = view.findViewById(R.id.tv_subtitle);
        subtitleView.setText(subtitle);
    }

    //public abstract void safeCancelOperations();

}
