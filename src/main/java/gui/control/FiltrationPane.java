package gui.control;

import addons.OperationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import model.operation.filter.BandPassFilter;
import model.operation.filter.Filter;
import model.operation.filter.HighPassFilter;
import model.operation.filter.LowPassFilter;
import model.operation.window.*;
import model.signal.Signal;
import gui.distance.ParamPane;
import gui.graph.SignalTab;

import java.util.ArrayList;

/**
 * O2(Okno Hanninga) i F2 (górnoprzepustowy)
 */

public class FiltrationPane implements EventHandler {
	VBox vPane;
	ComboBox filterSelect;
	ComboBox windowSelect;
	TextField rzadTextField;
	TextField czestotliwoscOdcieciatextField;
	TextField czestotliwoscProbkowaniatextField;

	public Button generateButton;
	public Button distanceButton;
	private ObservableList<Filter> filters;
	private ObservableList<Window> windows;


	public FiltrationPane() {
		registerFilters();
		registerWindows();
		buildView();
	}

	private void registerFilters() {
		ArrayList<Filter> availableFilters = new ArrayList();
		availableFilters.add(new BandPassFilter());
		availableFilters.add(new HighPassFilter());
		availableFilters.add(new LowPassFilter());
		filters = FXCollections.observableArrayList(availableFilters);
	}

	private void registerWindows() {
		ArrayList<Window> availableWindows = new ArrayList();
		availableWindows.add(new RectangularWindow());
		availableWindows.add(new HammingWindow());
		availableWindows.add(new HanningWindow());
		availableWindows.add(new BlackamanWindow());

		windows = FXCollections.observableArrayList(availableWindows);
	}

	private void buildView() {
		vPane = new VBox();
		vPane.setPadding(new Insets(10, 10, 10, 10));

		Label labelOperation = new Label("Filtr:");

		Label labelRzad = new Label("Rząd filtru:");
		Label labelCzestotliwoscOdciecia = new Label("Częstotliwość odcięcia:");
		Label labelCzestotlProbkow = new Label("Częstotliwość probkowania:");
		Label labelWindow = new Label("Okno:");


		filterSelect = new ComboBox();
		windowSelect = new ComboBox();

		rzadTextField = new TextField("20");
		czestotliwoscOdcieciatextField = new TextField("20");
		czestotliwoscProbkowaniatextField = new TextField("1000");


		generateButton = new Button("Generuj");
		distanceButton = new Button("Pomiar odległości");
		generateButton.setOnMouseClicked(this);
		distanceButton.setOnMouseClicked(event -> {

			Stage saveWindow = new Stage();
			saveWindow.setTitle("Pomiar odległości");

			BorderPane layout = new BorderPane();
			ParamPane paramPane = new ParamPane();

			layout.setCenter(paramPane.getView());
//			layout.setCenter(graphTabController.getView());


//			StackPane root = new StackPane();
//			SavePane pane = new SavePane(TabsModel.INSTANCE, saveWindow);
//			root.getChildren().add(pane.getView());
			Scene scene = new Scene(layout, 700, 1000);
			saveWindow.setScene(scene);
			scene.getStylesheets().add("style.css");
			saveWindow.show();

		});


		Callback<ListView<SignalTab>, ListCell<SignalTab>> signalCellFactory = getSignalCellFactory();
		Callback<ListView<SignalTab>, ListCell<SignalTab>> secondSignalCellFactory = getSignalCellFactory();

		filterSelect.setItems(filters);
		windowSelect.setItems(windows);
		Callback<ListView<Filter>, ListCell<Filter>> filterCellFactory = getFilterCellFactory();
		Callback<ListView<Window>, ListCell<Window>> windowCellFactory = getWindowCellFactory();

		filterSelect.setButtonCell(signalCellFactory.call(null));
		filterSelect.setCellFactory(signalCellFactory);
		windowSelect.setButtonCell(secondSignalCellFactory.call(null));
		windowSelect.setCellFactory(secondSignalCellFactory);

		filterSelect.setButtonCell(filterCellFactory.call(null));
		filterSelect.setCellFactory(filterCellFactory);

		windowSelect.setButtonCell(windowCellFactory.call(null));
		windowSelect.setCellFactory(windowCellFactory);


		vPane.getChildren().addAll(labelOperation, filterSelect, labelWindow, windowSelect,
				labelRzad, rzadTextField, labelCzestotliwoscOdciecia, czestotliwoscOdcieciatextField, labelCzestotlProbkow, czestotliwoscProbkowaniatextField,
				generateButton, distanceButton);
	}

	private Callback<ListView<Filter>, ListCell<Filter>> getFilterCellFactory() {
		return new Callback<ListView<Filter>, ListCell<Filter>>() {
			@Override
			public ListCell<Filter> call(ListView<Filter> l) {
				return new ListCell<Filter>() {

					@Override
					protected void updateItem(Filter item, boolean empty) {
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

	private Callback<ListView<Window>, ListCell<Window>> getWindowCellFactory() {
		return new Callback<ListView<Window>, ListCell<Window>>() {
			@Override
			public ListCell<Window> call(ListView<Window> l) {
				return new ListCell<Window>() {

					@Override
					protected void updateItem(Window item, boolean empty) {
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
//		Object firstTab = firstSignalSelect.getSelectionModel().getSelectedItem();


		if (filledInputs()) {
			try {
				Integer rzad = Integer.valueOf(rzadTextField.getText());
				Double czestotliwoscOdciecia = Double.valueOf(czestotliwoscOdcieciatextField.getText());
				Double czestotliwoscProbkowania = Double.valueOf(czestotliwoscProbkowaniatextField.getText());

				Filter selectedFilter = (Filter) filterSelect.getSelectionModel().getSelectedItem();
				Window selectedWindow = (Window) windowSelect.getSelectionModel().getSelectedItem();

				Signal signal = selectedWindow.execute(selectedFilter, rzad, czestotliwoscOdciecia, czestotliwoscProbkowania);
				TabsModel.INSTANCE.addTab(signal);
			} catch (OperationException ex) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("BŁĄD");
				alert.setContentText(ex.getMessage());
				alert.showAndWait();
			}
		}
	}

	private boolean filledInputs() {
		if (rzadTextField.getText() == null) {
			return false;
		}
		if (czestotliwoscOdcieciatextField.getText() == null) {
			return false;
		}
		if (czestotliwoscProbkowaniatextField.getText() == null) {
			return false;
		}
		if (filterSelect.getSelectionModel().getSelectedItem() == null) {
			return false;
		}
		if (windowSelect.getSelectionModel().getSelectedItem() == null) {
			return false;
		}
		return true;
	}

}
