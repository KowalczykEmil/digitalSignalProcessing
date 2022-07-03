package model.noise;

import javafx.scene.chart.XYChart;
import model.signal.DiscreteSignal;
import model.signal.Signal;

import java.util.List;

public class ImpulseNoise extends AbstractNoise {

    @Override
    public String getName() {
        return "Szum impulsowy";
    }

    @Override
    protected Signal getSignalType() {
        return new DiscreteSignal();
    }

    protected List<XYChart.Data<Double, Double>> generateDataSeries() {
        double amplitude = params.getAmplitude();
        double initialTime = params.getInitialTime();
        double finalTime = params.getInitialTime() + params.getDuration();
        double basePeriod = params.getBasePeriod();

        for (double x = initialTime; x <= finalTime; x += basePeriod) {
            double y;
            if(0.5>Math.random()) {
                y=amplitude;
            }else {
                y=0;
            }
            dataset.add(new XYChart.Data<>(x, y));
        }

        return dataset;
    }

    @Override
    protected void correctParams() {
        params.setSamplingPeriod(params.getBasePeriod());
    }

    @Override
    protected boolean isFillFactorEditorVisible() {
        return false;
    }

}
