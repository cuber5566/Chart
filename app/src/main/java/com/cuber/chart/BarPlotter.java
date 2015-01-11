package com.cuber.chart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cuber on 2015/1/8.
 */
public class BarPlotter extends Plotter {

    private final int TEXT_PADDING = 20;

    private int mColor = 0XFF000000;
    private int mTextSize = 35, mLineWidth = 45;

    private Paint mTextPaint, mLinePaint;

    private List<Integer> indexList = new ArrayList<>();
    private Rect textBounds = new Rect();


    private int index = 0;
    private int offset = 0;
    private boolean isFirstDraw = true;

    public BarPlotter(Chart chart) {
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

    private void getOffset() {
        int count = 0;
        for (int i = 0; i < getGraphic().mAdapter.getCountOfDataSet(); i++) {

            Map<String, Object> o = getGraphic().mAdapter.getDrawingOptionsForDataSet(i);
            if (o.containsKey(ChartBaseAdapter.DRAWING_OPTION_PLOTTER)) {

                if (o.get(ChartBaseAdapter.DRAWING_OPTION_PLOTTER) instanceof BarPlotter) {
                    count++;
                    indexList.add(i);
                }
            }
        }

        if (count % 2 == 0)
            offset = ((count - 1) / 2) * mLineWidth + mLineWidth / 2;
        else
            offset = ((count - 1) / 2) * mLineWidth;

    }

    protected void drawDataPoint(Canvas canvas, String value, int dataSet, int position, PointF from, PointF to, Map<String, Object> options) {

        if (isFirstDraw) {
            getOffset();
            isFirstDraw = false;
        }

        for (int i = 0; i < indexList.size(); i++) {
            if (indexList.get(i) == dataSet) {
                index = i;
                break;
            }
        }

        if (isShowGraphic) {

            float x = to.x - offset + (index * mLineWidth);

            //Bar
            canvas.drawLine(x, to.y, x, getGraphic().maxScreenY, mLinePaint);

            //Value
            if (isShowValueOnGraphic) {
                mTextPaint.getTextBounds(value, 0, value.length(), textBounds);
                canvas.drawText(value, x - textBounds.width() / 2, to.y - TEXT_PADDING, mTextPaint);
            }
        }

    }
}
