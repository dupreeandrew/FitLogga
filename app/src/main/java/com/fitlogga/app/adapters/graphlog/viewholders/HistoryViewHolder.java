package com.fitlogga.app.adapters.graphlog.viewholders;


import android.annotation.SuppressLint;
import android.util.SparseLongArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.graphlog.viewholders.markers.StandardMarker;
import com.fitlogga.app.adapters.graphlog.viewholders.markers.WeightMarker;
import com.fitlogga.app.models.exercises.ExerciseType;
import com.fitlogga.app.viewmods.datelinechart.DateLineCharter;
import com.fitlogga.app.viewmods.datelinechart.DateMarkerView;
import com.github.mikephil.charting.charts.LineChart;

public class HistoryViewHolder extends RecyclerView.ViewHolder  {

    private View itemView;

    public HistoryViewHolder(@NonNull View itemView, RecyclerView recyclerView) {
        super(itemView);
        this.itemView = itemView;
        configureNestedScrolling(recyclerView);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void configureNestedScrolling(RecyclerView recyclerView) {
        LineChart lineChart = itemView.findViewById(R.id.chart_standard);
        lineChart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        recyclerView.requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        recyclerView.requestDisallowInterceptTouchEvent(false);
                        break;
                    }
                }

                return false;
            }
        });
    }

    public void setTitle(String title) {
        TextView titleView = itemView.findViewById(R.id.tv_title);
        titleView.setText(title);
    }

    public void setSubtitle(String subtitle) {
        TextView subtitleView = itemView.findViewById(R.id.tv_subtitle);
        subtitleView.setText(subtitle);
    }

    public void setGraphUnit(DateLineCharter.Unit unit, ExerciseType exerciseType) {
        LineChart lineChart = itemView.findViewById(R.id.chart_standard);

        DateMarkerView marker = getMarker(unit, exerciseType);
        marker.setChartView(lineChart);
        DateLineCharter.set(lineChart, unit, marker);
    }

    private DateMarkerView getMarker(DateLineCharter.Unit unit, ExerciseType exerciseType) {
        SparseLongArray entryNumToTimestampMap = unit.getEntryNumToTimestampMap();
        if (exerciseType == ExerciseType.FREE_WEIGHT_EXERCISE) {
            return new WeightMarker(itemView.getContext(), entryNumToTimestampMap);
        }
        else {
            return new StandardMarker(itemView.getContext(), entryNumToTimestampMap);
        }
    }

}
