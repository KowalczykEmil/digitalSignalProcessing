package gui.graph;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.signal.Signal;

public class SignalTab extends Tab {
	private int index;
	Signal signal;
	VBox vPanel;
	Node tabContent;

	public SignalTab(String header, Node tabContent, Signal signal, int tabId) {
		super(header, tabContent);
		this.tabContent = tabContent;
		this.signal = signal;
		this.index = tabId;
		vPanel = new VBox();
		vPanel.getChildren().add(tabContent);
	}

	public Pane getView() {
		return vPanel;
	}


	public Signal getSignal() {
		return signal;
	}

	public void setSignal(Signal signal) {
		this.signal = signal;
		signal.getChart();
	}

	public int getIndex() {
		return index;
	}
}
