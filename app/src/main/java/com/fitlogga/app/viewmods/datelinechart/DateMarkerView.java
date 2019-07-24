
package com.fitlogga.app.viewmods.datelinechart;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.IdRes;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class DateMarkerView extends MarkerView {

    private TextView tvContent;
    private long referenceTimestamp;  // minimum timestamp in your data set
    private DateFormat mDataFormat;
    private Date mDate;

    public DateMarkerView(Context context, int layoutResource,
                          @IdRes int dateTextViewId, long referenceTimestamp) {
        super(context, layoutResource);
        tvContent = findViewById(dateTextViewId);
        this.referenceTimestamp = referenceTimestamp;
        this.mDataFormat = new SimpleDateFormat("MM/dd/yy @ hh:mm a", Locale.ENGLISH);
        this.mDate = new Date();
    }

    @Override
    public final void refreshContent(Entry e, Highlight highlight) {
        updateDateTextView(e);
        onRefreshContent(e, highlight);
        super.refreshContent(e, highlight);
    }

    private final void updateDateTextView(Entry e){
        long timestamp = referenceTimestamp - (int)e.getX();

        mDate.setTime(timestamp);
        String date = mDataFormat.format(mDate);
        tvContent.setText(date);

    }

    public abstract void onRefreshContent(Entry e, Highlight highlight);


    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }


}
