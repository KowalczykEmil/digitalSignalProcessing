package controller;

import javafx.scene.Node;
import model.TabsModel;
import gui.control.ControlPane;

public class ControlController {
	TabsModel graphTabModel;
	ControlPane controlView;

	public ControlController(TabsModel graphTabModel) {
		this.graphTabModel = graphTabModel;
		controlView = new ControlPane(graphTabModel);

	}


	public Node getView() {
		return controlView.getView();
	}
}
