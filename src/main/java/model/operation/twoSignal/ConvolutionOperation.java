package model.operation.twoSignal;

import addons.OperationException;
import javafx.scene.chart.XYChart;
import model.NoiseParam;
import model.signal.DiscreteSignal;
import model.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class ConvolutionOperation implements SignalOperation {
	@Override
	public String getName() {
		return "splot";
	}

	@Override
	public Signal execute(Signal first, Signal second) throws OperationException {
		if (!(first instanceof DiscreteSignal) || !(second instanceof DiscreteSignal)) {
			throw new OperationException("Sygnały wejściowe powinny być typu dyskretnego");
		}

//		if (!first.getNoiseParam().getSamplingPeriod().equals(second.getNoiseParam().getSamplingPeriod())) {
//			throw new OperationException("Sygnały wejściowe powinny być tak samo próbkowane");
//		}
		DiscreteSignal output = new DiscreteSignal();

		List<XYChart.Data<Double, Double>> firstDataset = first.getDataset();
		List<XYChart.Data<Double, Double>> secondDataset = second.getDataset();
		List<XYChart.Data<Double, Double>> outputDataset = new ArrayList<>();

		int outputLength = firstDataset.size() + secondDataset.size() - 1;


		NoiseParam outputParams = first.getNoiseParam().copyOf();
		outputParams.setBasePeriod(null);


		for (int i = 0; i < outputLength; i++) {
			double sum = 0.0;
			int secondStartIndex = Math.max(0, i - firstDataset.size() + 1);
			int secondFinishIndex = Math.min(secondDataset.size(), i + 1);
			for (int j = secondStartIndex; j < secondFinishIndex; j++) {
				sum += secondDataset.get(j).getYValue() * firstDataset.get(i - j).getYValue();
			}
			outputDataset.add(new XYChart.Data<Double, Double>(i * outputParams.getSamplingPeriod(), sum));
		}

		outputParams.setDuration(outputDataset.get(outputDataset.size() - 1).getXValue() - outputParams.getInitialTime());

		output.generateSignal(outputParams, outputDataset);
		return output;
	}
}
