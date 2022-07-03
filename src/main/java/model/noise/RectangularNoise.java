package model.noise;

import javafx.scene.chart.XYChart;
import model.signal.ContinuousSignal;
import model.signal.Signal;

import java.util.List;

public class RectangularNoise extends AbstractNoise {

	@Override
	public String getName() {
		return "sygnal prostokątny";
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

		for (double x = initialTime; x <= finalTime; x += params.getSampling()) {
			double y;
			int k = (int) ((x / basePeriod) - (initialTime / basePeriod));

			if (x < (fillFactor * basePeriod + k * basePeriod + initialTime) && x >= (k * basePeriod + initialTime)) {
				y = amplitude;
			} else {
				y = 0;
			}
			dataset.add(new XYChart.Data<>(x, y));
		}

		return dataset;
	}

}
