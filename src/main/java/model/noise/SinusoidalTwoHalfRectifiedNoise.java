package model.noise;

import javafx.scene.chart.XYChart;
import model.signal.ContinuousSignal;
import model.signal.Signal;

import java.util.List;

public class SinusoidalTwoHalfRectifiedNoise extends AbstractNoise {

	@Override
	public String getName() {
		return "sinusoidalny wyprostowany dwupołówkowo";
	}

	@Override
	protected Signal getSignalType() {
		return new ContinuousSignal();
	}

	protected List<XYChart.Data<Double, Double>> generateDataSeries() {
		double amplitude = params.getAmplitude();
		double initialTime = params.getInitialTime();
		double finalTime = params.getInitialTime() + params.getDuration();
		double basePeriod = params.getBasePeriod();

		for (double x = initialTime; x <= finalTime; x += params.getSamplingPeriod()) {
			double sin = Math.sin((2.0 * Math.PI / basePeriod) * (x - initialTime));
			double y = amplitude * (Math.abs(sin));
			dataset.add(new XYChart.Data<>(x, y));
		}
		return dataset;
	}


	@Override
	protected boolean isFillFactorEditorVisible() {
		return false;
	}

}
