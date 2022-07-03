import controller.ControlController;
import controller.GraphTabController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.TabsModel;
import gui.top_bar.TopMenuBar;

public class ApplicationCPS extends Application {

	@Override
	public void start(Stage stage) {

		TabsModel graphTabModel = new TabsModel();
		ControlController controlController = new ControlController();
		GraphTabController graphTabController = new GraphTabController();
		TopMenuBar topBar = new TopMenuBar(stage);

		BorderPane layout = new BorderPane();
		layout.setPadding(new Insets(15,20,10,10));
		layout.setTop(topBar.getView());
		layout.setCenter(controlController.getView());
		layout.setBottom(graphTabController.getView());


		Scene scene = new Scene(layout, 1400, 780);
		stage.setMinWidth(1400);
		stage.setMinHeight(780);

		stage.setScene(scene);
		stage.setTitle("Zadanie 3/4 - Emil i Joasia");
		scene.getStylesheets().add("style.css");

		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}
