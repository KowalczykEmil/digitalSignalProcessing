package model.operation.singleSignal;

import addons.OperationException;
import javafx.scene.chart.XYChart;
import model.NoiseParam;
import model.signal.ContinuousSignal;
import model.signal.DiscreteSignal;
import model.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class SamplingOperation implements SingleSignalOperation {
	final double epsilon = 0.00001d;
	int currentPointIndex;


	@Override
	public Signal execute(Signal signal, SingleSignalOperationParam param) throws OperationException {
		DiscreteSignal output = new DiscreteSignal();
		List<XYChart.Data<Double, Double>> inputDataset = signal.getDataset();
		List<XYChart.Data<Double, Double>> outputDataset = new ArrayList<>();
		NoiseParam inputNoiseParam = signal.getNoiseParam();
		NoiseParam outputNoiseParam = inputNoiseParam.copyOf();

		outputNoiseParam.setSamplingPeriod(param.getSamplingFrequency());

		double samplingPeriod = 1 / param.getSamplingFrequency();
		if (signal instanceof DiscreteSignal) {
			for (XYChart.Data<Double, Double> point : inputDataset) {
				double dd = (point.getXValue() % samplingPeriod);
				if (dd < epsilon || (samplingPeriod - dd < epsilon)) {
					outputDataset.add(point);
				}
			}
		} else if (signal instanceof ContinuousSignal) {
			currentPointIndex = 0;
			double initXValue = inputDataset.get(0).getXValue();
			double finalXValue = inputDataset.get(inputDataset.size() - 1).getXValue();

			double samplingX = initXValue;
			while (isWithin(samplingX, finalXValue)) {
				XYChart.Data<Double, Double> point = getPointByXValue(samplingX, inputDataset);
				double fraction = (point.getXValue() % samplingPeriod);
				if ((fraction < epsilon || (Math.abs(samplingPeriod - fraction) < epsilon))) { //trafilsmy równo
					outputDataset.add(point);
				} else { //liczymy wartośc pośrednią między punktami
					outputDataset.add(getInterpolatedPoint(inputDataset, samplingX, point));
				}
				samplingX += samplingPeriod;
			}
		}

		output.generateSignal(outputNoiseParam, outputDataset);
		return output;
	}

	private XYChart.Data<Double, Double> getInterpolatedPoint(List<XYChart.Data<Double, Double>> inputDataset, double samplingX, XYChart.Data<Double, Double> point) {
		XYChart.Data<Double, Double> secondPoint = inputDataset.get(currentPointIndex - 1);
		double firstYValue = point.getYValue();
		double secondYValue = secondPoint.getYValue();

		double firstDist = point.getXValue() - samplingX;
		double secondDist = samplingX - secondPoint.getXValue();

		double samplingY = ((secondYValue * 1 / secondDist) + (firstYValue * 1 / firstDist)) / (1 / firstDist + 1 / secondDist);
		XYChart.Data<Double, Double> interpolatedPoint = new XYChart.Data<>();
		interpolatedPoint.setXValue(samplingX);
		interpolatedPoint.setYValue(samplingY);
		return interpolatedPoint;
	}

	private boolean isWithin(double currentX, double finalX) {
		if (finalX - currentX >= 0) {
			return true;
		} else if (Math.abs(finalX - currentX) < epsilon) {
			return true;
		}
		return false;
	}

	private XYChart.Data<Double, Double> getPointByXValue(double xValue, List<XYChart.Data<Double, Double>> dataset) throws OperationException {
		for (int i = currentPointIndex; i < dataset.size(); i++) {
			currentPointIndex = i;
			if (dataset.get(i).getXValue() >= xValue) { //znajdujemy pierwszą wartość która jest większa od naszgo X
				return dataset.get(i);
			}
		}
		if (Math.abs(xValue - dataset.get(dataset.size() - 1).getXValue()) < epsilon) { //dodatkowo sprawdzamy żeby ostatni punkt się zalapal
			return dataset.get(dataset.size() - 1);
		}
		throw new OperationException("Wykroczono poza zakres wartości X");
	}


	private void copyParams(NoiseParam inputNoiseParam, NoiseParam outputNoiseParam) {
		outputNoiseParam.setSamplingPeriod(inputNoiseParam.getSamplingPeriod());
		outputNoiseParam.setAmplitude(inputNoiseParam.getAmplitude());
		outputNoiseParam.setInitialTime(inputNoiseParam.getInitialTime());
		outputNoiseParam.setDuration(inputNoiseParam.getDuration());
		outputNoiseParam.setBasePeriod(inputNoiseParam.getBasePeriod());
		outputNoiseParam.setFillFactor(inputNoiseParam.getFillFactor());
	}

	@Override
	public String getName() {
		return "Próbkowanie";
	}
}
