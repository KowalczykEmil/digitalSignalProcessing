package model.noise;

import javafx.scene.chart.XYChart;
import model.signal.ContinuousSignal;
import model.signal.Signal;

import java.util.List;
import java.util.Random;

public class UniformNoise extends AbstractNoise {
    @Override
    public String getName() {
        return "Szum o rozkładzie jednostajnym";
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
            double y = ((Math.random() * (amplitude - (-1 * amplitude))) + (-1 * amplitude));
            dataset.add(new XYChart.Data<>(x, y));
        }
        return dataset;
    }


    @Override
    protected boolean isFillFactorEditorVisible() {
        return false;
    }
}
