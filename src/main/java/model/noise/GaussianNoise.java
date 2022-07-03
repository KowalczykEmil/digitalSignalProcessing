package model.noise;

import javafx.scene.chart.XYChart;
import model.NoiseParam;
import model.signal.ContinuousSignal;
import model.signal.DiscreteSignal;
import model.signal.Signal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GaussianNoise extends AbstractNoise {

	@Override
	public String getName() {
		return "gaussowski";
	}

	protected List<XYChart.Data<Double, Double>> generateDataSeries() {
		double amplitude = params.getAmplitude();
		double phaseShift = params.getInitialTime();
		double finalTime = params.getInitialTime() + params.getDuration();
		double basePeriod = params.getBasePeriod();

		Random r = new Random();

		for (double x = phaseShift; x <= finalTime; x += params.getSampling()) {
			double y = (r.nextGaussian());
			dataset.add(new XYChart.Data<>(x, y));
		}
		return dataset;
	}

	@Override
	protected Signal getSignalType() {
		return new ContinuousSignal();
	}

	@Override
	protected boolean isFillFactorEditorVisible() {
		return false;
	}

}
