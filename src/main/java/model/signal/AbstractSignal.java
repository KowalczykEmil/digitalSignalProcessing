package model.signal;

import javafx.scene.chart.*;
import model.NoiseParam;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSignal implements Signal {
    private final int UNIT_STEP = 1;
    private final double GRAPH_MARGIN = 1.1;
    private final int ROUND_DIGITS = 2;
    private final int CHART_POINT_FILTER = 10000;
    protected List<XYChart.Data<Double, Double>> dataset;
    private XYChart graphChart;
    private int intervals = 5;
    private NoiseParam noiseParam;


    public void generateSignal(NoiseParam params, List<XYChart.Data<Double, Double>> dataset) {
        this.dataset = dataset;
        refreshCharts();
        this.noiseParam = params;
    }

    private void refreshCharts() {
        prepareChart();
        bindDataToGraphChart(dataset);
    }

    private void prepareChart() {
        NumberAxis xAxis;
        if (dataset != null && !dataset.isEmpty()) {
            double min = dataset.get(0).getXValue();
            double max = dataset.get(dataset.size() - 1).getXValue();
            double unitStep = Math.abs((max - min) / 20);
            xAxis = new NumberAxis(min - unitStep / 2, max + unitStep / 2, unitStep);
        } else {
            xAxis = new NumberAxis();
        }
        final NumberAxis yAxis = new NumberAxis();
        graphChart = getNewChart(xAxis, yAxis);
        graphChart.setLegendVisible(false);
    }

    private void bindDataToGraphChart(List<XYChart.Data<Double, Double>> dataset) {
        XYChart.Series fSeries = new XYChart.Series();
        dataset = filterPoints(dataset);

        fSeries.nameProperty().unbind();
        fSeries.getData().addAll(dataset);
        graphChart.getData().addAll(fSeries);
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
                XYChart.Data<Double, Double> newPoint = new XYChart.Data<Double, Double>(point.getXValue(),point.getYValue());
                output.add(newPoint);
            }
        }
        return output;
    }

    protected XYChart getNewChart(NumberAxis xAxis, NumberAxis yAxis) {
        return null;
    }

    @Override
    public Chart getChart() {
        return graphChart;
    }

    public Double getAverageValue() {
        double avg;
        double sumValues = 0;
        int numberOfPoints = 0;

        if (noiseParam.getBasePeriod() != null && noiseParam.getBasePeriod() != 0) {
            int numberOfWholePeriods = (int) (noiseParam.getDuration() / noiseParam.getBasePeriod());
            double maxXValue = noiseParam.getInitialTime() + numberOfWholePeriods * noiseParam.getBasePeriod();
            for (var data : dataset) {
                if (data.getXValue() <= maxXValue) {
                    sumValues += data.getYValue();
                    numberOfPoints++;
                } else {
                    break;
                }
            }
        } else {
            for (var data : dataset) {
                sumValues += data.getYValue();
                numberOfPoints++;
            }
        }

        avg = (1.0 / numberOfPoints) * sumValues;

        return Precision.round(avg, 4);
    }

    public Double getAbsoluteAverageValue() {
        double avg;
        double sumValues = 0;
        int numberOfPoints = 0;

        if (noiseParam.getBasePeriod() != null && noiseParam.getBasePeriod() != 0) {
            int numberOfWholePeriods = (int) (noiseParam.getDuration() / noiseParam.getBasePeriod());
            double maxXValue = noiseParam.getInitialTime() + numberOfWholePeriods * noiseParam.getBasePeriod();
            for (var data : dataset) {
                if (data.getXValue() <= maxXValue) {
                    sumValues += Math.abs(data.getYValue());
                    numberOfPoints++;
                } else {
                    break;
                }
            }
        } else {
            for (var data : dataset) {
                sumValues += Math.abs(data.getYValue());
                numberOfPoints++;
            }
        }
        avg = (1.0 / numberOfPoints) * sumValues;

        return Precision.round(avg, 4);
    }

    //wartosc skuteczna
    public Double getRMS() {
        return Precision.round(Math.sqrt(getAvgPowers()), 4);
    }

    public Double getVariance() {
        double variance;
        double sumValues = 0;
        double avg = getAverageValue();
        int numberOfPoints = 0;

        if (noiseParam.getBasePeriod() != null && noiseParam.getBasePeriod() != 0) {
            int numberOfWholePeriods = (int) (noiseParam.getDuration() / noiseParam.getBasePeriod());
            double maxXValue = noiseParam.getInitialTime() + numberOfWholePeriods * noiseParam.getBasePeriod();
            for (var data : dataset) {
                if (data.getXValue() <= maxXValue) {
                    sumValues += Math.pow(data.getYValue() - avg, 2);
                    numberOfPoints++;
                } else {
                    break;
                }
            }
        } else {
            for (var data : dataset) {
                sumValues += Math.pow(data.getYValue() - avg, 2);
                numberOfPoints++;
            }
        }
        variance = (1.0 / numberOfPoints) * sumValues;

        return Precision.round(variance, 4);
    }

    public Double getAvgPowers() {
        double avgPower;
        double sumValues = 0;
        int numberOfPoints = 0;

        if (noiseParam.getBasePeriod() != null && noiseParam.getBasePeriod() != 0) {
            int numberOfWholePeriods = (int) (noiseParam.getDuration() / noiseParam.getBasePeriod());
            double maxXValue = noiseParam.getInitialTime() + numberOfWholePeriods * noiseParam.getBasePeriod();
            for (var data : dataset) {
                if (data.getXValue() <= maxXValue) {
                    sumValues += Math.pow(data.getYValue(), 2);
                    numberOfPoints++;
                } else {
                    break;
                }
            }
        } else {
            for (var data : dataset) {
                sumValues += Math.pow(data.getYValue(), 2);
                numberOfPoints++;
            }
        }
        avgPower = (1.0 / numberOfPoints) * sumValues;

        return Precision.round(avgPower, 4);
    }

    @Override
    public String getCharacteristics() {
        long startTime = System.nanoTime();

        StringBuilder output = new StringBuilder();

        output.append("Warto???? ??rednia sygna??u: " + getAverageValue() + "\n");
        System.out.println("Warto???? ??rednia sygna??u: " + (System.nanoTime() - startTime) / 100000);
        startTime = System.nanoTime();

        output.append("Warto???? ??rednia bezwzgl??dna: " + getAbsoluteAverageValue() + "\n");
        System.out.println("Warto???? ??rednia bezwzgl??dna: " + (System.nanoTime() - startTime) / 100000);
        startTime = System.nanoTime();

        output.append("Moc ??rednia sygna??u: " + getAvgPowers() + "\n");
        System.out.println("Moc ??rednia sygna??u: " + (System.nanoTime() - startTime) / 100000);
        startTime = System.nanoTime();

        output.append("Wariancja sygna??u: " + getVariance() + "\n");
        System.out.println("Wariancja sygna??u: " + (System.nanoTime() - startTime) / 100000);
        startTime = System.nanoTime();

        output.append("Warto???? skuteczna: " + getRMS() + "\n");
        System.out.println("Warto???? skuteczna: " + (System.nanoTime() - startTime) / 100000);

        return output.toString();
    }

    @Override
    public StackedBarChart getHistogram() {
        return generateBarChart();
    }

    /**
     * Wyznacza najmniejsz?? warto???? Y z listy punkt??w (X,Y)
     *
     * @return Najmniejsza warto???? Y z podanej listy
     */
    protected double getMinFromDataset() {
        double min = 0;
        for (int i = 0; i < dataset.size(); i++) {
            if (dataset.get(i).getYValue() < min) {
                min = dataset.get(i).getYValue();
            }
        }
        return min;
    }

    /**
     * Wyznacza najwi??ksz?? warto???? Y z listy punkt??w (X,Y)
     *
     * @return Najwi??ksza warto???? Y z podanej listy
     */
    protected double getMaxFromDataset() {
        double max = 0;
        for (int i = 0; i < dataset.size(); i++) {
            if (dataset.get(i).getYValue().doubleValue() > max) {
                max = dataset.get(i).getYValue().doubleValue();
            }
        }
        return max;
    }

    /**
     * Generuje serie danych dla wykresu s??upkowego
     *
     * @param start          Pocz??tek przedzia??u dla kt??rego ma by?? wyznaczona seria danych
     * @param end            Koniec przedzia??u dla kt??rego ma by?? wyznaczona seria danych
     * @param intervalNumber Numer przedzia??u
     * @return
     */
    private XYChart.Series generateBarChartSeries(double start, double end, int intervalNumber, double maxValue) {
        XYChart.Series series = new XYChart.Series();
        int valueCounter = 0;
        double maxXValue = 0;
        if (noiseParam.getBasePeriod() != null) {
            int numberOfWholePeriods = (int) (noiseParam.getDuration() / noiseParam.getBasePeriod());
            maxXValue = noiseParam.getInitialTime() + numberOfWholePeriods * noiseParam.getBasePeriod();
        } else {
            maxXValue=dataset.get(dataset.size()-1).getXValue();
        }

        for (XYChart.Data<Double, Double> doubleDoubleData : dataset) {
            if (doubleDoubleData.getXValue() > maxXValue) {
                break;
            }
            if (intervalNumber < intervals && ((doubleDoubleData.getYValue() >= start && doubleDoubleData.getYValue() < end))) {
                valueCounter++;
            } else if (intervalNumber == intervals && (doubleDoubleData.getYValue() >= start && doubleDoubleData.getYValue() <= maxValue)) {
                valueCounter++;
            }
        }
        series.getData().add(new XYChart.Data(("(" + Precision.round(start, ROUND_DIGITS) + ";" + Precision.round(end, ROUND_DIGITS) + ")"), valueCounter));

        return series;
    }

    /**
     * Generuje wykres s??upkowy wykorzystywany jako histogram
     *
     * @return Gotowy do wyrenderowania wykres
     */
    private StackedBarChart generateBarChart() {

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final StackedBarChart<String, Number> bc =
                new StackedBarChart<String, Number>(xAxis, yAxis);

        double minValue = getMinFromDataset();
        double maxValue = getMaxFromDataset();

        double intervalWidth = (maxValue - minValue) / intervals;
        double start = minValue;
        double end = minValue + intervalWidth;

        for (int i = 1; i <= intervals; i++) {
            bc.getData().add(generateBarChartSeries(start, end, i, maxValue));
            start = end;
            end = end + intervalWidth;
        }
        return bc;
    }

    @Override
    public List<XYChart.Data<Double, Double>> getDataset() {
        return dataset;
    }

    @Override
    public void setDataset(List<XYChart.Data<Double, Double>> dataset) {
        this.dataset = dataset;
        this.noiseParam = getParamFromDataset();
        refreshCharts();
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
        params.setAmplitude(Math.max(Math.abs(yMax), Math.abs(yMin)));
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
