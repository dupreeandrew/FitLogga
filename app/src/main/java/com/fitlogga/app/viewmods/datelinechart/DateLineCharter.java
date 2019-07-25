package com.fitlogga.app.viewmods.datelinechart;

import android.content.Context;
import android.util.Log;
import android.util.SparseLongArray;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

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
import java.util.Map;

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
        private List<Map<String, Integer>> extraValueDataList;

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

        @Nullable
        public List<Map<String, Integer>> getExtraValueDataList() {
            return extraValueDataList;
        }

        public void setExtraValueData(List<Map<String, Integer>> extraValueDataList) {
            this.extraValueDataList = extraValueDataList;
        }

    }

    /**
     * Takes a line chart, and starts to draw points, using a date-based X-axis.
     */
    public static void set(LineChart lineChart, Unit unit, DateMarkerView markerView) {

        List<Long> timestamps = unit.getTimestamps();
        Data[] dataPieces = unit.getDataObjects();

        final int MAX_VISIBLE_POINTS = Math.min(timestamps.size(), 7);

        SparseLongArray entryNumToTimestampMap = unit.getEntryNumToTimestampMap();
        configureXAxis(MAX_VISIBLE_POINTS, lineChart, entryNumToTimestampMap);
        customizeLineChart(lineChart, markerView, MAX_VISIBLE_POINTS);


        LineData lineData = getLineData(dataPieces);
        lineChart.setData(lineData);
        lineChart.moveViewToX(MAX_VISIBLE_POINTS);

    }



    private static void configureXAxis(int numVisiblePoints, LineChart lineChart,
                                       SparseLongArray entryNumToTimestampMap) {
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new HourAxisValueFormatter(entryNumToTimestampMap));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(numVisiblePoints);

    }

    private static LineData getLineData(Data[] dataPieces) {
        LineData lineData = new LineData();
        int dataIterationCount = 0;
        for (Data data : dataPieces) {

            List<Integer> valueList = data.getValueList();
            Collections.reverse(valueList);

            List<Entry> entryList = new ArrayList<>();
            for (int i = 0; i < valueList.size(); i++) {
                int value = valueList.get(i);
                Entry entry = new Entry(i, value);
                entryList.add(entry);
            }

            injectAnyExtraNonNullData(entryList, data);

            String label = data.getLabel();


            LineDataSet lineDataSet = new LineDataSet(entryList, label);
            customizeLineDataSet(lineDataSet, dataIterationCount);

            lineData.addDataSet(lineDataSet);

            dataIterationCount++;

        }
        return lineData;
    }

    private static void injectAnyExtraNonNullData(List<Entry> entryList, Data data) {
        List<Map<String, Integer>> extraValueDataList = data.getExtraValueDataList();
        if (extraValueDataList != null) {
            addExtraData(entryList, extraValueDataList);
        }

    }

    private static void addExtraData(List<Entry> entryList,
                                     List<Map<String, Integer>> extraValueDataList) {

        for (int i = 0; i < entryList.size(); i++) {

            Object extraData = extraValueDataList.get(i);

            Entry entry = entryList.get(i);
            entry.setData(extraData);
        }

    }

    private static void customizeLineDataSet(LineDataSet lineDataSet,
                                             @IntRange(from = 0, to = 1) int colorSeed) {
        lineDataSet.setLineWidth(3.0f);

        Context context = ApplicationContext.getInstance();
        lineDataSet.setCircleRadius(5.0f);


        if (colorSeed == 0) {
            lineDataSet.setColors(new int[]{R.color.colorPrimaryLight}, context);
            lineDataSet.setCircleColors(new int[] {R.color.colorAccentDark}, context);
        }
        else {
            lineDataSet.setColors(new int[]{R.color.colorAccent}, context);
            lineDataSet.setCircleColors(new int[] {R.color.colorPrimary}, context);
        }

    }

    private static void customizeLineChart(LineChart lineChart, DateMarkerView markerView,
                                           int maxVisiblePoints) {
        lineChart.setMarker(markerView);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.setVisibleXRangeMaximum(maxVisiblePoints);
    }


}
