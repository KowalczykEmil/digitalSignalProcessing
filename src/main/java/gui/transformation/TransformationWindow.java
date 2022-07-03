package gui.transformation;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import model.NoiseParam;
import model.operation.singleSignal.DFTTransformation;
import model.signal.Signal;
import org.apache.commons.math3.complex.Complex;

import java.util.List;

public class TransformationWindow implements EventHandler{
    VBox vPanel;
    Chart chartReal;
    Chart chartIm;
    Button reverseTransformButton;
    List<XYChart.Data<Double, Double>> dataset;
    List<Complex> complexValues;
    Signal signal;
    public TransformationWindow(Chart chart, Chart chartIm, List<XYChart.Data<Double, Double>> dataset, List<Complex> complexValues, Signal signal) {
        this.chartReal = chart;
        this.chartIm=chartIm;
        this.dataset=dataset;
        this.complexValues=complexValues;
        this.signal=signal;
        this.vPanel = new VBox();
        createView();

    }

    private void createView(){
        prepareButton();
        if (chartReal != null && chartIm!=null) {
            vPanel.getChildren().addAll(chartReal,chartIm, reverseTransformButton);
        }
    }

    public Node getView(){
        return vPanel;
    }

    public void prepareButton(){
        reverseTransformButton = new Button("Przekształcenie odwrotne");
        reverseTransformButton.centerShapeProperty();
        reverseTransformButton.setOnMouseClicked(event -> {

            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis();

            String chartType = signal.getChart().getTypeSelector();
            if(chartType.equals("ScatterChart")){
                final ScatterChart<Number,Number> scatterChart = new ScatterChart<Number,Number>(xAxis,yAxis);
                XYChart.Series series = new XYChart.Series();

                DFTTransformation dftTransformation = new DFTTransformation();
                List<XYChart.Data<Double, Double>> reverseDataset = dftTransformation.reverseTransform(dataset, complexValues, signal);

                series.getData().addAll(reverseDataset);

                scatterChart.getData().add(series);
                scatterChart.setTitle("Przekształcenie odwrotne");
                this.vPanel.getChildren().remove(2);

                this.vPanel.getChildren().add(2, scatterChart);
                //this.vPanel.getChildren().add(3, this.reverseTransformButton);
            }else {
                final LineChart<Number,Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
                XYChart.Series series = new XYChart.Series();

                DFTTransformation dftTransformation = new DFTTransformation();
                List<XYChart.Data<Double, Double>> reverseDataset = dftTransformation.reverseTransform(dataset, complexValues, signal);

                series.getData().addAll(reverseDataset);

                lineChart.getData().add(series);
                lineChart.setCreateSymbols(false);
                lineChart.setTitle("Przekształcenie odwrotne");
                this.vPanel.getChildren().remove(2);

                this.vPanel.getChildren().add(2, lineChart);
                //this.vPanel.getChildren().add(3, this.reverseTransformButton);
            }


        });
    }

    @Override
    public void handle(Event event) {

    }
}
