package gui.topBar;

import controller.BinaryController;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.TabsModel;
import model.signal.Signal;
import gui.save.SavePane;

import java.io.File;
import java.util.List;

public class TopMenuBar {
	private MenuBar menuBar;
	Stage stage;
	BinaryController binaryController;

	public TopMenuBar(Stage stage) {
		this.stage = stage;
		binaryController = new BinaryController();
		buildView();
	}

	private void buildView() {
		menuBar = new MenuBar();
		Menu fileMenu = new Menu("Plik");

		MenuItem save = new MenuItem("Zapisz");
		MenuItem open = new MenuItem("Wczytaj");

		fileMenu.getItems().addAll(save, open);

		open.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			File selectedFile = fileChooser.showOpenDialog(stage);
			List<Signal> signals = binaryController.getSignalsFromData(selectedFile.getAbsolutePath());

			for (Signal signal : signals) {
				TabsModel.INSTANCE.addTab(signal);
			}
		});

		save.setOnAction(e -> {
			Stage saveWindow = new Stage();
			saveWindow.setTitle("Zapis");
			StackPane root = new StackPane();
			SavePane pane = new SavePane(TabsModel.INSTANCE, saveWindow);
			root.getChildren().add(pane.getView());
			Scene scene = new Scene(root, 500, 250);
			saveWindow.setScene(scene);
			saveWindow.show();
		});
		menuBar.getMenus().addAll(fileMenu);
	}


	public Node getView() {
		return menuBar;
	}
}
