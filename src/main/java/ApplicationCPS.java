import controller.ControlController;
import controller.GraphTabController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.TabsModel;
import gui.topBar.TopMenuBar;

public class ApplicationCPS extends Application {

	@Override
	public void start(Stage primaryStage) {

		TabsModel graphTabModel = new TabsModel();
		ControlController controlController = new ControlController(graphTabModel);
		GraphTabController graphTabController = new GraphTabController(graphTabModel);
		TopMenuBar topBar = new TopMenuBar(primaryStage, graphTabModel);

		BorderPane layout = new BorderPane();
		layout.setPadding(new Insets(15,20,10,10));
		layout.setTop(topBar.getView());
		layout.setCenter(controlController.getView());
		layout.setBottom(graphTabController.getView());

		Scene scene = new Scene(layout, 1600, 800);
		primaryStage.setMinWidth(1400);
		primaryStage.setMinHeight(780);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Zadanie 1 - Emil i Joanna");
		scene.getStylesheets().add("style.css");

		primaryStage.show();

	}

	public static void main(String[] args) {
		launch();
	}

}
