package com.fitlogga.app.adapters.graphlog.viewholders;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.graphlog.viewholders.markers.StandardMarker;
import com.fitlogga.app.viewmods.datelinechart.DateLineCharter;
import com.github.mikephil.charting.charts.LineChart;

public class HistoryViewHolder extends RecyclerView.ViewHolder  {

    private View itemView;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void setTitle(String title) {
        TextView titleView = itemView.findViewById(R.id.tv_title);
        titleView.setText(title);
    }

    public void setGraphUnit(DateLineCharter.Unit unit) {
        LineChart lineChart = itemView.findViewById(R.id.chart_standard);
        StandardMarker marker = new StandardMarker(itemView.getContext(),
                unit.getEntryNumToTimestampMap());
        marker.setChartView(lineChart);
        DateLineCharter.set(lineChart, unit, marker);
    }

}
