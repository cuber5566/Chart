package com.cuber.library;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cuber on 2015/1/7.
 */
public abstract class ChartBaseAdapter implements ChartAdapter {

    public static final String DRAWING_OPTION_PLOTTER = "DRAWING_OPTION_PLOTTER";

    @Override
    public int getCountOfDataSet() {
        return 1;
    }

    public String getXLabelForXValue(double value) {
        return "" + value;
    }

    public String getYLabelForYValue(double value) {
        return "" + value;
    }

    public String getValueToString(int dataSet, double value) {
        return "" + value;
    }

    public double getMinDataX() {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < getCountOfDataSet(); i++) {
            for (int j = 0; j < getCountOfDataPointInDataSet(i); j++) {
                double v = getXValueForDataPoint(i, j);
                min = v < min ? v : min;
            }
        }
        min = min - 2 * getXMinorGridGap();
        return min - min % getXMajorGridGroup();
    }

    public double getMaxDataX() {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < getCountOfDataSet(); i++) {
            for (int j = 0; j < getCountOfDataPointInDataSet(i); j++) {
                double v = getXValueForDataPoint(i, j);
                max = v > max ? v : max;
            }
        }
        return max + 2 * getXMinorGridGap();
    }

    public double getMinDataY() {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < getCountOfDataSet(); i++) {
            for (int j = 0; j < getCountOfDataPointInDataSet(i); j++) {
                double v = getYValueForDataPoint(i, j);
                min = v < min ? v : min;
            }
        }
        min = min - getYRange() * 0.2f;
        return min - min % getYMajorGridGroup();
    }

    public double getMaxDataY() {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < getCountOfDataSet(); i++) {
            for (int j = 0; j < getCountOfDataPointInDataSet(i); j++) {
                double v = getYValueForDataPoint(i, j);
                max = v > max ? v : max;
            }
        }
        return max + getYRange() * 0.2f;
    }

    private double getYRange() {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < getCountOfDataSet(); i++) {
            for (int j = 0; j < getCountOfDataPointInDataSet(i); j++) {
                double v = getYValueForDataPoint(i, j);
                min = v < min ? v : min;
                max = v > max ? v : max;
            }
        }
        return max - min;
    }

    public long getXMajorGridGroup() {
        return 5;
    }

    public long getXMinorGridGap() {
        return 1;
    }

    /* number of minor grid per major grid */
    public long getYMajorGridGroup() {
        return 5;
    }

    /* delta between a minor grid (in data space) */
    public long getYMinorGridGap() {
        return 1;
    }

    public Map<String, Object> getDrawingOptionsForDataSet(int index) {
        return new HashMap<>();
    }
}