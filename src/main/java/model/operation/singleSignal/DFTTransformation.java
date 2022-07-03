package model.operation.singleSignal;

import addons.OperationException;
import javafx.scene.chart.*;
import model.NoiseParam;
import model.signal.Signal;
import org.apache.commons.math3.complex.Complex;

import java.util.ArrayList;
import java.util.List;

public class DFTTransformation implements SingleSignalOperation {
    private List<Complex> complexInput = new ArrayList<>();
    private List<Complex> complexOutput = new ArrayList<>();
    private Signal signal;
    private NoiseParam noiseParam;
    @Override
    public Signal execute(Signal signal, SingleSignalOperationParam param) throws OperationException {


        return null;
    }

    @Override
    public String getName() {
        return "DFT";
    }

    private void toComplex(Signal signal) {
        for (int i = 0; i < signal.getDataset().size(); i++) {
            complexInput.add(new Complex(signal.getDataset().get(i).getYValue()));
        }
    }

    public List<Complex> transform(Signal signal) {
        this.signal = signal;
        this.noiseParam=signal.getNoiseParam();
        toComplex(signal);
        int N = complexInput.size();

        Complex W = new Complex(Math.cos(2.0 * Math.PI / N), Math.sin(2.0 * Math.PI / N));

        for (int m = 0; m < N; m++) {
            Complex sum = new Complex(0.0);
            for (int n = 0; n < N; n++) {
                sum = sum.add(complexInput.get(n).multiply(W.pow(-m * n)));
            }
            complexOutput.add(sum.divide(N));
        }
        return complexOutput;
    }

    public List<XYChart.Data<Double, Double>> reverseTransform(List<XYChart.Data<Double, Double>> dataset, List<Complex> complexSet, Signal signal) {
        List<XYChart.Data<Double, Double>> output = new ArrayList<>();
        this.signal=signal;
        this.noiseParam=signal.getNoiseParam();
        int N = complexSet.size();
        Complex W = new Complex(Math.cos(2.0 * Math.PI / N), Math.sin(2.0 * Math.PI / N));

        for (int m = 0; m < N; m++) {
            Complex sum = new Complex(0.0);
            for (int n = 0; n < N; n++) {
                sum = sum.add(complexSet.get(n).multiply(W.pow(m * n)));
            }
            output.add(new XYChart.Data<>(dataset.get(m).getXValue(), sum.getReal()));
        }

        return output;
    }

    public XYChart getRealValuesChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        Double f = noiseParam.getFpr()/complexOutput.size();

        for (int i = 0; i < complexOutput.size(); i++) {
           // series.getData().add(new XYChart.Data(signal.getDataset().get(i).getXValue(), complexOutput.get(i).getReal()));
            series.getData().add(new XYChart.Data(i*f, complexOutput.get(i).getReal()));
        }

        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);
        lineChart.setTitle("Część rzeczywista");
        return lineChart;
    }

    public XYChart getImgValuesChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        Double f = noiseParam.getFpr()/complexOutput.size();
        for (int i = 0; i < complexOutput.size(); i++) {
            series.getData().add(new XYChart.Data(i*f, complexOutput.get(i).getImaginary()));
        }

        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);
        lineChart.setTitle("Część urojona");
        return lineChart;
    }

    public XYChart getAbsComplexChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        Double f = noiseParam.getFpr()/complexOutput.size();
        for (int i = 0; i < complexOutput.size(); i++) {
            series.getData().add(new XYChart.Data(i*f, complexOutput.get(i).abs()));
        }

        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);
        lineChart.setTitle("Moduł liczby zespolonej");
        return lineChart;
    }

    public XYChart getArgComplexChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        final LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        XYChart.Series series = new XYChart.Series();
        Double f = noiseParam.getFpr()/complexOutput.size();
        for (int i = 0; i < complexOutput.size(); i++) {
            series.getData().add(new XYChart.Data(i*f, complexOutput.get(i).getArgument()));
        }

        lineChart.getData().add(series);
        lineChart.setCreateSymbols(false);
        lineChart.setTitle("Argument liczby zespolonej");
        return lineChart;
    }

    public Signal getSignal() {
        return signal;
    }

    public List<Complex> getComplexOutput() {
        return complexOutput;
    }
}
