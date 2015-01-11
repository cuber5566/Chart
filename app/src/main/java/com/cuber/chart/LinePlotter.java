package com.cuber.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.Map;

/**
 * Created by cuber on 2015/1/8.
 */
public class LinePlotter extends Plotter {

    private final int TEXT_PADDING = 20;
    private final float RADIUS = 10;

    private int mColor = 0XFF000000;
    private int mTextSize = 35, mLineWidth = 5;

    private Paint mTextPaint, mLinePaint;

    public LinePlotter(Chart chart) {
        super(chart);
        mTextPaint = new Paint() {
            {
                setAntiAlias(true);
                setStrokeWidth(2);
                setStyle(Style.FILL_AND_STROKE);
                setColor(mColor);
                setTextSize(mTextSize);
            }
        };

        mLinePaint = new Paint() {
            {
                setAntiAlias(true);
                setStrokeWidth(mLineWidth);
                setStyle(Style.FILL_AND_STROKE);
                setColor(mColor);
            }
        };
    }

    public void setGraphicColor(int color) {
        mTextPaint.setColor(color);
        mLinePaint.setColor(color);
    }


    protected void drawDataPoint(Canvas canvas, String value, int dataSet, int position, PointF from, PointF to, Map<String, Object> options) {

        if (isShowGraphic) {
            //Line
            if (from != null)
                canvas.drawLine(from.x, from.y, to.x, to.y, mLinePaint);


            if (isShowValueOnGraphic) {
                //Value
                canvas.drawText(value, to.x, (to.y - TEXT_PADDING), mTextPaint);

                //Point
                canvas.drawCircle(to.x, to.y, RADIUS, mLinePaint);
            }
        }
    }
}
