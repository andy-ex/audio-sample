package util;

import javafx.scene.chart.XYChart;

/**
 * Created by Dmitry_Volchek on 7/10/2014.
 */
public class LineChartUtil {

    public static  XYChart.Series<Number, Number> createNumberSeries(int[] data) {
        return createNumberSeries(data, 1);
    }

    public static  XYChart.Series<Number, Number> createNumberSeries(int[] data, int rate) {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        for (int i = 0; i < data.length; i = i + rate) {
            series.getData().add(new XYChart.Data<>(i, data[i]));
        }
        return series;
    }
}
