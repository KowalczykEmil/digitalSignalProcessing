package model.noise;

import javafx.scene.chart.XYChart;
import model.signal.ContinuousSignal;
import model.signal.Signal;

import java.util.List;

public class UnitJump extends AbstractNoise {
    @Override
    public String getName() {
        return "Skok jednostkowy";
    }

    @Override
    protected Signal getSignalType() {
        return new ContinuousSignal();
    }

    protected List<XYChart.Data<Double, Double>> generateDataSeries() {
        double amplitude = params.getAmplitude();
        double initialTime = params.getInitialTime();
        double finalTime = params.getInitialTime() + params.getDuration();

        for (double x = initialTime; x <= finalTime; x += params.getSampling()) {
            double y;
            if (x < 0) {
                y = 0;
            } else if (x == 0) {
                y = amplitude / 2;
            } else {
                y = amplitude;
            }
            dataset.add(new XYChart.Data<>(x, y));
        }
        return dataset;
    }


    @Override
    protected boolean isFillFactorEditorVisible() {
        return false;
    }
}
