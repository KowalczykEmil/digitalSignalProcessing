package model.operation.filter;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class HighPassFilter implements Filter {
    double epsilon = 0.000001d;

    /**
     * Filtr górnoprzepustowy- pasmo przepustowe leży w zakresie od f0 fo fp/2 (fp czestotliwosc prókowania)
     */
    public List<Double> execute(int rzadFiltru, double czestotliwoscOdciecia, double czestotliwoscProbkowania) {
        List<XYChart.Data<Double, Double>> dataset = new ArrayList<>();
        List<Double> amplitudes = new ArrayList<>();

        double K = czestotliwoscProbkowania / czestotliwoscOdciecia;
        double center = (rzadFiltru) / 2.0;

        for (int n = 0; n < rzadFiltru; n++) {
            double hn;
            if ((n < center + epsilon) && (n > center - epsilon)) {
                hn = 2 / K;
            } else {
                hn = Math.sin(2 * Math.PI * (n - center) / K) / (Math.PI * (n - center));
            }
            amplitudes.add(hn * Math.pow(-1.0,n));

//            if (n % 2 == 0) {
//                amplitudes.add(hn * Math.pow(1.0,n);
//            } else {
//                amplitudes.add(hn * -1.0);
//            }
        }
        return amplitudes;
    }

    @Override
    public String getName() {
        return "Filtr górnoprzepustowy";
    }
}
