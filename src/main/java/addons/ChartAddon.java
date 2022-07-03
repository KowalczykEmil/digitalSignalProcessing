package addons;

import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChartAddon {
    private ChartAddon() {
    }

    public static XYChart.Data<Number, Number> prepareDataRecord(Number numberOne,
                                                                 Number numberTwo) {
        return new XYChart.Data(numberOne, numberTwo);
    }

    public static XYChart.Data<Number, String> prepareDataRecord(Number number,
                                                                 String string) {
        return new XYChart.Data(number, string);
    }

    public static XYChart.Data<String, Number> prepareDataRecord(String string,
                                                                 Number number) {
        return new XYChart.Data(string, number);
    }

    public static XYChart.Data<String, String> prepareDataRecord(String stringOne,
                                                                 String stringTwo) {
        return new XYChart.Data(stringOne, stringTwo);
    }

    public static TabPanel castTabPaneToCustomTabPane(TabPane tabPane) {
        return (TabPanel) tabPane.getSelectionModel().getSelectedItem().getContent();
    }

    public static void textFieldSetValue(TextField textField, String string) {
        textField.setText(string);
    }

    public static TextField setTextFieldPosition(TextField textField,
                                                 int pointX, int pointY) {
        textField.setLayoutX(pointX);
        textField.setLayoutY(pointY);

        return textField;
    }

    public static Label prepareLabelWithPosition(String text, int pointX, int pointY) {
        Label label = new Label(text);
        label.setLayoutX(pointX);
        label.setLayoutY(pointY);

        return label;
    }

    public static void appendLabelText(Node node, String text) {
        Label label = (Label) node;
        String initialText = label.getText().substring(0, label.getText().indexOf(":") + 1);
        label.setText(initialText + "     " + text);
    }

    public static void fillComboBox(ComboBox comboBox, Collection collection) {
        List items = comboBox.getItems();
        items.clear();
        collection.forEach((it) -> items.add(it));
        comboBox.getSelectionModel().selectFirst();
    }

    public static List<String> getTabNameList(List<Tab> tabList) {
        List<String> names = new ArrayList<>();
        tabList.forEach((it) -> names.add(it.getText()));

        return names;
    }

    public static void clearAndAddNewDataToChart(XYChart chart, XYChart.Series series) {
        chart.getData().clear();
        chart.getData().add(series);
    }

    public static void changeLineChartToScatterChart(TabPane tabPane, ScatterChart scatterChart) {
        TabPanel customTabPane = castTabPaneToCustomTabPane(tabPane);
        customTabPane.getChart().setContent(scatterChart);
        ScatterChart newScatterChart = (ScatterChart) customTabPane.getChart().getContent();
        newScatterChart.setAnimated(false);
    }

    public static void changeScatterChartToLineChart(TabPane tabPane, LineChart lineChart) {
        TabPanel customTabPane = castTabPaneToCustomTabPane(tabPane);
        customTabPane.getChart().setContent(lineChart);
        LineChart newLineChart = (LineChart) customTabPane.getChart().getContent();
        newLineChart.setAnimated(false);
        newLineChart.setCreateSymbols(false);
        newLineChart.setAnimated(false);
    }

    public static void fillLineChart(TabPanel customTabPane,
                                     Collection<RecordAddon<Number, Number>> dataCollection) {
        LineChart lineChart = (LineChart) customTabPane.getChart().getContent();
        XYChart.Series series = new XYChart.Series<>();

        dataCollection.forEach((it) -> {
            series.getData().add(prepareDataRecord(it.getX(), it.getY()));
        });

        clearAndAddNewDataToChart(lineChart, series);
    }

    public static void fillScatterChart(TabPanel customTabPane,
                                        Collection<RecordAddon<Number, Number>> dataCollection) {
        ScatterChart scatterChart = (ScatterChart) customTabPane.getChart().getContent();
        XYChart.Series series = new XYChart.Series<>();

        dataCollection.forEach((it) -> {
            series.getData().add(prepareDataRecord(it.getX(), it.getY()));
        });

        clearAndAddNewDataToChart(scatterChart, series);
    }

    public static void fillBarChart(TabPanel customTabPane,
                                    Collection<RecordAddon<String, Number>> dataCollection) {
        BarChart barChart = (BarChart) customTabPane.getHistogram().getContent();
        XYChart.Series series = new XYChart.Series<>();

        dataCollection.forEach((it) -> {
            series.getData().add(prepareDataRecord(it.getX(), it.getY()));
        });

        clearAndAddNewDataToChart(barChart, series);
    }
}
