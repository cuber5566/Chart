# Chart

![Screenshot](https://github.com/cuber5566/Chart/blob/master/app/src/main/res/drawable-mdpi/chart_pic1.jpg)

# ChartAdapter
``` java
public interface ChartAdapter {

    int getCountOfDataSet();

    int getCountOfDataPointInDataSet(int dataSet);

    double getXValueForDataPoint(int dataSet, int position);

    double getYValueForDataPoint(int dataSet, int position);
}

```
# ChartBaseAdapter
``` java
public  abstract class ChartBaseAdapter implements ChartAdapter {

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
      ...
    }

    public double getMaxDataX() {
      ...
    }

    public double getMinDataY() {
        ...
    }

    public double getMaxDataY() {
        ...
    }

    private double getYRange() {
        ...
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
```
# Plotter

only support LinePlotter&BarPlotter now
