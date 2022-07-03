package model.operation.twoSignal;

import addons.OperationException;
import javafx.scene.chart.XYChart;
import model.NoiseParam;
import model.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class CorrelationConvolutionOperation extends ConvolutionOperation {
	@Override
	public String getName() {
		return "korelacja (impl splot)";
	}

	@Override
	public Signal execute(Signal first, Signal second) throws OperationException {
		Signal invertedSignal = second.copyOf();

		invertedSignal.getDataset();
		List<XYChart.Data<Double, Double>> invertedDataset = new ArrayList<>();

		Double startX = invertedSignal.getDataset().get(0).getXValue();
		Double samplingPeriod = invertedSignal.getNoiseParam().getSamplingPeriod();
		Double currentXStep = samplingPeriod;

		for (int i =  invertedSignal.getDataset().size() -1 ; i > -1 ; i --) {
			invertedDataset.add(new XYChart.Data(startX + currentXStep, invertedSignal.getDataset().get(i).getYValue()));
			currentXStep += samplingPeriod;
		}

		NoiseParam noiseParam = invertedSignal.getNoiseParam().copyOf();
		invertedSignal.generateSignal(noiseParam, invertedDataset);

		return super.execute(first, invertedSignal);

	}
}
