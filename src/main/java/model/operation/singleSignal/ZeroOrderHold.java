package model.operation.singleSignal;

import addons.OperationException;
import javafx.scene.chart.XYChart;
import model.NoiseParam;
import model.signal.ContinuousSignal;
import model.signal.DiscreteSignal;
import model.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class ZeroOrderHold implements SingleSignalOperation {
	@Override
	public Signal execute(Signal signal, SingleSignalOperationParam param) throws OperationException {
		if (!(signal instanceof DiscreteSignal)) {
			throw new OperationException("Wybrana operacja wymaga sygnału dyskretnego na wejściu!");
		}

		Signal outputSignal = new ContinuousSignal();
		List<XYChart.Data<Double, Double>> inputDataset = signal.getDataset();
		List<XYChart.Data<Double, Double>> outputDataset = new ArrayList<>();
		NoiseParam inputNoiseParam = signal.getNoiseParam();

		NoiseParam outputNoiseParam = inputNoiseParam.copyOf();

		double samplingPeriod = 1 / param.getSamplingFrequency();

		int numberOfSamples = (int) (inputNoiseParam.getDuration() / samplingPeriod);
		int currentInputSample = -1;
		double currentYValue = inputDataset.get(0).getYValue();

		for (int i = 0; i < numberOfSamples; i++) {
			double currentXValue = i * samplingPeriod;
			XYChart.Data<Double, Double> outputPoint = new XYChart.Data<>();
			XYChart.Data<Double, Double> inputPoint;
			if (currentInputSample + 1 < inputDataset.size() -1) {
				inputPoint = inputDataset.get(currentInputSample + 1);
				if (inputPoint.getXValue() <= currentXValue) {
					currentInputSample++;
					currentYValue = inputPoint.getYValue();
				}
			}
			outputPoint.setXValue(currentXValue);
			outputPoint.setYValue(currentYValue);
			outputDataset.add(outputPoint);
		}

		outputNoiseParam.setSamplingPeriod(samplingPeriod);
		outputSignal.generateSignal(outputNoiseParam, outputDataset);
		return  outputSignal;
	}

	@Override
	public String getName() {
		return "Ekstrapolacja zerowego rzędu";
	}
}
