package gui.control;

import addons.OperationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.TabsModel;

import model.operation.twoSignal.*;
import model.signal.Signal;
import gui.graph.SignalTab;

import java.util.ArrayList;

public class OperationPane implements EventHandler {
	VBox vPane;
	ComboBox firstSignalSelect;
	ComboBox secondSignalSelect;
	ComboBox operationSelect;
	public Button generateButton;
	private ObservableList<SignalOperation> operations;


	public OperationPane() {
		registerOperations();
		buildView();
	}

	private void registerOperations() {
		ArrayList<SignalOperation> operationss = new ArrayList();
		operationss.add(new SignalAddition());
		operationss.add(new SignalSubtraction());
		operationss.add(new SignalMultiplication());
		operationss.add(new SignalDivision());
		operationss.add(new ConvolutionOperation());
		operationss.add(new CorrelationOperation());
		operationss.add(new CorrelationConvolutionOperation());
		operations = FXCollections.observableArrayList(operationss);
	}

	private void buildView() {
		vPane = new VBox();
		vPane.setPadding(new Insets(10,10,10,10));


		Label labelFirstSignal = new Label("Sygnał");
		Label labelSecondSignal = new Label("Sygnał");
		Label labelOperation = new Label("Operacja:");

		firstSignalSelect = new ComboBox();
		secondSignalSelect = new ComboBox();
		operationSelect = new ComboBox();

		generateButton = new Button("Generuj");
		generateButton.setOnMouseClicked(this);


		firstSignalSelect.setItems(TabsModel.INSTANCE.getSignalPanes());
		secondSignalSelect.setItems(TabsModel.INSTANCE.getSignalPanes());
		Callback<ListView<SignalTab>, ListCell<SignalTab>> signalCellFactory = getSignalCellFactory();
		Callback<ListView<SignalTab>, ListCell<SignalTab>> secondSignalCellFactory = getSignalCellFactory();

		operationSelect.setItems(operations);
		Callback<ListView<SignalOperation>, ListCell<SignalOperation>> operationCellFactory = getOperationCellFactory();

		firstSignalSelect.setButtonCell(signalCellFactory.call(null));
		firstSignalSelect.setCellFactory(signalCellFactory);
		secondSignalSelect.setButtonCell(secondSignalCellFactory.call(null));
		secondSignalSelect.setCellFactory(secondSignalCellFactory);
		operationSelect.setButtonCell(operationCellFactory.call(null));
		operationSelect.setCellFactory(operationCellFactory);

		vPane.getChildren().addAll(labelFirstSignal, firstSignalSelect, labelOperation, operationSelect, labelSecondSignal, secondSignalSelect, generateButton);
	}

	private Callback<ListView<SignalOperation>, ListCell<SignalOperation>> getOperationCellFactory() {
		return new Callback<ListView<SignalOperation>, ListCell<SignalOperation>>() {
			@Override
			public ListCell<SignalOperation> call(ListView<SignalOperation> l) {
				return new ListCell<SignalOperation>() {

					@Override
					protected void updateItem(SignalOperation item, boolean empty) {
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
		Object firstTab = firstSignalSelect.getSelectionModel().getSelectedItem();
		Object secondTab = secondSignalSelect.getSelectionModel().getSelectedItem();
		SignalOperation operation = (SignalOperation) operationSelect.getSelectionModel().getSelectedItem();

		if (firstTab != null && secondTab != null && operation != null) {
			Signal firstSignal = ((SignalTab) firstTab).getSignal();
			Signal secondSignal = ((SignalTab) secondTab).getSignal();

			if (firstSignal == null || secondSignal == null) {
				return;
			}

			try {
				Signal output = operation.execute(firstSignal, secondSignal);
				TabsModel.INSTANCE.addTab(output);
			} catch (OperationException ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("BŁĄD");
				alert.setContentText(ex.getMessage());
				alert.showAndWait();
			}
		}

	}

}
