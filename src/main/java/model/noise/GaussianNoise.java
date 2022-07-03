package model.noise;

import javafx.scene.chart.XYChart;
import model.signal.ContinuousSignal;
import model.signal.Signal;

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

		Random r = new Random();

		for (double x = phaseShift; x <= finalTime; x += params.getSamplingPeriod()) {
			double y = (r.nextGaussian()) * amplitude / 3;
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

	@Override
	protected boolean isBasePeriodEditorVisible() {
		return false;
	}

}
