package controller;

import javafx.scene.chart.XYChart;
import model.NoiseParam;
import model.signal.ContinuousSignal;
import model.signal.DiscreteSignal;
import model.signal.Signal;
import gui.graph.SignalTab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BinaryController {
	private final String POINT_END_SEPARATOR = "\n";
	private final String POINT_X_Y_SEPARATOR = " ";
	private final String TAB_SEPARATOR = ";";
	private final String CONTINUOUS_SGN_TAG = "C";
	private final String DISCRETE_SGN_TAG = "D";
	double epsilon = 0.0000001d;

	public byte[] getDataFromTab(SignalTab signalPane, Double sampling) {
		StringBuilder stringBuilder = new StringBuilder();

		Signal signal = signalPane.getSignal();
		List<XYChart.Data<Double, Double>> dataset = signal.getDataset();

		NoiseParam params = signal.getNoiseParam();
		NoiseParam newParams = new NoiseParam();
		copyParams(params, newParams);

		newParams.setSamplingPeriod(sampling);
		appendProperties(stringBuilder, newParams);
		appendAmplitudes(stringBuilder, dataset, sampling);
		appendSignalType(stringBuilder, signal);

		return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
	}

	private void appendSignalType(StringBuilder stringBuilder, Signal signal) {
		if (signal instanceof ContinuousSignal) {
			stringBuilder.append(CONTINUOUS_SGN_TAG);
		} else if (signal instanceof DiscreteSignal) {
			stringBuilder.append(DISCRETE_SGN_TAG);
		}
	}

	private void appendProperties(StringBuilder stringBuilder, NoiseParam params) {
		stringBuilder.append(params.getBasePeriod() + POINT_END_SEPARATOR);
		stringBuilder.append(params.getInitialTime() + POINT_END_SEPARATOR);
		stringBuilder.append(params.getDuration() + POINT_END_SEPARATOR);
		stringBuilder.append(params.getAmplitude() + POINT_END_SEPARATOR);
		stringBuilder.append(params.getFillFactor() + POINT_END_SEPARATOR);
		stringBuilder.append(params.getSamplingPeriod() + POINT_END_SEPARATOR);
	}

	private void appendAmplitudes(StringBuilder stringBuilder, List<XYChart.Data<Double, Double>> dataset, Double sampling) {
		for (XYChart.Data<Double, Double> point : dataset) {
			double dd = (point.getXValue() % sampling);
			if (dd < epsilon || (sampling - dd < epsilon)) {
				stringBuilder.append(point.getYValue() + POINT_END_SEPARATOR);
			}
		}
	}

	public List<Signal> getSignalsFromData(String path) {
		List<Signal> signals = new ArrayList<>();
		List<XYChart.Data<Double, Double>> dataset = new ArrayList<>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine();
			boolean newSignal = true;
			NoiseParam params = null;
			Double xValue = 0d;

			while (line != null) {
				if (newSignal) {
					params = new NoiseParam();
					params.setBasePeriod(line.equals("null") ? null : Double.valueOf(line));
					line = reader.readLine();
					params.setInitialTime(line.equals("null")  ? null :Double.valueOf(line));
					line = reader.readLine();
					params.setDuration(line.equals("null") ? null :Double.valueOf(line));
					line = reader.readLine();
					params.setAmplitude(line.equals("null")  ? null :Double.valueOf(line));
					line = reader.readLine();
					params.setFillFactor(line.equals("null")  ? null :Double.valueOf(line));
					line = reader.readLine();
					params.setSamplingPeriod(line.equals("null") ? null :Double.valueOf(line));
					line = reader.readLine();
					newSignal = false;
					xValue = params.getInitialTime();
				}

				if (line.startsWith(CONTINUOUS_SGN_TAG)) {
					Signal signal = new ContinuousSignal();
					signal.generateSignal(params, dataset);
					signals.add(signal);
					dataset = new ArrayList<>();
					newSignal = true;
				} else if (line.startsWith(DISCRETE_SGN_TAG)) {
					Signal signal = new DiscreteSignal();
					signal.generateSignal(params, dataset);
					signals.add(signal);
					dataset = new ArrayList<>();
					newSignal = true;
				} else {
					Double pointY = Double.valueOf(line);
					dataset.add(new XYChart.Data(xValue, pointY));
					xValue = xValue + params.getSamplingPeriod();
				}
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return signals;
	}


	private void copyParams(NoiseParam inputNoiseParam, NoiseParam outputNoiseParam) {
		outputNoiseParam.setSamplingPeriod(inputNoiseParam.getSamplingPeriod());
		outputNoiseParam.setAmplitude(inputNoiseParam.getAmplitude());
		outputNoiseParam.setInitialTime(inputNoiseParam.getInitialTime());
		outputNoiseParam.setDuration(inputNoiseParam.getDuration());
		outputNoiseParam.setBasePeriod(inputNoiseParam.getBasePeriod());
		outputNoiseParam.setFillFactor(inputNoiseParam.getFillFactor());
	}
}
