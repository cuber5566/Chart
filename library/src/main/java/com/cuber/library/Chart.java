package com.cuber.library;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
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

    protected float maxScreenX, maxScreenY;
    private double minDataX, maxDataX, minDataY, maxDataY;

    private float mWidth, mHeight;
    private Paint mPaint_gray, mPaint_black, mPaint_textX, mPaint_textY;
    private float mPadding_bottom = 32;//dp
    private float mPadding_left = 32;//dp
    private float mPadding_text = 2;//dp
    private float labelSpaceX = 40;//56
    private float mTextSizeX = 48;//px
    private float mTextSizeY = 48;//px
    private float mShortLineLength = 8;

    //display
    private boolean showMinorXLine = true;
    private boolean showMajorXLine = true;
    private boolean showMinorYLine = true;
    private boolean showMajorYLine = true;

    //temp
    private float move_x;

    //interface
    private OnScrollChangeListener listener;
    private double callbackValue;

    public interface OnScrollChangeListener {
        public void onScrollChange(float offset);

        public void onXValueChange(double XValue);
    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnTouchListener(this);

        mResources = context.getResources();
        mPadding_bottom = convertDpToPixel((int) mPadding_bottom);
        mPadding_left = convertDpToPixel((int) mPadding_left);
        mPadding_text = convertDpToPixel((int) mPadding_text);
        mShortLineLength = convertDpToPixel((int) mShortLineLength);
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

    public void showMinorXLine(boolean show) {
        this.showMinorXLine = show;
        invalidate();
    }

    public void showMajorXLine(boolean show) {
        this.showMajorXLine = show;
        invalidate();
    }

    public void showMinorYLine(boolean show) {
        this.showMinorYLine = show;
        invalidate();
    }

    public void showMajorYLine(boolean show) {
        this.showMajorYLine = show;
        invalidate();
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.listener = listener;
    }

    float last_x = 0;
    float cur_x = 0;

    float last_zoom = 0;
    float cur_zoom = 0;

    float d_x;
    float d_zoom;

    float last_labelSpaceX;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                cur_x = last_x = event.getX();
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                cur_zoom = last_zoom = event.getX(0) - event.getX(1);
                break;

            case MotionEvent.ACTION_MOVE:

                last_labelSpaceX = labelSpaceX;

                // zoom
                if (event.getPointerCount() > 1) {

                    cur_zoom = Math.abs(event.getX(0) - event.getX(1));
                    d_zoom = Math.abs(cur_zoom) - Math.abs(last_zoom);
                    last_zoom = cur_zoom;

                    labelSpaceX += d_zoom;

                    //zoom in max
                    double a_data = mAdapter.getMaxDataX() - mAdapter.getMinDataX();
                    if (labelSpaceX * a_data < mWidth) {
                        labelSpaceX = (float) (mWidth / a_data);
                    }
                    //zoom out max
                    else if (labelSpaceX >= mWidth) {
                        labelSpaceX = mWidth;
                    }

                    move_x *= labelSpaceX / last_labelSpaceX;

                }

                // move
                else {
                    cur_x = event.getX();

                    d_x = cur_x - last_x;
                    last_x = cur_x;
                    move_x += d_x;
                }

                //move left max
                if (move_x < 0) {
                    move_x = 0;
                }
                //move right max
                else if (move_x > maxScreenX - mWidth) {
                    move_x = maxScreenX - mWidth;
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_POINTER_UP:
                //pointer0 & pointer1 both may be up
                //if pointer0 up ,last_x change to pointer1
                if ((event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) == 0) {
                    last_x = event.getX(1);
                }
                break;
        }
        if (listener != null)
            listener.onScrollChange(move_x);

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();

        maxScreenY = mHeight - mPadding_bottom;

        double max = ((maxDataX - minDataX) / mAdapter.getXMinorGridGap()) * labelSpaceX;
        maxScreenX = (float) max > mWidth ? (float) max : mWidth;

        canvas.save();
        canvas.clipRect(mPadding_left, 0, mWidth, mHeight);

        drawXLine(canvas);
        drawYLine(canvas);
        for (int i = 0; i < mAdapter.getCountOfDataSet(); i++)
            drawGraphic(canvas, i);

        canvas.restore();
        drawLeftSide(canvas);

    }

    private void drawYLine(Canvas canvas) {

        double gap = mAdapter.getYMinorGridGap();

        // draw minor lines
        if (showMinorYLine) {
            for (double i = minDataY; i < maxDataY; i += gap) {
                float y = convertDataYToScreenY(i);
                canvas.drawLine(0, y, maxScreenX, y, mPaint_gray);
            }
        }

        // draw major lines
        if (showMajorYLine) {
            gap *= mAdapter.getYMajorGridGroup();
            for (double i = minDataY; i < maxDataY; i += gap) {

                float y = convertDataYToScreenY(i);
                canvas.drawText(mAdapter.getYLabelForYValue(i), 0, convertDataYToScreenY(i) - mPadding_text, mPaint_textY);

                canvas.drawLine(0, y, maxScreenX, y, mPaint_black);
            }
        }
    }

    private void drawXLine(Canvas canvas) {

        double gap = mAdapter.getXMinorGridGap();
        double startX = convertScreenXToDataX(maxScreenX - move_x - mWidth);
        double endX = convertScreenXToDataX(maxScreenX - move_x);
        startX -= (startX - minDataX) % gap;

        // draw minor lines
        if (showMinorXLine) {
            for (double i = startX; i < endX; i += gap) {
                float x = (move_x + convertDataXToScreenX(i) - (maxScreenX - mWidth));
                if (x > maxScreenX) break;
                canvas.drawLine(x, 0, x, maxScreenY, mPaint_gray);
            }
        }

        if (listener != null && callbackValue != endX - endX % gap) {
            callbackValue = endX - endX % gap;
            listener.onXValueChange(callbackValue);
        }

        // draw major lines
        gap *= mAdapter.getXMajorGridGroup();
        startX -= (startX - minDataX) % gap;

        double i;
        for (i = startX; i < endX; i += gap) {
            float x = (move_x + convertDataXToScreenX(i) - (maxScreenX - mWidth));
            if (x > maxScreenX) break;

            String text = mAdapter.getXLabelForXValue(i);
            mPaint_textX.getTextBounds(text, 0, text.length(), textBounds);

            canvas.drawText(text, (x - textBounds.width() / 2), (maxScreenY + textBounds.height() + mShortLineLength / 2), mPaint_textX);

            if (showMajorXLine) {
                canvas.drawLine(x, 0, x, maxScreenY, mPaint_black);
            } else {
                canvas.drawLine(x, mHeight - mPadding_bottom - mShortLineLength, x, mHeight - mPadding_bottom, mPaint_black);
            }
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
                canSkip = (x < 0 || x > maxScreenX);
            } else {
                canSkip = (x < 0 && last_p.x < 0) ||
                        (x > maxScreenX && last_p.x > maxScreenX);
            }

            if (canSkip) break;

            plotter.drawDataPoint(canvas, mAdapter.getValueToString(position, mAdapter.getYValueForDataPoint(position, i)), position, i, last_p, cur_p, options);

            last_p = cur_p;
        }
    }

    private void drawLeftSide(Canvas canvas) {

        double gap = mAdapter.getYMinorGridGap();
        gap *= mAdapter.getYMajorGridGroup();

        for (double i = minDataY; i < maxDataY; i += gap) {

            float y = convertDataYToScreenY(i);
            String text = mAdapter.getYLabelForYValue(i);
            mPaint_textX.getTextBounds(text, 0, text.length(), textBounds);

            canvas.drawText(text, mPadding_left - textBounds.width() - mPadding_text, y, mPaint_textX);
            canvas.drawLine(mPadding_left, y, mPadding_left + mShortLineLength, y, mPaint_black);
        }

        //X,Y Base Line
        canvas.drawLine(mPadding_left, maxScreenY, mWidth, maxScreenY, mPaint_black);
        canvas.drawLine(mPadding_left, 0, mPadding_left, maxScreenY, mPaint_black);
    }

    protected float convertDpToPixel(int dp) {
        return dp * (mResources.getDisplayMetrics().densityDpi / 160f);
    }

    protected float convertDataXToScreenX(double dataX) {

        return (float) ((dataX - minDataX) * maxScreenX / (maxDataX - minDataX));
    }

    protected double convertScreenXToDataX(float screenX) {

        return screenX * (maxDataX - minDataX) / maxScreenX + minDataX;
    }

    protected float convertDataYToScreenY(double dataY) {

        return (float) (maxScreenY - (dataY - minDataY) * maxScreenY / (maxDataY - minDataY));
    }

    protected double convertScreenYToDataY(float screenY) {

        return screenY * (maxDataY - minDataY) / maxScreenY + minDataY;
    }
}
