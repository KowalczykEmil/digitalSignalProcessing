package model.signal;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.apache.commons.math3.util.Precision;

import java.util.concurrent.Callable;

public class ContinuousSignal extends AbstractSignal {

    protected XYChart getNewChart(NumberAxis xAxis, NumberAxis yAxis) {
        LineChart chart = new LineChart<>(xAxis, yAxis);
        chart.setCreateSymbols(false);
        chart.getStyleClass().add("thick-chart");
        return chart;
    }

}
