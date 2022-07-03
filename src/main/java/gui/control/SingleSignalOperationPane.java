package gui.control;

import addons.OperationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.TabsModel;
import model.operation.singleSignal.*;
import model.signal.Signal;
import gui.graph.SignalTab;

import java.util.ArrayList;

public class SingleSignalOperationPane implements EventHandler {
	VBox vPane;
	ComboBox signalSelect;
	ComboBox operationSelect;
	private TextField sampling;
	private TextField quantizationStep;
	private TextField sincParam;
	private Button generateButton;
	private ObservableList<SingleSignalOperation> operations;


	public SingleSignalOperationPane() {
		registerOperations();
		buildView();
	}

	private void registerOperations() {
		ArrayList<SingleSignalOperation> operationss = new ArrayList();
		operationss.add(new SamplingOperation());
		operationss.add(new RoundedQuantization());
		operationss.add(new ZeroOrderHold());
		operationss.add(new SincReconstruction());
		operations = FXCollections.observableArrayList(operationss);
	}

	private void buildView() {
		vPane = new VBox();
		vPane.setPadding(new Insets(10,10,10,10));

		Label labelSignal = new Label("Sygnał");
		Label labelOperation = new Label("Operacja:");
		Label labelSampling = new Label("Częstotliwość próbkowania:");
		Label labelQuantizationStep = new Label("Krok kwantyzacji:");
		Label labelSincParam = new Label("Parametr sinc:");

		signalSelect = new ComboBox();
		operationSelect = new ComboBox();
		sampling = new TextField("100");
		quantizationStep = new TextField("0.01");
		sincParam = new TextField("5");

		generateButton = new Button("Generuj");
		generateButton.setOnMouseClicked(this);


		signalSelect.setItems(TabsModel.INSTANCE.getSignalPanes());
		Callback<ListView<SignalTab>, ListCell<SignalTab>> signalCellFactory = getSignalCellFactory();

		operationSelect.setItems(operations);
		Callback<ListView<SingleSignalOperation>, ListCell<SingleSignalOperation>> operationCellFactory = getOperationCellFactory();

		signalSelect.setButtonCell(signalCellFactory.call(null));
		signalSelect.setCellFactory(signalCellFactory);
		operationSelect.setButtonCell(operationCellFactory.call(null));
		operationSelect.setCellFactory(operationCellFactory);


		EventHandler<ActionEvent> event =
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
//						((SingleSignalOperation) operationSelect.getSelectionModel().getSelectedItem()).setInputVisibility(editor);
					}
				};

		operationSelect.setOnAction(event);

		vPane.getChildren().addAll(labelSignal, signalSelect, labelOperation, operationSelect, labelSampling, sampling, labelQuantizationStep, quantizationStep, labelSincParam, sincParam, generateButton);
	}

	private Callback<ListView<SingleSignalOperation>, ListCell<SingleSignalOperation>> getOperationCellFactory() {
		return new Callback<ListView<SingleSignalOperation>, ListCell<SingleSignalOperation>>() {
			@Override
			public ListCell<SingleSignalOperation> call(ListView<SingleSignalOperation> l) {
				return new ListCell<SingleSignalOperation>() {

					@Override
					protected void updateItem(SingleSignalOperation item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
							setText(item.getName());
						} else {
							setText(null);
						}
					}
				};
			}
		};
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

	public Pane getView() {
		return vPane;
	}

	@Override
	public void handle(Event event) {
		Object signalSelect = this.signalSelect.getSelectionModel().getSelectedItem();
		SingleSignalOperation operation = (SingleSignalOperation) operationSelect.getSelectionModel().getSelectedItem();

		SingleSignalOperationParam params = getParamsFromView();

		if (signalSelect != null  && operation != null) {
			Signal signal = ((SignalTab) signalSelect).getSignal();

			if (signal == null) {
				return;
			}

			try {
				Signal output = operation.execute(signal, params);
				TabsModel.INSTANCE.addTab(output);
			} catch (OperationException ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("BŁĄD");
				alert.setContentText(ex.getMessage());
				alert.showAndWait();
			}
		}

	}

	private SingleSignalOperationParam getParamsFromView() {
		SingleSignalOperationParam param = new SingleSignalOperationParam();
		param.setSamplingFrequency(Double.valueOf(sampling.getCharacters().toString()));
		param.setQuantizationStep(Double.valueOf(quantizationStep.getCharacters().toString()));
		param.setNeighbourSamples(Integer.valueOf(sincParam.getCharacters().toString()));
		return param;
	}
}
