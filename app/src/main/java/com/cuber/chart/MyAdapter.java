package com.cuber.chart;

import com.cuber.library.Chart;
import com.cuber.library.ChartBaseAdapter;
import com.cuber.library.Plotter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cuber on 2015/1/9.
 */
public class MyAdapter extends ChartBaseAdapter {

    private List<Temperature> list1, list2, list3, list4;
    private Chart mChart;
    private List<Plotter> mPlotters;

    public MyAdapter(Chart chart, List<Plotter> plotters) {
        this.mChart = chart;
        this.mPlotters = plotters;
        setData1();
        setData2();
        setData3();
        setData4();
    }

    @Override
    public int getCountOfDataSet() {
        return 4;
    }

    @Override
    public int getCountOfDataPointInDataSet(int dataSet) {

        if (dataSet == 0) {
            return list1.size();
        }
        //
        else if (dataSet == 1) {
            return list2.size();
        }
        //
        else if (dataSet == 2) {
            return list3.size();
        }
        //
        else  {
            return list4.size();
        }
    }

    @Override
    public double getXValueForDataPoint(int dataSet, int position) {

        if (dataSet == 0) {
            return list1.get(position).getTime();
        }
        //
        else if (dataSet == 1) {
            return list2.get(position).getTime();
        }
        //
        else if (dataSet == 2) {
            return list3.get(position).getTime();
        }
        //
        else  {
            return list4.get(position).getTime();
        }
    }

    @Override
    public double getYValueForDataPoint(int dataSet, int position) {

        if (dataSet == 0) {
            return list1.get(position).getValue();
        }
        //
        else if (dataSet == 1) {
            return list2.get(position).getValue();
        }
        //
        else if (dataSet == 2) {
            return list3.get(position).getValue();
        }
        //
        else  {
            return list4.get(position).getValue();
        }
    }

    @Override
    public String getXLabelForXValue(double value) {
        //return new SimpleDateFormat("HH:mm").format(value);
        return String.format("%.0f", value);
    }

    @Override
    public String getYLabelForYValue(double value) {
        return String.format("%.0f", value);
    }

    @Override
    public String getValueToString(int dataSet, double value) {
        return String.format("%.2f", value);
    }

    @Override
    public long getXMinorGridGap() {
        return 1;
    }

    @Override
    public long getYMinorGridGap() {
        return 1;
    }

    @Override
    public long getXMajorGridGroup() {
        return 5;
    }

    @Override
    public long getYMajorGridGroup() {
        return 5;
    }


    @Override
    public Map<String, Object> getDrawingOptionsForDataSet(final int position) {

        HashMap<String, Object> options = new HashMap<>();
        options.put(DRAWING_OPTION_PLOTTER, mPlotters.get(position));
        return options;
    }

    /**
     * ===== Data =====
     */
    public void setData1() {
        list1 = new ArrayList<>();

        for (int i = 7; i < 20; i++) {
            double y = Math.random() * 20 + 10;
            Temperature t = new Temperature(i, y);
            list1.add(t);
        }
//        for (int i=0; i<20000; i++) {
//            double theta = ((i / 100.) * 2 * Math.PI);
//            double y = Math.cos(theta) * 100;
//            Temperature t = new Temperature(i, y /*Math.random() * 100 + 50 */);
////            Log.e("yc", "x " + theta + " " + y + " " + i);
//            list1.add(t);
//        }
    }

    public void setData2() {
        list2 = new ArrayList<>();

        for (int i = 11; i < 22; i++) {
            double y = Math.random() * 20 + 0;
            Temperature t = new Temperature(i, y);
            list2.add(t);
        }
//        long now = System.currentTimeMillis();
//        for (int i = 0; i < 20000; i++) {
//            double theta = ((i / 50.) * 2 * Math.PI);
//            double y = Math.cos(theta) * 40;
//            Temperature t = new Temperature(i, y /*Math.random() * 100 + 50 */);
//            //   Log.e("yc", "x " + theta + " " + y + " " + i);
//            list2.add(t);
//        }
    }

    public void setData3() {
        list3 = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            double y = Math.random() * 20 + 10;
            Temperature t = new Temperature(i, y);
            list3.add(t);
        }

//        long now = System.currentTimeMillis();
//        for (int i = 0; i < 20000; i++) {
//            double theta = ((i / 50.) * 2 * Math.PI);
//            double y = Math.cos(theta) * 40;
//            Temperature t = new Temperature(i, y /*Math.random() * 100 + 50 */);
//            //   Log.e("yc", "x " + theta + " " + y + " " + i);
//            list2.add(t);
//        }
    }

    public void setData4() {
        list4 = new ArrayList<>();

        for (int i = 0; i < 22; i++) {
            double y = Math.random() * 20 + 10;
            Temperature t = new Temperature(i, y);
            list4.add(t);
        }
    }

    //class
    public static class Temperature {

        private double value;
        private long time;

        public Temperature(long time, double value) {
            this.time = time;
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public long getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
}
