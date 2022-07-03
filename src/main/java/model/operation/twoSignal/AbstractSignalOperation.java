package model.operation.twoSignal;

import addons.OperationException;
import javafx.scene.chart.XYChart;
import lombok.Getter;
import lombok.Setter;
import model.NoiseParam;
import model.signal.ContinuousSignal;
import model.signal.DiscreteSignal;
import model.signal.Signal;

import java.util.ArrayList;
import java.util.List;

abstract class AbstractSignalOperation implements SignalOperation {
	final double epsilon = 0.000001d;
	final double DOUBLE_MAX_VALUE = (2 - Math.pow(2, -23)) * Math.pow(2, 127);
	NoiseParam param = new NoiseParam();

	@Override
	public Signal execute(Signal first, Signal second) throws OperationException {
		Signal output;
		NoiseParam firstParams = first.getNoiseParam();
		NoiseParam secondParams = second.getNoiseParam();

		checkSampling(firstParams, secondParams);

		if (first instanceof ContinuousSignal && second instanceof ContinuousSignal) {
			output = new ContinuousSignal();
		} else if (first instanceof DiscreteSignal && second instanceof DiscreteSignal) {
			output = new DiscreteSignal();
		} else {
			throw new OperationException("Sygnały powinny być tego samego typu");
		}

		List<XYChart.Data<Double, Double>> dataset = getDataset(first, second);
		NoiseParam param = prepareParams(dataset, firstParams.getSamplingPeriod());
		output.generateSignal(param, dataset);

		return output;
	}

	private void checkSampling(NoiseParam firstParams, NoiseParam secondParams) throws OperationException {
		if (firstParams != null && secondParams != null ) {
			if (!firstParams.getSamplingPeriod().equals(secondParams.getSamplingPeriod())) {
				throw new OperationException("Próbkowanie obu sygnałów poinno być takie samo (" + firstParams.getSamplingPeriod() + " != " + secondParams.getSamplingPeriod() + ")");
			}
		}
	}

	private List<XYChart.Data<Double, Double>> getDataset(Signal first, Signal second) {
		List<XYChart.Data<Double, Double>> dataset;

		List<XYChart.Data<Double, Double>> firstDataset = first.getDataset();
		List<XYChart.Data<Double, Double>> secondDataset = second.getDataset();

		double firstMinXValue = DOUBLE_MAX_VALUE;
		double secondMinXValue = DOUBLE_MAX_VALUE;
		if (!firstDataset.isEmpty()) {
			firstMinXValue = firstDataset.get(0).getXValue();
		}
		if (!secondDataset.isEmpty()) {
			secondMinXValue = secondDataset.get(0).getXValue();
		}

		if (firstMinXValue <= secondMinXValue) {
			dataset = executeCalculation(secondDataset, firstDataset);
		} else {
			dataset = executeCalculation(firstDataset, secondDataset);
		}
		if (first.getNoiseParam().getBasePeriod() != null && second.getNoiseParam().getBasePeriod() != null) {
			if(first.getNoiseParam().getBasePeriod()>second.getNoiseParam().getBasePeriod()){
				param.setBasePeriod(first.getNoiseParam().getBasePeriod());
			}else{
				param.setBasePeriod(second.getNoiseParam().getBasePeriod());
			}
		}

		return dataset;
	}

	private List<XYChart.Data<Double, Double>> executeCalculation(List<XYChart.Data<Double, Double>> firstDataset, List<XYChart.Data<Double, Double>> secondDataset) {
		List<XYChart.Data<Double, Double>> dataset = new ArrayList<>();

		Iterators interators = new Iterators();

		processData(firstDataset, secondDataset, dataset, interators);

		Double firstMaxX = firstDataset.get(firstDataset.size() -1).getXValue();
		Double secondMaxX = secondDataset.get(secondDataset.size() -1).getXValue();

//		już przeszliśmy przez pierwszy dataset, sprawdzamy czy w drugim coś jest
		if (firstMaxX.compareTo(secondMaxX) > 0) {
			processData(secondDataset, firstDataset, dataset, interators);
		}
		return dataset;
	}

	private void processData(List<XYChart.Data<Double, Double>> firstDataset, List<XYChart.Data<Double, Double>> secondDataset, List<XYChart.Data<Double, Double>> dataset, Iterators iterators) {
		int secondIter = iterators.getFirstIter();
		int firstIter = iterators.getSecondIter();

		for (; secondIter < secondDataset.size(); secondIter++) {
			Double secondX = secondDataset.get(secondIter).getXValue();
			Double secondY = secondDataset.get(secondIter).getYValue();
			if (firstIter < firstDataset.size()) {
				Double firstX = firstDataset.get(firstIter).getXValue();
				Double firstY = firstDataset.get(firstIter).getYValue();
				if (Math.abs(firstX - secondX) < epsilon) {
					dataset.add(new XYChart.Data<>(secondX, calculate(secondY, firstY)));
					firstIter++;
				} else {
					dataset.add(new XYChart.Data<>(secondX, secondY)); //czy nie powinniśmy liczyć wtedy Y jako 0?
				}
			} else {
				dataset.add(new XYChart.Data<>(secondX, secondY));
			}
		}
		iterators.setFirstIter(firstIter);
		iterators.setSecondIter(secondIter);
	}

	abstract double calculate(Double first, Double second);

	private NoiseParam prepareParams(List<XYChart.Data<Double, Double>> dataset, Double sampling) {

		if (dataset != null &&!dataset.isEmpty()) {
			double minX = dataset.get(0).getXValue();
			double maxX = dataset.get(dataset.size() -1).getXValue();

			double maxY = 0;
			double minY = 0;
			for (XYChart.Data<Double, Double> point :dataset) {
				if (point.getYValue().compareTo(maxY) > 0) {
					maxY = point.getYValue();
				}
				if (point.getYValue().compareTo(minY) < 0) {
					minY = point.getYValue();
				}
			}

			param.setInitialTime(minX);
			param.setDuration(maxX - minX);
			param.setAmplitude(Double.max(maxY, -minY));
			param.setSamplingPeriod(sampling);
		}
		return param;
	}


	@Getter
	@Setter
	class Iterators{
		private int secondIter = 0;
		private int firstIter = 0;
	}

}
