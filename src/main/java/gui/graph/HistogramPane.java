package gui.graph;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import model.signal.Signal;

import static addons.NumberFormatter.getIntegerFormatter;
import static addons.NumberFormatter.validInteger;

public class HistogramPane implements EventHandler {
	VBox vPanel;
	Chart chart;
	public TextField textFieldHistogramIntervals;
	public Button RefreshHistogram;
	Signal signal;


	public HistogramPane(Signal signal, Chart chart) {
		this.chart = chart;
		this.vPanel = new VBox();
		this.signal = signal;
		createView();

	}

	private void createView() {
		textFieldHistogramIntervals = new TextField();
		textFieldHistogramIntervals.setTextFormatter(getIntegerFormatter());
		RefreshHistogram = new Button("Przegeneruj Histogram");
		RefreshHistogram.setOnAction(this);

		vPanel.getChildren().addAll(chart, textFieldHistogramIntervals, RefreshHistogram);
	}


	public Node getView() {
		return vPanel;
	}


	@Override
	public void handle(Event event) {
		if (validInteger(textFieldHistogramIntervals.getText()) ) {
			int intervals = Integer.valueOf(textFieldHistogramIntervals.getText());
			signal.setIntervals(intervals);
			signal.getHistogram();

			chart = signal.getHistogram();
			vPanel.getChildren().remove(0);
			vPanel.getChildren().add(0, chart);
		}
	}
}
