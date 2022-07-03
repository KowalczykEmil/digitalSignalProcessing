package gui.control;

import loader.FileLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import model.NoiseParam;
import model.TabsModel;
import model.noise.*;
import model.signal.Signal;

import java.util.ArrayList;

import static addons.NumberFormatter.*;

public class GeneratorPane implements EventHandler {
	VBox vPane;
	FileLoader editor;
	private ObservableList<Noise> noises;
	private Signal signal;

	public GeneratorPane() {
		this.vPane = new VBox();
		vPane.setPadding(new Insets(10,10,10,10));
		registerNoises();
		editor = buildView();
//		buildView();
	}

	private void registerNoises() {
		ArrayList<Noise> noisess = new ArrayList();
		noisess.add(new GaussianNoise());
		noisess.add(new SinusoidalNoise());
		noisess.add(new SinusoidalHalfRectifiedNoise());
		noisess.add(new SinusoidalTwoHalfRectifiedNoise());
		noisess.add(new UnitImpulse());
		noisess.add(new RectangularNoise());
		noisess.add(new RectangularSymmetricNoise());
		noisess.add(new UnitJump());
		noisess.add(new TriangularNoise());
		noisess.add(new UniformNoise());
		noisess.add(new ImpulseNoise());

		noises = FXCollections.observableArrayList(noisess);
	}

	private FileLoader buildView() {
		editor = new FileLoader();
		//Tak na prawdę ta klasa to jest controller, być może wynieść tą metodę do klasy Widoku
		Label signalTypeLabel = new Label("Rodzaj sygnału:");
		editor.signalTypeSelection = new ComboBox();
		editor.signalTypeSelection.setItems(noises);
		Callback<ListView<Noise>, ListCell<Noise>> cellFactory = new Callback<ListView<Noise>, ListCell<Noise>>() {
			@Override
			public ListCell<Noise> call(ListView<Noise> l) {
				return new ListCell<Noise>() {

					@Override
					protected void updateItem(Noise item, boolean empty) {
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

		EventHandler<ActionEvent> event =
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						((Noise) editor.signalTypeSelection.getSelectionModel().getSelectedItem()).setInputVisibility(editor);
					}
				};

		editor.signalTypeSelection.setOnAction(event);

		editor.signalTypeSelection.setButtonCell(cellFactory.call(null));
		editor.signalTypeSelection.setCellFactory(cellFactory);

		editor.generateButton = new Button("Generuj");
		editor.generateButton.setOnAction(this);

		editor.labelAmplitude = new Label("Amplituda");
		editor.labelInitialTime = new Label("Czas początkowy");
		editor.labelDuration = new Label("Czas trwania sygnału");
		editor.labelBasePeriod = new Label("Okres podstawowy");
		editor.labelFillFactor = new Label("Współczynnik wypełnienia");
		editor.labelHistogramIntervals = new Label("Ilość przedziałów histogramu");
		editor.labelFrequency = new Label("Częstotliwośc próbkowania");

		editor.textFieldAmplitude = new TextField("1");
		editor.textFieldInitialTime = new TextField("0");
		editor.textFieldDuration = new TextField("1");
		editor.textFieldBasePeriod = new TextField("0.1");
		editor.textFieldFillFactor = new TextField("0.5");
		editor.textFieldHistogramIntervals = new TextField("10");

		editor.textFieldFrequency = new TextField("10000");

		setFieldFormatters();
		setListeners();

		vPane.getChildren().addAll(signalTypeLabel, editor.signalTypeSelection,
				editor.labelAmplitude, editor.textFieldAmplitude,
				editor.labelInitialTime, editor.textFieldInitialTime,
				editor.labelDuration, editor.textFieldDuration,
				editor.labelBasePeriod, editor.textFieldBasePeriod,
				editor.labelFillFactor, editor.textFieldFillFactor,
				editor.labelHistogramIntervals, editor.textFieldHistogramIntervals,
				editor.labelFrequency, editor.textFieldFrequency,
				editor.generateButton);

		return editor;
	}

	private void setListeners() {
		editor.textFieldInitialTime.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue,
			                    String newValue) {
				if (newValue.matches("-?\\d*(\\.)\\d{2}")) {
					editor.textFieldInitialTime.setText(String.valueOf(((Math.round(Double.valueOf(newValue) / AbstractNoise.SAMPLE_DIST)) * AbstractNoise.SAMPLE_DIST)));
				}
			}
		});
	}

	private void setFieldFormatters() {
		editor.textFieldAmplitude.setTextFormatter(getDecimalFormatter());
		editor.textFieldInitialTime.setTextFormatter(getDecimalFormatter());
		editor.textFieldDuration.setTextFormatter(getDecimalFormatter());
		editor.textFieldBasePeriod.setTextFormatter(getDecimalFormatter());
		editor.textFieldFillFactor.setTextFormatter(getDecimalFormatter());
		editor.textFieldHistogramIntervals.setTextFormatter(getIntegerFormatter());
	}

	private NoiseParam getDataFromView() {
		NoiseParam noiseParam = new NoiseParam();
		noiseParam.setAmplitude(Double.valueOf(editor.textFieldAmplitude.getText()));
		noiseParam.setInitialTime(Double.valueOf(editor.textFieldInitialTime.getText()));
		noiseParam.setDuration(Double.valueOf(editor.textFieldDuration.getText()));
		noiseParam.setBasePeriod(Double.valueOf(editor.textFieldBasePeriod.getText()));
		noiseParam.setFillFactor(Double.valueOf(editor.textFieldFillFactor.getText()));
		noiseParam.setSamplingPeriod(1/Double.valueOf(editor.textFieldFrequency.getText()));

		return noiseParam;
	}

	public Pane getView() {
		return vPane;
	}

	@Override
	public void handle(Event event) {
		Noise selectedNoise = (Noise) editor.signalTypeSelection.getSelectionModel().getSelectedItem();
		if (selectedNoise == null || !validInteger(editor.textFieldHistogramIntervals.getText())) { //todo zabezpieczenia na inne pola?
			return;
		}
		selectedNoise.setParams(getDataFromView());
		int intervals = Integer.valueOf(editor.textFieldHistogramIntervals.getText());

		signal = selectedNoise.generate();
		signal.setIntervals(intervals);
		TabsModel.INSTANCE.addTab(signal);
	}
}
