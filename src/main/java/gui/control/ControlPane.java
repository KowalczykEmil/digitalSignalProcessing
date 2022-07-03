package gui.control;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.TabsModel;

public class ControlPane {
	VBox vPane;
	TabsModel graphTabs;
	TabPane controlTabs;

	public ControlPane(TabsModel graphTabModel) {
		this.graphTabs = graphTabModel;
		buildView();
	}

	public void buildView() {
		controlTabs = new TabPane();
		Tab generateSignalTab = new Tab("Generator", new GeneratorPane(graphTabs).getView());
		Tab operationTab = new Tab("Arytemtyka", new OperationPane(graphTabs).getView());
		Tab singleOperationTab = new Tab("Operacje 2");
		Tab comparisonTab = new Tab("Porównanie");
		generateSignalTab.closableProperty().setValue(false);
		operationTab.closableProperty().setValue(false);

		controlTabs.getTabs().addAll(generateSignalTab, operationTab);

		vPane = new VBox(controlTabs);
		vPane.setPrefWidth(350);
	}

	public Pane getView () {
		return vPane;
	}
}
