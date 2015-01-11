package com.cuber.chart;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Map;

/**
 * Created by cuber on 2015/1/5.
 */
public class Chart extends View implements View.OnTouchListener {

    private Resources mResources;
    private Rect textBounds = new Rect();
    protected ChartBaseAdapter mAdapter;

    protected float minScreenX, maxScreenX, minScreenY, maxScreenY;
    private double minDataX, maxDataX, minDataY, maxDataY;

    private float mWidth, mHeight;
    private Paint mPaint_gray, mPaint_black, mPaint_textX, mPaint_textY;
    private float mPadding_bottom = 32;//dp
    private float mPadding_text = 2;//dp
    private float labelSpaceX = 40;//56
    private float mTextSizeX = 48;//px
    private float mTextSizeY = 48;//px


    //temp
    private float move_x;

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnTouchListener(this);

        ViewPager v;

        mResources = context.getResources();
        mPadding_bottom = convertDpToPixel((int) mPadding_bottom);
        mPadding_text = convertDpToPixel((int) mPadding_text);
        labelSpaceX = convertDpToPixel((int) labelSpaceX);

        mPaint_gray = new Paint() {
            {
                setAntiAlias(true);
                setStrokeWidth(3);
                setColor(0xFFC8C8C8);
            }
        };

        mPaint_black = new Paint() {
            {
                setAntiAlias(true);
                setStrokeWidth(3);
                setColor(0xFF000000);
                setTextSize(36);
            }
        };

        mPaint_textX = new Paint() {
            {
                setAntiAlias(true);
                setStrokeWidth(3);
                setStyle(Style.FILL);
                setColor(0xFF000000);
                setTextSize(mTextSizeX);
            }
        };

        mPaint_textY = new Paint() {
            {
                setAntiAlias(true);
                setStrokeWidth(3);
                setStyle(Style.FILL);
                setColor(0xFF000000);
                setTextSize(mTextSizeY);
            }
        };
    }

    public void setAdapter(ChartBaseAdapter adapter) {
        mAdapter = adapter;
        minDataX = mAdapter.getMinDataX();
        maxDataX = mAdapter.getMaxDataX();
        minDataY = mAdapter.getMinDataY();
        maxDataY = mAdapter.getMaxDataY();
    }

    public void setLabelSpaceX(int labelSpaceX) {
        this.labelSpaceX = convertDpToPixel(labelSpaceX);
    }

    float last_x = 0;
    float cur_x = 0;

    float last_zoom = 0;
    float cur_zoom = 0;

    float d_x;
    float d_zoom;

    float last_screen_x;
    float cur_screen_x;
    float last_move_x;
    float cur_move_x;

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                cur_x = last_x = event.getX();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                cur_zoom = last_zoom = event.getX(0) - event.getX(1);
                cur_screen_x = last_screen_x = maxScreenX - minScreenX;
                cur_move_x = last_move_x = move_x;

                break;

            case MotionEvent.ACTION_MOVE:

                cur_screen_x = maxScreenX;
                cur_move_x = move_x;

                // zoom
//                if (event.getPointerCount() >= 2) {
//                    cur_zoom = Math.abs(event.getX(0) - event.getX(1));
//                    d_zoom = Math.abs(cur_zoom) - Math.abs(last_zoom);
//                    last_zoom = cur_zoom;
//
//                    labelSpaceX += d_zoom;
//
//                    minScreenX -= d_zoom;
//                    //zoom in max
//                    double a_data = mAdapter.getMaxDataX() - mAdapter.getMinDataX();
//                    if (labelSpaceX * a_data < mWidth) {
//                        labelSpaceX = (float) (mWidth / a_data);
//                    }
//                    //zoom out max
//                    else if (labelSpaceX >= mWidth) {
//                        labelSpaceX = mWidth;
//                    }
//
//                    move_x = (int)(last_move_x * cur_screen_x / last_screen_x);
//                    last_move_x = move_x;
//                    last_screen_x = cur_screen_x;
//

//                    Log.i("DEBUG", "cur_zoom:" + cur_zoom + " , last_zoom:" + last_zoom + " ,d_zoom:" + d_zoom + ", labelSpaceX:" + labelSpaceX + " , move_x" + move_x);
//                }

                // move
//                else {
                    cur_x = event.getX();

                    d_x = cur_x - last_x;
                    last_x = cur_x;
                    move_x += d_x;
//                }

                //move left max
                if (move_x < 0) {
                    move_x = 0;
                }
                //move right max
                else if (move_x > maxScreenX - mWidth) {
                    move_x = maxScreenX - mWidth;
                }

