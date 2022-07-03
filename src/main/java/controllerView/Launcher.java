package controllerView;

import addons.DouglasPeuckerAlg;
import addons.RecordAddon;
import addons.TabPanel;
import exceptions.ActionException;
import exceptions.Announcements;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import loader.FileLoader;
import model.ArithmeticType;
import model.Stats;
import signals.*;
import stageCore.StageController;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static addons.ChartAddon.*;

public class Launcher {

    /*------------------------ FIELDS REGION ------------------------*/
    private ComboBox comboBoxSignalTypes;
    private ComboBox comboBoxOperationTypes;
    private ComboBox comboBoxFirstSignal;
    private ComboBox comboBoxSecondSignal;

    private TextField textFieldAmplitude;
    private TextField textFieldStartTime;
    private TextField textFieldSignalDuration;
    private TextField textFieldBasicPeriod;
    private TextField textFieldFillFactor;
    private TextField textFieldJumpTime;
    private TextField textFieldProbability;
    private TextField textFieldSamplingFrequency;

    private TabPane tabPaneResults;
    private Spinner spinnerHistogramRange;

    private Map<Integer, Signal> signals = new HashMap<>();
    private FileLoader<Signal> signalFileReader;
    private boolean isScatterChart;

    /*------------------------ METHODS REGION ------------------------*/
    public Launcher(ComboBox comboBoxSignalTypes, ComboBox comboBoxOperationTypes,
                    ComboBox comboBoxFirstSignal, ComboBox comboBoxSecondSignal,
                    TextField textFieldAmplitude, TextField textFieldStartTime,
                    TextField textFieldSignalDuration, TextField textFieldBasicPeriod,
                    TextField textFieldFillFactor, TextField textFieldJumpTime,
                    TextField textFieldProbability, TextField textFieldSamplingFrequency,
                    TabPane tabPaneResults, Spinner spinnerHistogramRange) {
        this.comboBoxSignalTypes = comboBoxSignalTypes;
        this.comboBoxOperationTypes = comboBoxOperationTypes;
        this.comboBoxFirstSignal = comboBoxFirstSignal;
        this.comboBoxSecondSignal = comboBoxSecondSignal;
        this.textFieldAmplitude = textFieldAmplitude;
        this.textFieldStartTime = textFieldStartTime;
        this.textFieldSignalDuration = textFieldSignalDuration;
        this.textFieldBasicPeriod = textFieldBasicPeriod;
        this.textFieldFillFactor = textFieldFillFactor;
        this.textFieldJumpTime = textFieldJumpTime;
        this.textFieldProbability = textFieldProbability;
        this.textFieldSamplingFrequency = textFieldSamplingFrequency;
        this.tabPaneResults = tabPaneResults;
        this.spinnerHistogramRange = spinnerHistogramRange;
    }

    private void fillParamsTab(TabPanel tabPanel, double[] signalParams) {
        Pane pane = (Pane) tabPanel.getParameters().getContent();
        List<Node> paneChildren = pane.getChildren();

        DecimalFormat df = new DecimalFormat("##.####");
        appendLabelText(paneChildren.get(0), "" + df.format(signalParams[0]));
        appendLabelText(paneChildren.get(1), "" + df.format(signalParams[1]));
        appendLabelText(paneChildren.get(2), "" + df.format(signalParams[2]));
        appendLabelText(paneChildren.get(3), "" + df.format(signalParams[3]));
        appendLabelText(paneChildren.get(4), "" + df.format(signalParams[4]));
    }

    private void fillCustomTabPaneWithData(TabPane tabPane,
                                           Collection<RecordAddon<Number, Number>> chartCollection,
                                           Collection<RecordAddon<String, Number>> barChartCollection,
                                           double[] signalParams) {
        TabPanel tabPanel = castTabPaneToCustomTabPane(tabPane);

        if (isScatterChart) {
            fillScatterChart(tabPanel, chartCollection);
        } else {
            fillLineChart(tabPanel, chartCollection);
        }
        fillBarChart(tabPanel, barChartCollection);
        fillParamsTab(tabPanel, signalParams);
    }

