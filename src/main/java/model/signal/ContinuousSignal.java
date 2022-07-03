package model.signal;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.NoiseParam;

import java.util.ArrayList;
import java.util.List;

public class ContinuousSignal extends AbstractSignal {

    protected XYChart getNewChart(NumberAxis xAxis, NumberAxis yAxis) {
        LineChart chart = new LineChart<>(xAxis, yAxis);
        chart.setCreateSymbols(false);
        chart.getStyleClass().add("thick-chart");
        return chart;
    }


    @Override
    public Signal copyOf() {
        List<XYChart.Data<Double, Double>> datasetCopy = new ArrayList<>();
        ContinuousSignal copySignal = new ContinuousSignal();

        for (XYChart.Data<Double, Double> point :this.dataset) {
            datasetCopy.add(point);
        }

        NoiseParam paramsCopy = this.getNoiseParam().copyOf();
        copySignal.generateSignal(paramsCopy, datasetCopy);

        return copySignal;
    }
}
