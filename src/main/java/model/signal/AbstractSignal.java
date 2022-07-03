package model.signal;

import javafx.scene.chart.*;
import model.NoiseParam;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSignal implements Signal {
    private final int UNIT_STEP = 1;
    private final double GRAPH_MARGIN = 1.1;
    protected List<XYChart.Data<Double, Double>> dataset;
    private XYChart graphChart;
    private int intervals = 5;
    private NoiseParam noiseParam;

    public void generateSignal(NoiseParam params, List<XYChart.Data<Double, Double>> dataset) {
        this.dataset = dataset;
        long startTime = System.nanoTime();
        refreshCharts(params);
        System.out.println("CHART: " + ( System.nanoTime() - startTime)/100000);

        this.noiseParam = params;
    }

    private void refreshCharts(NoiseParam params) {
        prepareChart(params);
        bindDataToGraphChart(dataset);
    }

    protected void prepareChart(NoiseParam params) {
        double xFrom = params.getInitialTime();
        double xTo = params.getInitialTime() + params.getDuration();
        double amplitude = params.getAmplitude();

        NumberAxis xAxis = new NumberAxis(xFrom, xTo, (xTo - xFrom) / 20);
        NumberAxis yAxis = new NumberAxis((-amplitude) * GRAPH_MARGIN, amplitude * GRAPH_MARGIN, UNIT_STEP);
        graphChart = getNewChart(xAxis, yAxis);
        graphChart.setLegendVisible(false);
    }

    private void bindDataToGraphChart(List<XYChart.Data<Double, Double>> dataset) {
        XYChart.Series<Double, Double> fSeries = new XYChart.Series<>();
        if (dataset.size() > 10000) {
            dataset = filterPoints(dataset);
        }

        fSeries.nameProperty().unbind();
        fSeries.getData().addAll(dataset);
        graphChart.getData().addAll(fSeries);
    }

    private List<XYChart.Data<Double, Double>> filterPoints(List<XYChart.Data<Double, Double>> dataset){
        List<XYChart.Data<Double, Double>> filteredDataset = new ArrayList<>();
        int frequencyFactor = dataset.size() / 10000;
        int i = 0;
        for (XYChart.Data<Double, Double> point : dataset) {
            if (i > frequencyFactor) {
                filteredDataset.add(point);
                i=0;
            }
            i++;
        }
        return filteredDataset;
    }

    protected XYChart getNewChart(NumberAxis xAxis, NumberAxis yAxis) {
        return null;
    }

    @Override
    public Chart getChart() {
        return graphChart;
    }

    @Override
    public Double getAverageValue() {

        double avg;
        double sumValues = 0;

        if (noiseParam.getBasePeriod() != null && noiseParam.getDuration() % noiseParam.getBasePeriod() != 0) {
            double maxArg = noiseParam.getDuration() - (noiseParam.getBasePeriod() - 1);
            for (var data : dataset) {
                if (data.getXValue() <= maxArg) {
                    sumValues += data.getYValue();
                }
            }
        } else {
            for (var data : dataset) {
                sumValues += data.getYValue();
            }
        }

        avg = (1.0 / dataset.size()) * sumValues;

        return Precision.round(avg, 4);
    }

    @Override
    public Double getAbsoluteAverageValue() {
        double avg;
        double sumValues = 0;

        if (noiseParam.getBasePeriod() != null && noiseParam.getDuration() % noiseParam.getBasePeriod() != 0) {
            double maxArg = noiseParam.getDuration() - (noiseParam.getBasePeriod() - 1);
            for (var data : dataset) {
                if (data.getXValue() <= maxArg) {
                    sumValues += Math.abs(data.getYValue());
                }
            }
        } else {
            for (var data : dataset) {
                sumValues += Math.abs(data.getYValue());
            }
        }
        avg = (1.0 / dataset.size()) * sumValues;

        return Precision.round(avg, 4);
    }

    @Override
    public Double getWartoscSkuteczna() {
        return Precision.round(Math.sqrt(getAvgPowers()), 4);
    }

    @Override
    public Double getVariances() {
        double variance;
        double sumValues = 0;
        double avg = getAverageValue();

        if (noiseParam.getBasePeriod() != null && noiseParam.getDuration() % noiseParam.getBasePeriod() != 0) {
            double maxArg = noiseParam.getDuration() - (noiseParam.getBasePeriod() - 1);
            for (var data : dataset) {
                if (data.getXValue() <= maxArg) {
                    sumValues += Math.pow(data.getYValue() - avg, 2);
                }
            }
        } else {
            for (var data : dataset) {
                sumValues += Math.pow(data.getYValue() - avg, 2);
            }
        }
        variance = (1.0 / dataset.size()) * sumValues;

        return Precision.round(variance, 4);
    }

    @Override
    public Double getAvgPowers() {
        double avgPower;
        double sumValues = 0;

        if (noiseParam.getBasePeriod() != null && noiseParam.getDuration() % noiseParam.getBasePeriod() != 0) {
            double maxArg = noiseParam.getDuration() - (noiseParam.getBasePeriod() - 1);
            for (var data : dataset) {
                if (data.getXValue() <= maxArg) {
                    sumValues += Math.pow(data.getYValue(), 2);
                }
            }
        } else {
            for (var data : dataset) {
                sumValues += Math.pow(data.getYValue(), 2);
            }
        }
        avgPower = (1.0 / dataset.size()) * sumValues;

        return Precision.round(avgPower, 4);
    }

    @Override
    public String getCharacteristics() {
        long startTime = System.nanoTime();

        StringBuilder output = new StringBuilder();

        output.append("Wartość średnia sygnału: " + getAverageValue() + "\n");
        System.out.println("Wartość średnia sygnału: " + (System.nanoTime() - startTime)/100000);
        startTime = System.nanoTime();

        output.append("Wartość średnia bezwzględna: " + getAbsoluteAverageValue() + "\n");
        System.out.println("Wartość średnia bezwzględna: " + (System.nanoTime() - startTime)/100000);
        startTime = System.nanoTime();

        output.append("Moc średnia sygnału: " + getAvgPowers() + "\n");
        System.out.println("Moc średnia sygnału: " + (System.nanoTime() - startTime)/100000);
        startTime = System.nanoTime();

        output.append("Wariancja sygnału: " + getVariances() + "\n");
        System.out.println("Wariancja sygnału: " + (System.nanoTime() - startTime)/100000);
        startTime = System.nanoTime();

        output.append("Wartość skuteczna: " + getWartoscSkuteczna() + "\n");
        System.out.println("Wartość skuteczna: " + (System.nanoTime() - startTime)/100000);

        return output.toString();
    }

    @Override
    public StackedBarChart getHistogram() {
        return generateBarChart();
    }


    protected double getMinFromDataset() {          // Wyznacza najmniejszą wartość Y z datasetu (X,Y)
        double min = 0;
        for (int i = 0; i < dataset.size(); i++) {
            if (dataset.get(i).getYValue().doubleValue() < min) {
                min = dataset.get(i).getYValue().doubleValue();
            }
        }
        return min;                                 // Najmniejsza wartość Y z danego datasetu
    }


    protected double getMaxFromDataset() {          // Wyznacza największą wartość Y z datasetu (X,Y)
        double max = 0;
        for (int i = 0; i < dataset.size(); i++) {
            if (dataset.get(i).getYValue().doubleValue() > max) {
                max = dataset.get(i).getYValue().doubleValue();
            }
        }
        return max;                                 // Największa wartość Y z danego datasetu
    }

    /*
     * Generuje serie danych dla wykresu słupkowego
     *
     * start                Początek przedziału dla którego ma być wyznaczona seria danych
     * end                  Koniec przedziału dla którego ma być wyznaczona seria danych
     * intervalNumber       Numer przedziału
     * param limitArgs      Ograniczenie ilości argumentów, true -> czas trwania sygnału nie jest wielokrotnością wykresu
     * return
     */

    protected XYChart.Series generateBarChartSeries(double start, double end, int intervalNumber, boolean limitArgs, double maxValue) {
        XYChart.Series series = new XYChart.Series();
        int numOfData = dataset.size();
        int roundDigits = 2;
        int valueCounter = 0;

        if (limitArgs) {
            double maxArg = noiseParam.getDuration() - (noiseParam.getBasePeriod() - 1);
            for (int j = 0; j < numOfData; j++) {
                if (intervalNumber < intervals && dataset.get(j).getXValue() <= maxArg && ((dataset.get(j).getYValue().doubleValue() >= start && dataset.get(j).getYValue().doubleValue() < end))) {
                    valueCounter++;
                } else if (intervalNumber == intervals && dataset.get(j).getXValue() <= maxArg && (dataset.get(j).getYValue().doubleValue() >= start && dataset.get(j).getYValue().doubleValue() <= maxValue)) {
                    valueCounter++;
                }
            }
        } else {
            for (int j = 0; j < numOfData; j++) {
                if (!limitArgs && intervalNumber < intervals && ((dataset.get(j).getYValue().doubleValue() >= start && dataset.get(j).getYValue().doubleValue() < end))) {
                    valueCounter++;
                } else if (!limitArgs && intervalNumber == intervals && (dataset.get(j).getYValue().doubleValue() >= start && dataset.get(j).getYValue().doubleValue() <= maxValue)) {
                    valueCounter++;
                }
            }
        }
        series.getData().add(new XYChart.Data(("(" + Precision.round(start, roundDigits) + ";" + Precision.round(end, roundDigits) + ")"), valueCounter));

        return series;
    }


    protected StackedBarChart generateBarChart() {              // Generuje wykres słupkowy -> Histogram

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final StackedBarChart<String, Number> bc =
                new StackedBarChart<String, Number>(xAxis, yAxis);

        double minValue = getMinFromDataset();
        double maxValue = getMaxFromDataset();

        double intervalWidth = (maxValue - minValue) / intervals;
        double start = minValue;
        double end = minValue + intervalWidth;

        boolean limitArgs = false;
        if (noiseParam.getBasePeriod() != null && (noiseParam.getDuration() % noiseParam.getBasePeriod() != 0)) {
            limitArgs = true;
        }

        for (int i = 1; i <= intervals; i++) {
            bc.getData().add(generateBarChartSeries(start, end, i, limitArgs, maxValue));
            start = end;
            end = end + intervalWidth;
        }
        return bc;                  // Gotowy wykres do narysowania
    }

    @Override
    public List<XYChart.Data<Double, Double>> getDataset() {
        return dataset;
    }

    @Override
    public void setDataset(List<XYChart.Data<Double, Double>> dataset) {
        this.dataset = dataset;
        this.noiseParam  = getParamFromDataset();
        refreshCharts(this.noiseParam);
    }

    private NoiseParam getParamFromDataset() {
        NoiseParam params = new NoiseParam();
        Double xMin = 1000D;
        Double xMax = -1000D;
        Double yMin = 1000D;
        Double yMax = -1000D;

        for (XYChart.Data<Double, Double> point : dataset) {
            if (point.getXValue() < xMin) {
                xMin = point.getXValue();
            }
            if (point.getXValue() > xMax) {
                xMax = point.getXValue();
            }
            if (point.getYValue() < yMin) {
                yMin = point.getYValue();
            }
            if (point.getYValue() > yMax) {
                yMax = point.getYValue();
            }
        }

        params.setDuration(xMax - xMin);
        params.setInitialTime(xMin);
        params.setAmplitude(yMax);
        return params;
    }

    @Override
    public int getIntervals() {
        return intervals;
    }

    @Override
    public void setIntervals(int intervals) {
        this.intervals = intervals;
    }

    public NoiseParam getNoiseParam() {
        return noiseParam;
    }
}
