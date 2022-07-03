package model.noise;

import javafx.scene.chart.XYChart;
import model.signal.DiscreteSignal;
import model.signal.Signal;

import java.util.List;

public class UnitImpulse extends AbstractNoise {

	@Override
	public String getName() {
		return "impuls jednostkowy";
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

		int numberOfImpulses = (int) ((finalTime - initialTime) / basePeriod);
		int impulseIndex = (int) ((Math.random() * (numberOfImpulses)));

		for (double i = 0; i < (numberOfImpulses * basePeriod); i += basePeriod) {
			double y = 0;
			if (i == impulseIndex * basePeriod) {
				y = amplitude;
			}
			dataset.add(new XYChart.Data<>((i + initialTime), y));
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