//                Log.i("DEBUG", "move_x: " + move_x);
                invalidate();
                Log.i("Graphic", "" + move_x);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();

        minScreenY = 0;
        minScreenX = 0;
        maxScreenY = mHeight - mPadding_bottom;

        double max = ((maxDataX - minDataX) / mAdapter.getXMinorGridGap()) * labelSpaceX;
        maxScreenX = (float) max > mWidth ? (float) max : mWidth;

        drawXLine(canvas);
        drawYLine(canvas);

        for (int i = 0; i < mAdapter.getCountOfDataSet(); i++)
            drawGraphic(canvas, i);
    }

    private void drawYLine(Canvas canvas) {
        // draw minor lines
        double gap = mAdapter.getYMinorGridGap();
        for (double i = minDataY; i < maxDataY; i += gap) {
            float y = convertDataYToScreenY(i);
            canvas.drawLine(minScreenX, y, maxScreenX, y, mPaint_gray);
        }

        // draw major lines
        gap *= mAdapter.getYMajorGridGroup();
        for (double i = minDataY; i < maxDataY; i += gap) {

            float y = convertDataYToScreenY(i);
            canvas.drawText(mAdapter.getYLabelForYValue(i), minScreenX, convertDataYToScreenY(i) - mPadding_text, mPaint_textY);
            canvas.drawLine(minScreenX, y, maxScreenX, y, mPaint_black);
        }
    }

    private void drawXLine(Canvas canvas) {

        double gap = mAdapter.getXMinorGridGap();

        double startX = convertScreenXToDataX(maxScreenX - move_x - mWidth);
        startX -= (startX - minDataX) % gap;

        // draw minor lines
        for (double i = startX; i < maxDataX; i += gap) {
            float x = (move_x + convertDataXToScreenX(i) - (maxScreenX - mWidth));
            if (x > maxScreenX) break;
            canvas.drawLine(x, minScreenY, x, maxScreenY, mPaint_gray);
        }

        // draw major lines
        gap *= mAdapter.getXMajorGridGroup();
        startX = convertScreenXToDataX(maxScreenX - move_x - mWidth);
        startX -= (startX - minDataX) % gap;

        for (double i = startX; i < maxDataX; i += gap) {
            float x = (move_x + convertDataXToScreenX(i) - (maxScreenX - mWidth));
            if (x > maxScreenX) break;

            String text = mAdapter.getXLabelForXValue(i);
            mPaint_textX.getTextBounds(text, 0, text.length(), textBounds);

            canvas.drawText(text, (x - textBounds.width() / 2), (maxScreenY + textBounds.height() + mPadding_text), mPaint_textX);
            canvas.drawLine(x, minScreenY, x, maxScreenY, mPaint_black);
        }
    }

    private void drawGraphic(Canvas canvas, int position) {

        // options
        Map<String, Object> options = mAdapter.getDrawingOptionsForDataSet(position);

        Plotter plotter = new LinePlotter(this);
        if (options.containsKey(ChartBaseAdapter.DRAWING_OPTION_PLOTTER)) {
            plotter = (Plotter) options.get(ChartBaseAdapter.DRAWING_OPTION_PLOTTER);
        }

        // Binary Search
        // search for starting point to draw
        int l = 0;
        int r = mAdapter.getCountOfDataPointInDataSet(position);

        boolean found = false;
        int mid = 0;
        while (!found) {
            mid = (l + r) / 2;

            if (l == mid || r == mid) found = true;

            float x = convertDataXToScreenX(mAdapter.getXValueForDataPoint(position, mid));
            if (x > maxScreenX - move_x - mWidth) {
                r = mid;
            } else {
                l = mid;
            }
        }

        // get point
        PointF last_p = null;
        for (int i = mid; i < mAdapter.getCountOfDataPointInDataSet(position); i++) {

            float x = convertDataXToScreenX(mAdapter.getXValueForDataPoint(position, i));
            float y = convertDataYToScreenY(mAdapter.getYValueForDataPoint(position, i));

            PointF cur_p = new PointF((move_x - (maxScreenX - mWidth) + x), y);

            boolean canSkip;

            if (last_p == null) {
                canSkip = (x < minScreenX || x > maxScreenX);
            } else {
                canSkip = (x < minScreenX && last_p.x < minScreenX) ||
                        (x > maxScreenX && last_p.x > maxScreenX);
            }

            if (canSkip) break;

            plotter.drawDataPoint(canvas, mAdapter.getValueToString(position, mAdapter.getYValueForDataPoint(position, i)), position, i, last_p, cur_p, options);

            last_p = cur_p;
        }
    }

    protected float convertDpToPixel(int dp) {
        return dp * (mResources.getDisplayMetrics().densityDpi / 160f);
    }

    protected float convertDataXToScreenX(double dataX) {

        return (float) ((dataX - minDataX) * (maxScreenX - minScreenX) / (maxDataX - minDataX) + minScreenX);
    }

    protected double convertScreenXToDataX(float screenX) {

        return (screenX - minScreenX) * (maxDataX - minDataX) / (maxScreenX - minScreenX) + minDataX;
    }

    protected float convertDataYToScreenY(double dataY) {

        return (float) (maxScreenY - (dataY - minDataY) * (maxScreenY - minScreenY) / (maxDataY - minDataY) + minScreenY);
    }

    protected double convertScreenYToDataY(float screenY) {

        return (screenY - minScreenY) * (maxDataY - minDataY) / (maxScreenY - minScreenY) + minDataY;
    }
}