    private void convertSignalToChart(Signal signal) {
        /* prepare line/point chart data */
        List<Stats> data;
        if(signal instanceof GaussianNoiseSignal || signal instanceof UniformNoise){
            data = new ArrayList<>();
            for(int i = 0; i < signal.getData().size(); i++){
                if(i % (signal.getData().size() / 1000) == 0)
                    data.add(signal.getData().get(i));
            }
        }else if(signal instanceof ContinousSignal || signal instanceof FinalSignal){
            DouglasPeuckerAlg douglasPeucker = new DouglasPeuckerAlg();
            data = signal.getData();
            data = new ArrayList<>(douglasPeucker.calculate(
                    data,
                    (data.get(data.size() - 1).getX() - data.get(0).getX()) * 1.0 / 10000.0,
                    0,
                    data.size() - 1));
        }else{
            data = signal.getData();
        }
        System.out.println("Wygenerowanu punktów: " + data.size());
        List<RecordAddon<Number, Number>> chartData = data.stream()
                .map(d -> new RecordAddon<Number, Number>(d.getX(), d.getY()))
                .collect(Collectors.toList());

        /* prepare barchart data */
        DecimalFormat df = new DecimalFormat("#.##");
        List<RecordAddon<String, Number>> histogramData = signal
                .generateHistogram((int) spinnerHistogramRange.getValue())
                .stream()
                .map(range -> new RecordAddon<String, Number>(
                        df.format(range.getStart()) + " do " + df.format(range.getEnd()),
                        range.getAmount()))
                .collect(Collectors.toList());

        /* prepare params */
        double[] signalParams = new double[5];
        signalParams[0] = signal.meanValue();
        signalParams[1] = signal.absMeanValue();
        signalParams[2] = signal.rmsValue();
        signalParams[3] = signal.varianceValue();
        signalParams[4] = signal.meanPowerValue();

        /* render it all */
        fillCustomTabPaneWithData(tabPaneResults, chartData, histogramData, signalParams);
    }

    private void representSignal(Signal signal) {

        /* remember signal */
        int tabIndex = tabPaneResults.getSelectionModel().getSelectedIndex();
        signals.put(tabIndex, signal);

        /* generate signal */
        signal.generate();

        convertSignalToChart(signal);
    }

    public void computeCharts() {
        String selectedSignal = comboBoxSignalTypes.getSelectionModel()
                .getSelectedItem().toString();

        Double amplitude = null;
        Double rangeStart = null;
        Double rangeLength = null;
        Double term = null;
        Double fulfillment = null;
        Double jumpMoment = null;
        Double probability = null;
        Double sampleRate = null;

        try {
            amplitude = Double.parseDouble(textFieldAmplitude.getText());
            rangeStart = Double.parseDouble(textFieldStartTime.getText());
            rangeLength = Double.parseDouble(textFieldSignalDuration.getText());
            term = Double.parseDouble(textFieldBasicPeriod.getText());
            fulfillment = Double.parseDouble(textFieldFillFactor.getText());
            jumpMoment = Double.parseDouble(textFieldJumpTime.getText());
            probability = Double.parseDouble(textFieldProbability.getText());
            sampleRate = Double.parseDouble(textFieldSamplingFrequency.getText());

            Signal signal = null;
            isScatterChart = false;
            changeScatterChartToLineChart(tabPaneResults,
                    new LineChart<>(new NumberAxis(), new NumberAxis()));

            if (selectedSignal.equals("szum o rozkładzie jednostajnym")) {

                signal = new UniformNoise(rangeStart, rangeLength, amplitude);

            } else if (selectedSignal.equals("szum gaussowski")) {

                signal = new GaussianNoiseSignal(rangeStart, rangeLength, amplitude);

            } else if (selectedSignal.equals("sygnał sinusoidalny")) {

                signal = new SinusoidalSignal(rangeStart, rangeLength, amplitude, term);

            } else if (selectedSignal.equals("sygnał sinusoidalny wyprostowany jednopołówkowo")) {

                signal = new SinusoidalRectifiedOneHalfSignal(rangeStart, rangeLength,
                        amplitude, term);

            } else if (selectedSignal.equals("sygnał sinusoidalny wyprostowany dwupołówkowo")) {

                signal = new SinusoidalRectifiedTwoHalfSignal(rangeStart, rangeLength,
                        amplitude, term);

            } else if (selectedSignal.equals("sygnał prostokątny")) {

                signal = new RectangularSignal(rangeStart, rangeLength, amplitude, term,
                        fulfillment);

            } else if (selectedSignal.equals("sygnał prostokątny symetryczny")) {

                signal = new RectangularSymmetricSignal(rangeStart, rangeLength, amplitude,
                        term, fulfillment);

            } else if (selectedSignal.equals("sygnał trójkątny")) {

                signal = new TriangularSignal(rangeStart, rangeLength, amplitude, term,
                        fulfillment);

            } else if (selectedSignal.equals("skok jednostkowy")) {

                signal = new JumpSingal(rangeStart, rangeLength, amplitude, jumpMoment);

            } else if (selectedSignal.equals("impuls jednostkowy")) {

                isScatterChart = true;
                changeLineChartToScatterChart(tabPaneResults,
                        new ScatterChart(new NumberAxis(), new NumberAxis()));

                signal = new UnitImpulseSignal(rangeStart, rangeLength, sampleRate,
                        amplitude, jumpMoment.intValue());

            } else if (selectedSignal.equals("szum impulsowy")) {

                isScatterChart = true;
                changeLineChartToScatterChart(tabPaneResults,
                        new ScatterChart(new NumberAxis(), new NumberAxis()));

                signal = new ImpulseNoise(rangeStart, rangeLength, sampleRate, amplitude,
                        probability);

            }

            representSignal(signal);

        } catch (NumberFormatException e) {
            Announcements.messageBox("Błędne Dane",
                    "Wprowadzono błędne dane", Alert.AlertType.WARNING);

        }
    }

