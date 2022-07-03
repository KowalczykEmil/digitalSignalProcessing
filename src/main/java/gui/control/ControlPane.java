package gui.control;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ControlPane {
	VBox vPane;
	TabPane controlTabs;

	public ControlPane() {
		buildView();
	}

	public void buildView() {
		controlTabs = new TabPane();
		Tab generateSignalTab = new Tab("Generator", new GeneratorPane().getView());
		Tab operationTab = new Tab("Operacje", new OperationPane().getView());
		Tab singleOperationTab = new Tab("Operacje 2", new SingleSignalOperationPane().getView());
		Tab comparisonTab = new Tab("Porównanie", new ComparisonPane().getView());

		generateSignalTab.closableProperty().setValue(false);
		operationTab.closableProperty().setValue(false);

		controlTabs.getTabs().addAll(generateSignalTab, operationTab, singleOperationTab, comparisonTab);

		vPane = new VBox(controlTabs);
		vPane.setPrefWidth(350);
	}

	public Pane getView() {
		return vPane;
	}
}
