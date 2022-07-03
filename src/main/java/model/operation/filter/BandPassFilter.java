package model.operation.filter;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class BandPassFilter implements Filter {

	/**
	 * Filtr pasmowy- pasmo przepustowe leży w zakresie od fd do fg (fg>0, df<fg i fd,fg <fp/2)
	 */

	public List<Double> execute(int rzadFiltru, double czestotliwoscOdciecia, double czestotliwoscProbkowania) {
		List<XYChart.Data<Double, Double>> dataset = new ArrayList<>();
		List<Double> amplitudes = new ArrayList<>();

		double K = czestotliwoscProbkowania / czestotliwoscOdciecia;
		int center = (rzadFiltru ) / 2;


		for (int i = 0; i < rzadFiltru; i++) {
			double hn;
			if (i == center) {
				hn = 2 / K;
			} else {
				hn = Math.sin(2 * Math.PI * (i - center) / K) / (Math.PI * (i - center));
			}

			hn = hn * 2 * Math.sin(Math.PI * i / 2);
			amplitudes.add(hn);
		}
		return amplitudes;
	}

	@Override
	public String getName() {
		return "Filtr pasmowy";
	}

}
