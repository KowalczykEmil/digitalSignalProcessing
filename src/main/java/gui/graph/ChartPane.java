package gui.graph;

import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;

public class ChartPane {
	VBox vPanel;
	Chart chart;


	public ChartPane(Chart chart) {
		this.chart = chart;
		this.vPanel = new VBox();
		createView();

	}

private void createView(){
	if (chart != null) {
		vPanel.getChildren().addAll(chart);
	}
}


	public Node getView(){
		return vPanel;
	}

	public void setChart(Chart chart) {
		this.chart = chart;
		vPanel.getChildren().remove(0);
		vPanel.getChildren().add(0, chart);
	}
}
