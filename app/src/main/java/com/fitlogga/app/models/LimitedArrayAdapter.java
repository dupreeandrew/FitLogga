package com.fitlogga.app.models;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * This class acts as a normal ArrayAdapter but with a customizable limit.
 */
public class LimitedArrayAdapter<T> extends ArrayAdapter<T> {

    private int count;

    public LimitedArrayAdapter(Context context, int resource, T[] objects, int count) {
        super(context, resource, objects);
        this.count = count;
    }

    public LimitedArrayAdapter(Context context, int resource, List<T> objects, int count) {
        super(context, resource, objects);
        this.count = count;
    }

    @Override
    public int getCount() {
        return Math.min(count, super.getCount());
    }

}