    public void performOperationOnCharts() {
        String selectedOperation = comboBoxOperationTypes.getSelectionModel()
                .getSelectedItem().toString();
        int s1Index = comboBoxFirstSignal.getSelectionModel().getSelectedIndex();
        int s2Index = comboBoxSecondSignal.getSelectionModel().getSelectedIndex();

        Signal s1 = signals.get(s1Index);
        Signal s2 = signals.get(s2Index);
        Signal resultSignal = null;

        if (selectedOperation.equals(ArithmeticType.ADD.getName())) {
            resultSignal = new FinalSignal(s1, s2, (a, b) -> a + b);
        } else if (selectedOperation.equals(ArithmeticType.SUB.getName())) {
            resultSignal = new FinalSignal(s1, s2, (a, b) -> a - b);
        } else if (selectedOperation.equals(ArithmeticType.MULTIPLI.getName())) {
            resultSignal = new FinalSignal(s1, s2, (a, b) -> a * b);
        } else if (selectedOperation.equals(ArithmeticType.DIV.getName())) {
            resultSignal = new FinalSignal(s1, s2, (a, b) -> a / b);
        }

        representSignal(resultSignal);
    }

    public void loadChart() {
        int tabIndex = tabPaneResults.getSelectionModel().getSelectedIndex();

        try {
            signalFileReader = new FileLoader<>(new FileChooser()
                    .showOpenDialog(StageController.getApplicationStage())
                    .getName());

            signals.put(tabIndex, signalFileReader.read());
            convertSignalToChart(signals.get(tabIndex));

        } catch (NullPointerException | ActionException e) {
            e.printStackTrace();
            Announcements.messageBox("Błąd Ładowania Pliku",
                    "Nie można załadować wybranego pliku", Alert.AlertType.WARNING);
        }
    }

    public void saveChart() {
        int tabIndex = tabPaneResults.getSelectionModel().getSelectedIndex();

        try {
            if (signals.get(tabIndex) != null) {
                signalFileReader = new FileLoader<>(new FileChooser()
                        .showSaveDialog(StageController.getApplicationStage())
                        .getName());

                signalFileReader.write(signals.get(tabIndex));
            } else {
                Announcements.messageBox("Błąd Zapisu Do Pliku",
                        "Sygnał nie został jeszcze wygenerowany", Alert.AlertType.WARNING);
            }
        } catch (NullPointerException | ActionException e) {
            e.printStackTrace();
            Announcements.messageBox("Błąd Zapisu Do Pliku",
                    "Nie można zapisać do wybranego pliku", Alert.AlertType.WARNING);
        }
    }
}
