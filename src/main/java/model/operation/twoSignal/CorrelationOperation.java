package model.operation.twoSignal;

import addons.OperationException;
import javafx.scene.chart.XYChart;
import model.NoiseParam;
import model.signal.DiscreteSignal;
import model.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class CorrelationOperation implements SignalOperation  {
	@Override
	public String getName() {
		return "korelacja (impl bezpośrednia)";
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

		NoiseParam outputParams = first.getNoiseParam().copyOf();
		outputParams.setBasePeriod(null);

		double xValue = 0;

		for (int i = secondDataset.size() - 1; i >= (-1) * firstDataset.size(); i--) {
			double sum = 0.0;
			for (int j = 0; j < firstDataset.size(); j++) {
				if (!(i + j < 0 || i + j >= secondDataset.size())) {
					sum += firstDataset.get(j).getYValue() * secondDataset.get(i + j).getYValue();
				}
			}
			outputDataset.add(new XYChart.Data<Double, Double>((xValue * outputParams.getSamplingPeriod()), sum));
			xValue ++;
		}

		outputParams.setDuration(outputDataset.get(outputDataset.size() - 1).getXValue() - outputParams.getInitialTime());

		output.generateSignal(outputParams, outputDataset);
		return output;
	}
}
