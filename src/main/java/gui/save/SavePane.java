package gui.save;

import controller.BinaryController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.NoiseParam;
import model.TabsModel;
import gui.graph.SignalTab;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static model.noise.AbstractNoise.SAMPLE_DIST;

public class SavePane implements EventHandler {
	TabsModel graphTabModel;
	VBox vPane;
	private Button saveButton;
	private Slider samplingSlider;
	private ComboBox signalSelect;
	private Stage stage;

	public SavePane(TabsModel graphTabModel, Stage stage) {
		this.graphTabModel = graphTabModel;
		this.stage = stage;
		buildView();
	}

	private void buildView() {
		vPane = new VBox();
		vPane.setPadding(new Insets(15, 15, 15, 15));
		saveButton = new Button("Zapisz");
		saveButton.setOnMouseClicked(this);

		Label labelSignal = new Label("Sygnał:");
		Label labelSampling = new Label("Częstotliwość próbkowania [Hz]:");

		samplingSlider = prepareSlider(SAMPLE_DIST);

		signalSelect = new ComboBox();
		signalSelect.setItems(graphTabModel.getSignalPanes());
		Callback<ListView<SignalTab>, ListCell<SignalTab>> signalCellFactory = getSignalCellFactory();
		signalSelect.setButtonCell(signalCellFactory.call(null));
		signalSelect.setCellFactory(signalCellFactory);

		EventHandler<ActionEvent> event =
				new EventHandler<ActionEvent>() {
					public void handle(ActionEvent e) {
						SignalTab signal = (SignalTab) signalSelect.getSelectionModel().getSelectedItem();
						NoiseParam params = signal.getSignal().getNoiseParam();
						if (params != null && params.getSamplingPeriod() != null) {
							double SignalSampling = params.getSamplingPeriod();
							vPane.getChildren().remove(samplingSlider);
							samplingSlider = prepareSlider(SignalSampling);
							vPane.getChildren().add(3, samplingSlider);
						}

					}
				};

		signalSelect.setOnAction(event);

		vPane.getChildren().addAll(labelSignal, signalSelect, labelSampling, samplingSlider, saveButton);
	}


	private Slider prepareSlider(double sampling) {
		double samplingFrequency = 1 / sampling;
		Slider slider = new Slider(0, samplingFrequency, samplingFrequency);
		slider.setBlockIncrement(samplingFrequency/10);
		slider.setMajorTickUnit((samplingFrequency/10));
		slider.setMinorTickCount(0);
		slider.setShowTickLabels(true);
		slider.setSnapToTicks(true);

//		Slider slider = new Slider(100, 10000, 100);
//		slider.setBlockIncrement(100);
//		slider.setMajorTickUnit((100));
//		slider.setMinorTickCount(0);
//		slider.setShowTickLabels(true);
//		slider.setSnapToTicks(true);

//	    if (sampling <= 1 && sampling * 1000000 >= 1) {
//		    slider.setValue(1);
//	    }

		return slider;
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
		double samplingPeriod = 1 / samplingSlider.getValue();
		SignalTab signal = (SignalTab) signalSelect.getSelectionModel().getSelectedItem();

		if (signal != null) {
			BinaryController binaryController = new BinaryController();

			DirectoryChooser directoryChooser = new DirectoryChooser();
			File selectedDirectory = directoryChooser.showDialog(stage);
			if (selectedDirectory != null) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
				Date date = new Date();
				String filename = selectedDirectory + "\\" + formatter.format(date) + "_tabs.bin";
				byte[] data = binaryController.getDataFromTab(signal, samplingPeriod);

				File file = new File(filename);
				try (FileOutputStream outputStream = new FileOutputStream(file)) {
					outputStream.write(data);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				stage.close();

			}
		}
	}

}
