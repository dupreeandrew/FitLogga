package com.fitlogga.app.viewmods.datelinechart;

import android.content.Context;
import android.util.Log;
import android.util.SparseLongArray;

import com.fitlogga.app.R;
import com.fitlogga.app.models.ApplicationContext;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DateLineCharter {

    public static class Unit {

        private final List<Long> timestamps;
        private final Data[] dataObjects;
        private final SparseLongArray entryNumToTimestampMap;

        /**
         * @param timestamps Timestamps representing the data objects
         */
        public Unit(List<Long> timestamps, Data... dataObjects) {
            this.timestamps = timestamps;
            this.dataObjects = dataObjects;

            verifyCorrectSize(dataObjects);

            Collections.reverse(this.timestamps);
            this.entryNumToTimestampMap = getEntryNumToTimestampMap(timestamps);

        }

        private static SparseLongArray getEntryNumToTimestampMap(List<Long> timestamps) {
            SparseLongArray sparseLongArray = new SparseLongArray();
            for (int i = 0; i < timestamps.size(); i++) {
                long timestamp = timestamps.get(i);
                sparseLongArray.append(i, timestamp);
            }
            return sparseLongArray;
        }

        private void verifyCorrectSize(Data[] dataObjects) {
            int timestampSize = timestamps.size();
            List<Integer> valueListsSizes = getValueListsSizes(dataObjects);
            for (int size : valueListsSizes) {
                if (timestampSize != size) {
                    throw new IndexOutOfBoundsException("Value List size != timestamp size");
                }
            }
            Log.d("testt1", "Pass!");
        }

        private List<Integer> getValueListsSizes(Data[] dataObjects) {
            List<Integer> sizes = new ArrayList<>();
            for (Data data : dataObjects) {
                int size = data.getValueList().size();
                sizes.add(size);
            }
            return sizes;
        }

        public List<Long> getTimestamps() {
            return timestamps;
        }

        public Data[] getDataObjects() {
            return dataObjects;
        }

        public SparseLongArray getEntryNumToTimestampMap() {
            return entryNumToTimestampMap;
        }
    }

    public static class Data {

        private final String label;
        private final List<Integer> valueList;

        public Data(String label, List<Integer> valueList) {
            this.label = label;
            this.valueList = valueList;
        }

        public String getLabel() {
            return label;
        }

        public List<Integer> getValueList() {
            return valueList;
        }
    }

    /**
     * Takes a line chart, and starts to draw points, using a date-based X-axis.
     *
     */
    public static void set(LineChart lineChart, Unit unit, DateMarkerView markerView) {



        List<Long> timestamps = unit.getTimestamps();
        Data[] dataPieces = unit.getDataObjects();

        final int MAX_VISIBLE_POINTS = Math.min(timestamps.size(), 7);

        SparseLongArray entryNumToTimestampMap = unit.getEntryNumToTimestampMap();
        configureXAxis(MAX_VISIBLE_POINTS, lineChart, entryNumToTimestampMap);

        LineData lineData = new LineData();
        for (Data data : dataPieces) {
            List<Entry> entryList = new ArrayList<>();

            List<Integer> valueList = data.getValueList();
            Collections.reverse(valueList);

            for (int i = 0; i < valueList.size(); i++) {
                int value = valueList.get(i);
                Entry entry = new Entry(i, value);
                entryList.add(entry);
            }

            String label = data.getLabel();


            LineDataSet lineDataSet = new LineDataSet(entryList, label);
            customizeLineDataSet(lineDataSet);
            lineData.addDataSet(lineDataSet);


        }

        lineChart.setData(lineData);
        lineChart.setMarker(markerView);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.setVisibleXRangeMaximum(MAX_VISIBLE_POINTS);

        lineChart.moveViewToX(MAX_VISIBLE_POINTS);


    }

    private static void configureXAxis(int numVisiblePoints, LineChart lineChart,
                                       SparseLongArray entryNumToTimestampMap) {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new HourAxisValueFormatter(entryNumToTimestampMap));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(numVisiblePoints);

    }

    private static void customizeLineDataSet(LineDataSet lineDataSet) {
        lineDataSet.setLineWidth(3.0f);

        Context context = ApplicationContext.getInstance();
        lineDataSet.setColors(new int[]{R.color.colorPrimaryLight}, context);
        lineDataSet.setCircleRadius(5.0f);
        lineDataSet.setCircleColors(new int[] {R.color.colorAccentDark}, context);

    }


}
