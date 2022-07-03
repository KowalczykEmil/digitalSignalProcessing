package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.Chart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.signal.Signal;
import gui.graph.ChartPane;
import gui.graph.HistogramPane;
import gui.graph.SignalTab;

import java.util.ArrayList;

public class TabsModel {
	private int numberOfGeneratedTabs = 0;
	private TabPane tabs;
	private ObservableList<SignalTab> signalPanes;
	public static TabsModel INSTANCE;

	public TabsModel() {
		tabs = new TabPane();
		signalPanes = FXCollections.observableArrayList(new ArrayList<>());
		this.INSTANCE = this;
	}

	public void addTab(Signal signal) {
		addTab(buildSignalTabContent(signal), signal, "Karta " + (numberOfGeneratedTabs + 1));
	}

	public void addTab(Node tabContent, Signal signal, String header) {
		int index = tabs.getTabs().size();
		SignalTab signalTab = new SignalTab(header, tabContent, signal, numberOfGeneratedTabs + 1);
		setOnTabClosedListener(signalTab);
		signalPanes.add(index, signalTab);
		tabs.getTabs().add(index, signalTab);
		tabs.getSelectionModel().select(signalTab);
		numberOfGeneratedTabs++;
	}

	private void setOnTabClosedListener(Tab tab) {
		tab.setOnCloseRequest(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				Tab selectedTab = tabs.getSelectionModel().getSelectedItem();
				if (selectedTab != null) {
					signalPanes.remove(selectedTab);
				}
			}
		});
	}

	public Node buildSignalTabContent(Signal signal) {
		Chart chart = null;
		Chart histogram = null;
		Label characteristics = null;
		if (signal != null) {
			long startTime = System.nanoTime();

			chart = signal.getChart();
			histogram = signal.getHistogram();
			System.out.println("HISTOGRAM: " + ( System.nanoTime() - startTime) /100000);

			characteristics = new Label(signal.getCharacteristics());
		}


		TabPane tabs = new TabPane();
		ChartPane chartPane = new ChartPane(chart);
		Tab graphTab = new Tab("Wykres", chartPane.getView());
		graphTab.closableProperty().setValue(false);

		HistogramPane histogramPane = new HistogramPane(signal, histogram);
		Tab histogramTab = new Tab("Histogram", histogramPane.getView());
		histogramTab.closableProperty().setValue(false);

		Tab characteristicsTab = new Tab("Charakterystyka", characteristics);
		characteristicsTab.closableProperty().setValue(false);

		tabs.getTabs().add(graphTab);
		tabs.getTabs().add(histogramTab);
		tabs.getTabs().add(characteristicsTab);

		return new VBox(tabs);
	}

	public TabPane getTabs() {
		return tabs;
	}

	public ObservableList<SignalTab> getSignalPanes() {
		return signalPanes;
	}
}
