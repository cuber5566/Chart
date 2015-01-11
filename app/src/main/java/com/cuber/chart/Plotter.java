package com.cuber.chart;

import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.Map;

/**
 * Created by cuber on 2015/1/8.
 */
abstract public class Plotter {

    protected boolean isShowGraphic = true;
    protected boolean isShowValueOnGraphic = true;
    private Chart mChart;

    public Plotter(Chart chart) {
        this.mChart = chart;
    }

    protected Chart getGraphic() {
        return mChart;
    }

    public void showGraphic(boolean isShow) {
        this.isShowGraphic = isShow;
        mChart.invalidate();
    }

    public void showValue(boolean isShow) {
        this.isShowValueOnGraphic = isShow;
        mChart.invalidate();
    }

    abstract protected void drawDataPoint(Canvas canvas ,String value, int dataSet, int position, PointF from, PointF to, Map<String, Object> options);
}
