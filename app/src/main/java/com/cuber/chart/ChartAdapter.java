package com.cuber.chart;

/**
 * Created by cuber on 2015/1/10.
 */
public interface ChartAdapter {

    int getCountOfDataSet();

    int getCountOfDataPointInDataSet(int dataSet);

    double getXValueForDataPoint(int dataSet, int position);

    double getYValueForDataPoint(int dataSet, int position);
}
