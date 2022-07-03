package gui.control;

import addons.OperationException;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.TabsModel;
import model.operation.twoSignal.SignalOperation;
import model.signal.ComparisonSignal;
import gui.graph.SignalTab;


public class ComparisonPane implements EventHandler {
	VBox vPane;
	ComboBox firstSignalSelect;
	ComboBox secondSignalSelect;
	public Button compareButton;



	public ComparisonPane() {
		buildView();
	}

	private void buildView() {
		vPane = new VBox();
		vPane.setPadding(new Insets(10,10,10,10));

		Label labelFirstSignal = new Label("Sygnał");
		Label labelSecondSignal = new Label("Sygnał");

		firstSignalSelect = new ComboBox();
		secondSignalSelect = new ComboBox();


		compareButton = new Button("Porównaj");
		compareButton.setOnMouseClicked(this);


		firstSignalSelect.setItems(TabsModel.INSTANCE.getSignalPanes());
		secondSignalSelect.setItems(TabsModel.INSTANCE.getSignalPanes());
		Callback<ListView<SignalTab>, ListCell<SignalTab>> signalCellFactory = getSignalCellFactory();
		Callback<ListView<SignalTab>, ListCell<SignalTab>> secondSignalCellFactory = getSignalCellFactory();


		Callback<ListView<SignalOperation>, ListCell<SignalOperation>> operationCellFactory = getOperationCellFactory();

		firstSignalSelect.setButtonCell(signalCellFactory.call(null));
		firstSignalSelect.setCellFactory(signalCellFactory);
		secondSignalSelect.setButtonCell(secondSignalCellFactory.call(null));
		secondSignalSelect.setCellFactory(secondSignalCellFactory);


		vPane.getChildren().addAll(labelFirstSignal, firstSignalSelect, labelSecondSignal, secondSignalSelect, compareButton);
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
		SignalTab firstTab = (SignalTab) firstSignalSelect.getSelectionModel().getSelectedItem();
		SignalTab secondTab = (SignalTab) secondSignalSelect.getSelectionModel().getSelectedItem();

		if (firstTab != null && secondTab != null){
			try {

				ComparisonSignal signal = new ComparisonSignal();
				signal.addDataseries(firstTab, secondTab);
				TabsModel.INSTANCE.addTab(signal);
			} catch (OperationException ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("BŁĄD");
				alert.setContentText(ex.getMessage());
				alert.showAndWait();
			}
		}

	}

}
