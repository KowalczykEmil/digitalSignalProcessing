package controllerView;

import addons.TabPanel;
import addons.WindowTab;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.ArithmeticType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static addons.ChartAddon.fillComboBox;
import static addons.ChartAddon.getTabNameList;
import static addons.ChartAddon.prepareLabelWithPosition;
import static addons.ChartAddon.setTextFieldPosition;
import static addons.ChartAddon.textFieldSetValue;

public class Init {
    /*------------------------ FIELDS REGION ------------------------*/
    private ComboBox comboBoxSignalTypes;
    private ComboBox comboBoxOperationTypes;
    private ComboBox comboBoxFirstSignal;
    private ComboBox comboBoxSecondSignal;
    private Pane chooseParamsTab;

    private TextField textFieldAmplitude;
    private TextField textFieldStartTime;
    private TextField textFieldSignalDuration;
    private TextField textFieldBasicPeriod;
    private TextField textFieldFillFactor;
    private TextField textFieldJumpTime;
    private TextField textFieldProbability;
    private TextField textFieldSamplingFrequency;

    private TabPane tabPaneResults;

    /*------------------------ METHODS REGION ------------------------*/
    public Init(ComboBox comboBoxSignalTypes, ComboBox comboBoxOperationTypes,
                ComboBox comboBoxFirstSignal, ComboBox comboBoxSecondSignal,
                Pane chooseParamsTab, TextField textFieldAmplitude,
                TextField textFieldStartTime, TextField textFieldSignalDuration,
                TextField textFieldBasicPeriod, TextField textFieldFillFactor,
                TextField textFieldJumpTime, TextField textFieldProbability,
                TextField textFieldSamplingFrequency, TabPane tabPaneResults) {
        this.comboBoxSignalTypes = comboBoxSignalTypes;
        this.comboBoxOperationTypes = comboBoxOperationTypes;
        this.comboBoxFirstSignal = comboBoxFirstSignal;
        this.comboBoxSecondSignal = comboBoxSecondSignal;
        this.chooseParamsTab = chooseParamsTab;
        this.textFieldAmplitude = textFieldAmplitude;
        this.textFieldStartTime = textFieldStartTime;
        this.textFieldSignalDuration = textFieldSignalDuration;
        this.textFieldBasicPeriod = textFieldBasicPeriod;
        this.textFieldFillFactor = textFieldFillFactor;
        this.textFieldJumpTime = textFieldJumpTime;
        this.textFieldProbability = textFieldProbability;
        this.textFieldSamplingFrequency = textFieldSamplingFrequency;
        this.tabPaneResults = tabPaneResults;
    }

    private void fillTextFields() {
        textFieldSetValue(textFieldAmplitude, String.valueOf(1));
        textFieldSetValue(textFieldStartTime, String.valueOf(0));
        textFieldSetValue(textFieldSignalDuration, String.valueOf(5));
        textFieldSetValue(textFieldBasicPeriod, String.valueOf(1));
        textFieldSetValue(textFieldFillFactor, String.valueOf(0.5));
        textFieldSetValue(textFieldJumpTime, String.valueOf(2));
        textFieldSetValue(textFieldProbability, String.valueOf(0.5));
        textFieldSetValue(textFieldSamplingFrequency, String.valueOf(16));
    }

    private void fillComboBoxes() {
        fillComboBox(comboBoxSignalTypes, Stream.of(
                "szum o rozkładzie jednostajnym",
                "szum gaussowski",
                "sygnał sinusoidalny",
                "sygnał sinusoidalny wyprostowany jednopołówkowo",
                "sygnał sinusoidalny wyprostowany dwupołówkowo",
                "sygnał prostokątny",
                "sygnał prostokątny symetryczny",
                "sygnał trójkątny",
                "skok jednostkowy",
                "impuls jednostkowy",
                "szum impulsowy"
        ).collect(Collectors.toCollection(ArrayList::new)));

        fillComboBox(comboBoxOperationTypes, Stream.of(
                ArithmeticType.ADD.getName(),
                ArithmeticType.SUB.getName(),
                ArithmeticType.MULTIPLI.getName(),
                ArithmeticType.DIV.getName()
        ).collect(Collectors.toCollection(ArrayList::new)));

        fillComboBox(comboBoxFirstSignal, getTabNameList(tabPaneResults.getTabs()));
        fillComboBox(comboBoxSecondSignal, getTabNameList(tabPaneResults.getTabs()));
    }

