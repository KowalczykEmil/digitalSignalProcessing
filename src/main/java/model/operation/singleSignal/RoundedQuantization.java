package model.operation.singleSignal;

import addons.OperationException;
import javafx.scene.chart.XYChart;
import model.NoiseParam;
import model.signal.DiscreteSignal;
import model.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class RoundedQuantization implements SingleSignalOperation {

//https://technobyte.org/quantization-truncation-rounding/

	@Override
	public Signal execute(Signal signal, SingleSignalOperationParam param) throws OperationException {
		DiscreteSignal output = new DiscreteSignal();
		List<XYChart.Data<Double, Double>> inputDataset = signal.getDataset();
		List<XYChart.Data<Double, Double>> outputDataset = new ArrayList<>();
		NoiseParam inputNoiseParam = signal.getNoiseParam();
		double quantizationStep = param.getQuantizationStep();

		NoiseParam outputNoiseParam = inputNoiseParam.copyOf();

		for (XYChart.Data<Double, Double> point : inputDataset) {
			XYChart.Data<Double, Double> outputPoint = new XYChart.Data<Double, Double>();
			Double yValue = Math.floor((point.getYValue()/quantizationStep) + 0.5) * quantizationStep;
			outputPoint.setXValue(point.getXValue());
			outputPoint.setYValue(yValue);
			outputDataset.add(outputPoint);
		}


		output.generateSignal(outputNoiseParam,outputDataset);

		return output;
	}

	@Override
	public String getName() {
		return "Kwantyzacja równomierna z zaokrągleniem";
	}
}
