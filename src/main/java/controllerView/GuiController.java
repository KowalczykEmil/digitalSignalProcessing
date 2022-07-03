package controllerView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import stageCore.StageController;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {
    @FXML
    private TabPane tabPaneInputs;
    @FXML
    private Pane chooseParamsTab;
    @FXML
    private ComboBox comboBoxSignalTypes;
    @FXML
    private ComboBox comboBoxOperationTypes;
    @FXML
    private ComboBox comboBoxFirstSignal;
    @FXML
    private ComboBox comboBoxSecondSignal;
    @FXML
    private TabPane tabPaneResults;
    @FXML
    private Spinner spinnerHistogramRange;

    private TextField textFieldAmplitude = new TextField();
    private TextField textFieldStartTime = new TextField();
    private TextField textFieldSignalDuration = new TextField();
    private TextField textFieldBasicPeriod = new TextField();
    private TextField textFieldFillFactor = new TextField();
    private TextField textFieldJumpTime = new TextField();
    private TextField textFieldProbability = new TextField();
    private TextField textFieldSamplingFrequency = new TextField();

    private Init init;
    private Launcher launcher;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spinnerHistogramRange.setValueFactory(new SpinnerValueFactory
                .IntegerSpinnerValueFactory(5, 20, 10, 5));

        init = new Init(
                comboBoxSignalTypes, comboBoxOperationTypes, comboBoxFirstSignal,
                comboBoxSecondSignal, chooseParamsTab, textFieldAmplitude,
                textFieldStartTime, textFieldSignalDuration, textFieldBasicPeriod,
                textFieldFillFactor, textFieldJumpTime, textFieldProbability,
                textFieldSamplingFrequency, tabPaneResults
        );

        launcher = new Launcher(
                comboBoxSignalTypes, comboBoxOperationTypes, comboBoxFirstSignal,
                comboBoxSecondSignal, textFieldAmplitude, textFieldStartTime,
                textFieldSignalDuration, textFieldBasicPeriod, textFieldFillFactor,
                textFieldJumpTime, textFieldProbability, textFieldSamplingFrequency,
                tabPaneResults, spinnerHistogramRange
        );

        init.prepareTabPaneResults(0);
        init.prepareTabPaneInputs();
    }

    /*--------------------------------------------------------------------------------------------*/
    @FXML
    private void onActionButtonReloadStage(ActionEvent actionEvent) {
        StageController.reloadStage("MainView.fxml", "Zadanie 1 - Emil i Joasia");
    }


    @FXML
    private void onActionButtonCloseProgram(ActionEvent actionEvent) {
        System.exit(0);
    }

    /*--------------------------------------------------------------------------------------------*/
    @FXML
    private void onActionLoadChart(ActionEvent actionEvent) {
        launcher.loadChart();
    }

    @FXML
    private void onActionSaveChart(ActionEvent actionEvent) {
        launcher.saveChart();
    }

    /*--------------------------------------------------------------------------------------------*/
    @FXML
    private void onActionButtonGenerateData(ActionEvent actionEvent) {
        //TODO ADD IMPL, IN FINAL VERSION LOAD DATA FROM LOGIC
        Integer selectedTab = tabPaneInputs.getSelectionModel().getSelectedIndex();

        switch (selectedTab) {
            case 0: {
                launcher.computeCharts();
                break;
            }
            case 1: {
                launcher.performOperationOnCharts();
                break;
            }
        }
    }
}