    private void fillChooseParamsTab() {

        List<Node> basicInputs = Stream.of(
                prepareLabelWithPosition("Wybierz Parametry", 168, 14),
                prepareLabelWithPosition("Amplituda", 50, 50),
                prepareLabelWithPosition("Czas początkowy", 50, 90),
                prepareLabelWithPosition("Czas trwania sygnału", 50, 130),
                setTextFieldPosition(textFieldAmplitude, 270, 50),
                setTextFieldPosition(textFieldStartTime, 270, 90),
                setTextFieldPosition(textFieldSignalDuration, 270, 130)
        ).collect(Collectors.toCollection(ArrayList::new));

        chooseParamsTab.getChildren().setAll(basicInputs);

        comboBoxSignalTypes.setOnAction((event -> {
            String selectedSignal = comboBoxSignalTypes.getSelectionModel()
                    .getSelectedItem().toString();

            if (selectedSignal.equals("szum o rozkładzie jednostajnym")
                    || selectedSignal.equals("szum gaussowski")) {

                chooseParamsTab.getChildren().setAll(basicInputs);

            } else if (selectedSignal.equals("sygnał sinusoidalny")
                    || selectedSignal.equals("sygnał sinusoidalny wyprostowany jednopołówkowo")
                    || selectedSignal.equals("sygnał sinusoidalny wyprostowany dwupołówkowo")) {

                chooseParamsTab.getChildren().setAll(basicInputs);
                chooseParamsTab.getChildren().addAll(
                        prepareLabelWithPosition("Okres podstawowy", 50, 170),
                        setTextFieldPosition(textFieldBasicPeriod, 270, 170)
                );
            } else if (selectedSignal.equals("sygnał prostokątny")
                    || selectedSignal.equals("sygnał prostokątny symetryczny")
                    || selectedSignal.equals("sygnał trójkątny")) {

                chooseParamsTab.getChildren().setAll(basicInputs);
                chooseParamsTab.getChildren().addAll(
                        prepareLabelWithPosition("Okres podstawowy", 50, 170),
                        setTextFieldPosition(textFieldBasicPeriod, 270, 170),
                        prepareLabelWithPosition("Wspł wypełnienia", 50, 210),
                        setTextFieldPosition(textFieldFillFactor, 270, 210)
                );
            } else if (selectedSignal.equals("skok jednostkowy")) {

                chooseParamsTab.getChildren().setAll(basicInputs);
                chooseParamsTab.getChildren().addAll(
                        prepareLabelWithPosition("Czas skoku", 50, 170),
                        setTextFieldPosition(textFieldJumpTime, 270, 170)
                );
            } else if (selectedSignal.equals("impuls jednostkowy")) {

                chooseParamsTab.getChildren().setAll(basicInputs);
                chooseParamsTab.getChildren().addAll(
                        prepareLabelWithPosition("Częst próbkowania", 50, 170),
                        prepareLabelWithPosition("Numer próbki skoku", 50, 210),
                        setTextFieldPosition(textFieldSamplingFrequency, 270, 170),
                        setTextFieldPosition(textFieldJumpTime, 270, 210)
                );
            } else if (selectedSignal.equals("szum impulsowy")) {

                chooseParamsTab.getChildren().setAll(basicInputs);
                chooseParamsTab.getChildren().addAll(
                        prepareLabelWithPosition("Prawdopodobieństwo", 50, 170),
                        prepareLabelWithPosition("Częst próbkowania", 50, 210),
                        setTextFieldPosition(textFieldProbability, 270, 170),
                        setTextFieldPosition(textFieldSamplingFrequency, 270, 210)
                );
            }
        }));
    }

    public void prepareTabPaneInputs() {
        fillComboBoxes();
        fillTextFields();
        fillChooseParamsTab();
    }

    public void prepareTabPaneResults(int index) {

        LineChart lineChart = new LineChart<>(new NumberAxis(), new NumberAxis());
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);

        BarChart barChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        barChart.setAnimated(false);

        Pane pane = new Pane(
                prepareLabelWithPosition("Wartość średnia sygnału: ", 25, 40),
                prepareLabelWithPosition("Wartość średnia bezwzględna sygnału: ", 25, 80),
                prepareLabelWithPosition("Wartość skuteczna sygnału: ", 25, 120),
                prepareLabelWithPosition("Wariancja sygnału: ", 25, 160),
                prepareLabelWithPosition("Moc średnia sygnału: ", 25, 200)

        );

        tabPaneResults.getTabs().add(new Tab("Karta " + index,
                new TabPanel(
                        new WindowTab("Wykres", lineChart, false),
                        new WindowTab("Histogram", barChart, false),
                        new WindowTab("Parametry", pane, false)
                )));
    }
}
