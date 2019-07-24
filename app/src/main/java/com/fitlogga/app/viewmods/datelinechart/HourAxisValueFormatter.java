package com.fitlogga.app.viewmods.datelinechart;

import android.util.SparseLongArray;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class HourAxisValueFormatter extends ValueFormatter
{

    private DateFormat mDataFormat;
    private Date mDate;
    private SparseLongArray entryNumToTimestampMap;

    HourAxisValueFormatter(SparseLongArray entryNumToTimestampMap) {
        this.entryNumToTimestampMap = entryNumToTimestampMap;
        this.mDataFormat = new SimpleDateFormat("MM/dd", Locale.ENGLISH);
        this.mDate = new Date();
    }

    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     */
    @Override
    public String getFormattedValue(float value) {
        // Retrieve original timestamp
        long originalTimestamp = entryNumToTimestampMap.get((int) value);

        // Convert timestamp to MM/DD
        return getTime(originalTimestamp);
    }



    private String getTime(long timestamp){
        try{
            mDate.setTime(timestamp);
            return mDataFormat.format(mDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}
