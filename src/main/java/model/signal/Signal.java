package model.signal;

import javafx.scene.chart.Chart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableView;
import model.NoiseParam;

import java.util.List;

public interface Signal {

	public void generateSignal(NoiseParam params, List<XYChart.Data<Double, Double>> dataset);

	public Double getAverageValue();

	public Double getAbsoluteAverageValue();

	public Double getWartoscSkuteczna();

	public Double getVariances();

	public Double getAvgPowers();

	public Chart getChart();

	public StackedBarChart getHistogram();

	public List<XYChart.Data<Double, Double>> getDataset();

	public void setDataset(List<XYChart.Data<Double, Double>> dataset);

	public int getIntervals();

	public void setIntervals(int intervals);

	public String getCharacteristics();

	public NoiseParam getNoiseParam();

}
