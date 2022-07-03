package model.noise;

import javafx.scene.chart.XYChart;
import model.signal.ContinuousSignal;
import model.signal.Signal;

import java.util.List;

public class RectangularSymmetricNoise extends AbstractNoise {

	@Override
	public String getName() {
		return "sygnal prostokątny symetryczny";
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

			if (x < (fillFactor * basePeriod + k * basePeriod + initialTime) && x >= (k * basePeriod + initialTime)) {
				y = amplitude;
			} else {
				y = -amplitude;
			}
			dataset.add(new XYChart.Data<>(x, y));
		}

		return dataset;
	}

}
