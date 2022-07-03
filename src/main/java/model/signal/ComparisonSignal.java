package model.signal;

import addons.OperationException;
import javafx.scene.chart.*;
import model.NoiseParam;
import org.apache.commons.math3.util.Precision;
import gui.graph.SignalTab;

import java.util.ArrayList;
import java.util.List;

public class ComparisonSignal implements Signal {
	private LineChart graphChart;
	List<XYChart.Data<Double, Double>> firstDataset;
	List<XYChart.Data<Double, Double>> secondDataset;
	private final int CHART_POINT_FILTER = 10000;
	double epsilon = 0.0000001d;
	int currentRespondingValueIndex = 0;

	public ComparisonSignal() {
		firstDataset = new ArrayList<>();
		secondDataset = new ArrayList<>();

		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();

		LineChart chart = new LineChart<>(xAxis, yAxis);
		chart.setCreateSymbols(false);
		chart.getStyleClass().add("thick-chart");
		graphChart = chart;
	}

	public void addDataseries(SignalTab first, SignalTab second) throws OperationException {
		if (first.getSignal() == null || second.getSignal() == null) {
			throw new OperationException("jeden z sygnałów jest nullem");
		}

		if (first.getSignal() instanceof ComparisonSignal || second.getSignal() instanceof ComparisonSignal) {
			throw new OperationException("Nie można porównać wybranego sygnału");
		}

		List<XYChart.Data<Double, Double>> firstDataset = first.getSignal().getDataset();
		List<XYChart.Data<Double, Double>> secondDataset = second.getSignal().getDataset();
		this.firstDataset = firstDataset;
		this.secondDataset = secondDataset;

		XYChart.Series series1 = new XYChart.Series();
		series1.getData().addAll(filterPoints(firstDataset));
		series1.setName("Karta " + first.getIndex());

		XYChart.Series series2 = new XYChart.Series();
		series2.getData().addAll(filterPoints(secondDataset));
		series2.setName("Karta " + second.getIndex());

		graphChart.getData().addAll(series1, series2);

	}

	private List<XYChart.Data<Double, Double>> filterPoints(List<XYChart.Data<Double, Double>> input) {
		List<XYChart.Data<Double, Double>> output = new ArrayList<>();
		if (input.size() > CHART_POINT_FILTER) {
			int frequencyFactor = input.size() / CHART_POINT_FILTER;
			int i = 0;
			for (XYChart.Data<Double, Double> point : input) {
				if (i > frequencyFactor) {
					output.add(point);
					i = 0;
				}
				i++;
			}
		} else {
			for (XYChart.Data<Double, Double> point : input) {
				XYChart.Data<Double, Double> newPoint = new XYChart.Data<Double, Double>(point.getXValue(), point.getYValue());
				output.add(newPoint);
			}
		}
		return output;
	}

	@Override
	public void generateSignal(NoiseParam params, List<XYChart.Data<Double, Double>> dataset) {
	}

	@Override
	public Chart getChart() {
		return graphChart;
	}

	@Override
	public StackedBarChart getHistogram() {
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		StackedBarChart<String, Number> barChart = new StackedBarChart<String, Number>(xAxis, yAxis);
		return barChart;
	}

	@Override
	public List<XYChart.Data<Double, Double>> getDataset() {
		return null;
	}

	@Override
	public void setDataset(List<XYChart.Data<Double, Double>> dataset) {

	}

	@Override
	public int getIntervals() {
		return 0;
	}

	@Override
	public void setIntervals(int intervals) {

	}

	@Override
	public String getCharacteristics() {
		StringBuilder builder = new StringBuilder();
		Double mse = getMeanSquareError();
		builder.append("Błąd średniokwadratowy (MSE): " + getMeanSquareError() + " \n");
		Double snr = getSignalNoiseRatio();
		builder.append("Stosunek sygnał-szum (SNR): " + snr + " \n");
		builder.append("Szczytowy stosunek sygnał-szum (PSNR): " + getPeakSignalToNoiseRatio(mse) + " \n");
		builder.append("Maksymalna róznica (MD): " + getMaxDistance() + " \n");
		builder.append("Efektywna liczba bitów (ENOB): " + getEffectiveNumberOfBits(snr) + " \n");

		return builder.toString();
	}

