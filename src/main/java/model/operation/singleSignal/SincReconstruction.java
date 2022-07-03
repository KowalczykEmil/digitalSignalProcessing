package model.operation.singleSignal;

import addons.OperationException;
import javafx.scene.chart.XYChart;
import model.NoiseParam;
import model.signal.ContinuousSignal;
import model.signal.DiscreteSignal;
import model.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class SincReconstruction implements SingleSignalOperation {
	int currentPointIndex;
	final double epsilon = 0.00001d;


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

		double initialTime = inputDataset.get(0).getXValue();
		double finalTime = inputDataset.get(inputDataset.size() -1 ).getXValue();

		int neighbourSamples = param.getNeighbourSamples();

		currentPointIndex = 0;

		for (double x = initialTime ; x < finalTime; x += samplingPeriod) { //lecimy po wszystkich nowych próbkach
			setCurrentPointIndex(x, inputDataset);

			double amplitude = 0 ;
			for (int i = currentPointIndex - neighbourSamples; i <= currentPointIndex + neighbourSamples -1; i++) {
				if (i >= 0 && i < inputDataset.size() - 1) {
					amplitude += inputDataset.get(i).getYValue() * sinc(x / inputNoiseParam.getSamplingPeriod() - (i));
				}
			}
			XYChart.Data<Double, Double> outputPoint = new XYChart.Data<Double, Double>(x, amplitude);
			outputDataset.add(outputPoint);
		}
		outputNoiseParam.setSamplingPeriod(samplingPeriod);
		outputSignal.generateSignal(outputNoiseParam, outputDataset);
		return outputSignal;
	}

	private void setCurrentPointIndex(double xValue, List<XYChart.Data<Double, Double>> dataset) throws OperationException {
		for (int i = currentPointIndex; i< dataset.size(); i++) {
			currentPointIndex = i;
			if (dataset.get(i).getXValue() >= xValue) { //znajdujemy pierwszą wartość która jest większa od naszgo X
				return;
			}
		}
		throw new OperationException("Wykroczono poza zakres wartości X");
	}

	private double sinc(double x){
		if (x == 0) {
			return 1;
		} else {
			return (Math.sin(Math.PI * x) / (Math.PI * x));
		}
	}

	@Override
	public String getName() {
		return "Rekonstrukcja w oparciu o funkcję sinc";
	}
}
