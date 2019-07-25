package com.fitlogga.app.adapters.graphlog.viewholders.markers;

import android.content.Context;
import android.util.SparseLongArray;
import android.widget.TextView;

import com.fitlogga.app.R;
import com.fitlogga.app.adapters.graphlog.viewholders.LineChartUnitGen;
import com.fitlogga.app.viewmods.datelinechart.DateMarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.Map;

public class WeightMarker extends DateMarkerView {

    public WeightMarker(Context context, SparseLongArray entryNumToTimestampMap) {
        super(context, R.layout.marker_graph_weight, R.id.tv_date, entryNumToTimestampMap);
    }

    @Override
    public void onRefreshContent(Entry e, Highlight highlight) {
        @SuppressWarnings("unchecked")
        Map<String, Integer> setsAndRepsMap = (Map<String, Integer>) e.getData();

        int weight = (int) e.getY();
        int sets = setsAndRepsMap.get(LineChartUnitGen.SETS_KEY);
        int reps = setsAndRepsMap.get(LineChartUnitGen.REPS_KEY);

        TextView weightView = findViewById(R.id.tv_weight);
        TextView setsRepsView = findViewById(R.id.tv_sets_reps);

        String weightText = "Weight: " + weight;
        String setsRepsText = sets + " sets & " + reps + " reps";

        weightView.setText(weightText);
        setsRepsView.setText(setsRepsText);

    }
}
