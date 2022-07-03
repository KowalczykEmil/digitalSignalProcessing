package model.operation.filter;

import java.util.ArrayList;
import java.util.List;

public class LowPassFilter implements Filter {
    double epsilon = 0.000001d;

    /**
     * Filtr dolnoprzepustowy- pasmo przepustowe leży w zakresie od 0 do f0 (f0 - czestotliwosc odciecia filtru)
     */
    public List<Double> execute(int rzadFiltru, double czestotliwoscOdciecia, double czestotliwoscProbkowania) {
        List<Double> amplitudes = new ArrayList<>();

        double K = czestotliwoscProbkowania / czestotliwoscOdciecia;
        double center = (rzadFiltru) / 2.0;

        for (double i = 0; i < rzadFiltru + 1; i++) {
            double hn;
            if ((i < center + epsilon) && (i > center - epsilon)) {
                hn = 2 / K;
            } else {
                hn = Math.sin(2 * Math.PI * (i - center) / K) / (Math.PI * (i - center));
            }
            amplitudes.add(hn);
        }
        return amplitudes;
    }

    @Override
    public String getName() {
        return "Filtr dolnoprzepustowy";
    }

}
