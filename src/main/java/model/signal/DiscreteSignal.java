package model.signal;

import javafx.scene.chart.*;
import model.NoiseParam;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.List;

public class DiscreteSignal extends AbstractSignal {

    protected XYChart getNewChart(NumberAxis xAxis, NumberAxis yAxis) {
        ScatterChart chart = new ScatterChart<Number, Number>(xAxis, yAxis);
        return chart;
    }

    @Override
    public Signal copyOf() {
        List<XYChart.Data<Double, Double>> datasetCopy = new ArrayList<>();
        DiscreteSignal copySignal = new DiscreteSignal();

        for (XYChart.Data<Double, Double> point :this.dataset) {
            datasetCopy.add(point);
        }

        NoiseParam paramsCopy = this.getNoiseParam().copyOf();
        copySignal.generateSignal(paramsCopy, datasetCopy);

        return copySignal;
    }

}
