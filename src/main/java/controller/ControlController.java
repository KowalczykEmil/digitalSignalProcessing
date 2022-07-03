package controller;

import javafx.scene.Node;
import gui.control.ControlPane;

public class ControlController {
	ControlPane controlView;

	public ControlController() {
		controlView = new ControlPane();
	}

	public Node getView() {
		return controlView.getView();
	}
}
