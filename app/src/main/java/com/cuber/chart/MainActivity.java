package com.cuber.chart;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cuber.library.BarPlotter;
import com.cuber.library.Chart;
import com.cuber.library.LinePlotter;
import com.cuber.library.Plotter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    Chart mChart;
    Button show1, show2, show3, show4, XMajor, XMinor, YMajor, YMinor;
    TextView textView;
    List<Plotter> plotters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChart = (Chart) findViewById(R.id.graphics);
        mChart.setLabelSpaceX(80);

        initPlotter();
        mChart.setAdapter(new MyAdapter(mChart, plotters));

        show1 = (Button) findViewById(R.id.button1);
        show1.setOnClickListener(new View.OnClickListener() {
            boolean isShow = false;

            @Override
            public void onClick(View v) {
                plotters.get(0).showValue(isShow);
                isShow = !isShow;
            }
        });

        show2 = (Button) findViewById(R.id.button2);
        show2.setOnClickListener(new View.OnClickListener() {
            boolean isShow = false;

            @Override
            public void onClick(View v) {
                plotters.get(1).showGraphic(isShow);
                isShow = !isShow;
            }
        });

        show3 = (Button) findViewById(R.id.button3);
        show3.setOnClickListener(new View.OnClickListener() {
            boolean isShow = false;

            @Override
            public void onClick(View v) {
                plotters.get(2).showValue(isShow);
                isShow = !isShow;
            }
        });

        show4 = (Button) findViewById(R.id.button4);
        show4.setOnClickListener(new View.OnClickListener() {
            boolean isShow = false;

            @Override
            public void onClick(View v) {
                plotters.get(3).showGraphic(isShow);
                isShow = !isShow;
            }
        });

        XMajor = (Button) findViewById(R.id.button5);
        XMajor.setOnClickListener(new View.OnClickListener() {
            boolean isShow = false;

            @Override
            public void onClick(View v) {
                mChart.showMajorXLine(isShow);
                isShow = !isShow;
            }
        });

        XMinor = (Button) findViewById(R.id.button6);
        XMinor.setOnClickListener(new View.OnClickListener() {
            boolean isShow = false;

            @Override
            public void onClick(View v) {
                mChart.showMinorXLine(isShow);
                isShow = !isShow;
            }
        });

        YMajor = (Button) findViewById(R.id.button7);
        YMajor.setOnClickListener(new View.OnClickListener() {
            boolean isShow = false;

            @Override
            public void onClick(View v) {
                mChart.showMajorYLine(isShow);
                isShow = !isShow;
            }
        });

        YMinor = (Button) findViewById(R.id.button8);
        YMinor.setOnClickListener(new View.OnClickListener() {
            boolean isShow = false;

            @Override
            public void onClick(View v) {
                mChart.showMinorYLine(isShow);
                isShow = !isShow;
            }
        });

        textView = (TextView)findViewById(R.id.textView);
        mChart.setOnScrollChangeListener(new Chart.OnScrollChangeListener() {
            @Override
            public void onScrollChange(float offset) {

            }

            @Override
            public void onXValueChange(double XValue) {
                textView.setText("" + XValue);
            }
        });

    }

    public void initPlotter() {
        plotters = new ArrayList<>();

        plotters.add(new LinePlotter(mChart) {
            {
                setGraphicColor(0xaaE51C23);
            }
        });

        plotters.add(new LinePlotter(mChart) {
            {
                setGraphicColor(0xaa5677FC);
            }
        });

        plotters.add(new BarPlotter(mChart) {
            {
                setGraphicColor(0xaa259B24);
            }
        });

        plotters.add(new BarPlotter(mChart) {
            {
                setGraphicColor(0xaaFF9800);
            }
        });
    }


}
