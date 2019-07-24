package com.fitlogga.app.adapters.graphlog.viewholders.markers;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.fitlogga.app.R;
import com.fitlogga.app.viewmods.datelinechart.DateMarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

public class StandardMarker extends DateMarkerView {

    public StandardMarker(Context context, long referenceTimestamp) {
        super(context, R.layout.marker_graph_standard, R.id.tv_date, referenceTimestamp);
    }

    @Override
    public void onRefreshContent(Entry e, Highlight highlight) {
        float value = e.getY();
        Log.d("yuppers", "yes");
        TextView valueView = findViewById(R.id.tv_value);
        valueView.setText("Value: " + value);
    }
}
