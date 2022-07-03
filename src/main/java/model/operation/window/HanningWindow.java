package model.operation.window;

import javafx.scene.chart.XYChart;
import model.NoiseParam;
import model.operation.filter.Filter;
import model.signal.DiscreteSignal;
import model.signal.Signal;

import java.util.ArrayList;
import java.util.List;

public class HanningWindow implements Window {

	public Signal execute(Filter filter, int rzadFiltru, double czestotliwoscOdciecia, double czestotliwoscProbkowania) {
		List<XYChart.Data<Double, Double>> dataset = new ArrayList<>();
		List<Double> filterValue = filter.execute(rzadFiltru, czestotliwoscOdciecia, czestotliwoscProbkowania);

		double xValue = 0;
		for (int i = 0; i < filterValue.size(); i++) {
			double hanning = 0.5 -  0.5 * Math.cos(2 * Math.PI * i / rzadFiltru);
			dataset.add(new XYChart.Data(xValue, filterValue.get(i) * hanning));
			xValue ++;
		}

		Signal outputSignal = new DiscreteSignal();
		NoiseParam parmas = new NoiseParam();
		parmas.setDuration(filterValue.size() * 1.0);
		parmas.setInitialTime(0.0);
		parmas.setSamplingPeriod(1.0);
		outputSignal.generateSignal(parmas, dataset);
		return outputSignal;
	}

	@Override
	public String getName() {
		return "Okno Hanninga";
	}


}
