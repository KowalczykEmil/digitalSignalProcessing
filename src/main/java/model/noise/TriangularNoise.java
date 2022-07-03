package model.noise;

import javafx.scene.chart.XYChart;
import model.signal.ContinuousSignal;
import model.signal.Signal;

import java.util.List;

public class TriangularNoise extends AbstractNoise {

    @Override
    public String getName() {
        return "Sygnał trójkątny";
    }

    @Override
    protected Signal getSignalType() {
        return new ContinuousSignal();
    }

    @Override
    protected List<XYChart.Data<Double, Double>> generateDataSeries() {
        double amplitude = params.getAmplitude();
        double initialTime = params.getInitialTime();
        double finalTime = params.getInitialTime() + params.getDuration();
        double basePeriod = params.getBasePeriod();
        double fillFactor = params.getFillFactor();

        for (double x = initialTime; x <= finalTime; x += params.getSamplingPeriod()) {
            double y;
            int k = (int) ((x / basePeriod) - (initialTime / basePeriod));

            if (x >= (k * basePeriod + initialTime) && x < (fillFactor * basePeriod + k * basePeriod + initialTime)) {
                y = (amplitude / (fillFactor * basePeriod)) * (x - k * basePeriod - initialTime);
                dataset.add(new XYChart.Data<>(x, y));
            } else if (x >= (fillFactor * basePeriod + initialTime + k * basePeriod) && x < (basePeriod + k * basePeriod + initialTime)) {
                y = ((-1 * amplitude) / (basePeriod * (1 - fillFactor))) * (x - k * basePeriod - initialTime) + (amplitude / (1 - fillFactor));
                dataset.add(new XYChart.Data<>(x, y));
            }

        }

        return dataset;
    }

}
