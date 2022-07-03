package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import model.TabsModel;

public class GraphTabController implements ChangeListener<Tab> {
	TabsModel graphTabModel;
	VBox vPanel;


	public GraphTabController(TabsModel graphTabModel) {
		this.graphTabModel = graphTabModel;
		buildView();
	}

	public void buildView() {
		vPanel = new VBox();
		vPanel.getChildren().add(graphTabModel.getTabs());
	}


	public Node getView() {
		return vPanel;
	}

	@Override
	public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab t1) {

	}
}
