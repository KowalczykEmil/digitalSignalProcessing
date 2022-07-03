package gui.control;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.TabsModel;
import model.operation.singleSignal.DFTTransformation;
import model.signal.Signal;
import org.apache.commons.math3.complex.Complex;
import gui.graph.SignalTab;
import gui.transformation.TransformationWindow;

import java.util.List;

public class TransformationPane implements EventHandler {
    VBox vPane;
    Button transformationButton;
    ComboBox signalSelect;


    public TransformationPane(){
        buildView();
    }

    private void buildView() {
        vPane = new VBox();
        vPane.setPadding(new Insets(10,10,10,10));

        Label labelSelectSignal = new Label("Sygnał");
        signalSelect = new ComboBox();

        signalSelect.setItems(TabsModel.INSTANCE.getSignalPanes());
        Callback<ListView<SignalTab>, ListCell<SignalTab>> signalCellFactory = getSignalCellFactory();

        signalSelect.setButtonCell(signalCellFactory.call(null));
        signalSelect.setCellFactory(signalCellFactory);

        signalSelect.setButtonCell(signalCellFactory.call(null));
        signalSelect.setCellFactory(signalCellFactory);

        transformationButton = new Button("Przekształcenie DFT");
        transformationButton.setOnMouseClicked(event -> {

            Stage saveWindow = new Stage();
            Stage secondSaveWindow = new Stage();
            saveWindow.setTitle("Przekształcenie DFT(zestaw W1)");
            secondSaveWindow.setTitle("Przekształcenie DFT(zestaw W2)");

            BorderPane layout = new BorderPane();
            BorderPane secondLayout = new BorderPane();

            Object selectedSignal = signalSelect.getSelectionModel().getSelectedItem();

            Signal signal = ((SignalTab) selectedSignal).getSignal();

            DFTTransformation dftTransformation = new DFTTransformation();
            List<Complex> complexValues = dftTransformation.transform(signal);

            TransformationWindow transformationWindow = new TransformationWindow(dftTransformation.getRealValuesChart(), dftTransformation.getImgValuesChart(), signal.getDataset(), complexValues, signal);;
            TransformationWindow secondTransformationWindow = new TransformationWindow(dftTransformation.getAbsComplexChart(), dftTransformation.getArgComplexChart(), signal.getDataset(), complexValues, signal);;

            layout.setCenter(transformationWindow.getView());
            secondLayout.setCenter(secondTransformationWindow.getView());

            Scene scene = new Scene(layout, 1400, 780);
            Scene secondScene = new Scene(secondLayout, 1400, 780);
            saveWindow.setScene(scene);
            saveWindow.show();
            secondSaveWindow.setScene(secondScene);
            secondSaveWindow.show();
            scene.getStylesheets().add("style.css");
            secondScene.getStylesheets().add("style.css");

        });
        vPane.getChildren().addAll(labelSelectSignal, signalSelect, transformationButton);
    }

    public Pane getView() {
        return vPane;
    }

    @Override
    public void handle(Event event) {

    }

    private Callback<ListView<SignalTab>, ListCell<SignalTab>> getSignalCellFactory() {
        return new Callback<ListView<SignalTab>, ListCell<SignalTab>>() {
            @Override
            public ListCell<SignalTab> call(ListView<SignalTab> l) {
                return new ListCell<SignalTab>() {

                    @Override
                    protected void updateItem(SignalTab item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText("Karta " + item.getIndex());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        };
    }

}