	@Override
	public NoiseParam getNoiseParam() {
		return null;
	}

	private Double getMeanSquareError() {
		currentRespondingValueIndex = 0;
		Double mse = 0d;
		int numberOfPoints = 0;
		Double sumValDiffSquared = 0d;

		for (XYChart.Data<Double, Double> point : firstDataset) {
			Double respondingYValue = getRespondingValue(point.getXValue(), secondDataset);
			if (respondingYValue != null) {
				numberOfPoints++;
				sumValDiffSquared += Math.pow((point.getYValue() - respondingYValue), 2);
			}
		}

		if (numberOfPoints != 0) {
			mse = sumValDiffSquared / numberOfPoints;
		}
		return Precision.round(mse, 4);
	}

	private Double getSignalNoiseRatio() {
		currentRespondingValueIndex = 0;

		Double snr = 0d;

		Double sumValSquared = 0d;
		Double sumValDiffSquared = 0d;

		for (XYChart.Data<Double, Double> point : firstDataset) {
			Double respondingYValue = getRespondingValue(point.getXValue(), secondDataset);
			if (respondingYValue != null) {
				sumValSquared += Math.pow(point.getYValue(), 2);
				sumValDiffSquared += Math.pow((point.getYValue() - respondingYValue), 2);
			}
		}

		if (!sumValDiffSquared.equals(0d)) {
			snr = 10 * Math.log10(sumValSquared / sumValDiffSquared);
		}

		return Precision.round(snr, 4);
	}

	private Double getPeakSignalToNoiseRatio(Double meanSquareError) {
		currentRespondingValueIndex = 0;
		Double psnr = 0d;
		Double maxY = -Double.MAX_VALUE;

		for (XYChart.Data<Double, Double> point : firstDataset) {
			Double respondingYValue = getRespondingValue(point.getXValue(), secondDataset);
			if (respondingYValue != null) {
				if (maxY < point.getYValue()) {
					maxY = point.getYValue();
				}
			}
		}
		if (meanSquareError != null && !meanSquareError.equals(0d)) {
			psnr = 10 * Math.log10(maxY / meanSquareError);
		}
		return Precision.round(psnr, 4);
	}

	private Double getMaxDistance() {
		currentRespondingValueIndex = 0;

		Double maxDist = 0d;

		for (int i = 0; i < firstDataset.size(); i++) {
			Double respondingYValue = getRespondingValue(firstDataset.get(i).getXValue(), secondDataset);
			if (respondingYValue != null) {
				double dist = Math.abs(firstDataset.get(i).getYValue() - respondingYValue);
				if (dist > maxDist) {
					maxDist = dist;
				}
			}
		}
		return Precision.round(maxDist, 4);
	}

	private Double getEffectiveNumberOfBits(Double snr) {
		if (snr != null) {
			return Precision.round((snr - 1.76) / 6.02, 4);
		}
		return null;
	}

	private Double getRespondingValue(Double xValue, List<XYChart.Data<Double, Double>> dataset) {
		if (dataset.get(0).getXValue() > xValue || dataset.get(dataset.size() - 1).getXValue() < xValue) { //nie ma sensu szukać
			currentRespondingValueIndex = dataset.size();
			return null;
		}

		for (int i = currentRespondingValueIndex; i < dataset.size(); i++) {
			currentRespondingValueIndex = i;
			if (Math.abs(dataset.get(i).getXValue() - xValue) < epsilon) {
				return (dataset.get(i).getYValue());
			} else if (i != 0 && dataset.get(i).getXValue() >= xValue) {
				XYChart.Data<Double, Double> firstPoint = dataset.get(i);
				XYChart.Data<Double, Double> secondPoint = dataset.get(i - 1);

				double firstYValue = firstPoint.getYValue();
				double secondYValue = secondPoint.getYValue();

				double firstDist = firstPoint.getXValue() - xValue;
				double secondDist = xValue - secondPoint.getXValue();

				if (currentRespondingValueIndex == dataset.size() - 1) {
					currentRespondingValueIndex++; //nie chcemy zeby więcej porównywał
				}
				return ((secondYValue * 1 / secondDist) + (firstYValue * 1 / firstDist)) / (1 / firstDist + 1 / secondDist);
			}
		}
		return null;
	}


}
