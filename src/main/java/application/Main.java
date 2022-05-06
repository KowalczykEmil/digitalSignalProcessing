package application;

import javafx.application.Application;
import javafx.stage.Stage;
import stageCore.StageController;
import viewProperties.FrameConfiguration;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        StageController.buildStage(primaryStage, "/MainView.fxml", "Zadanie 1. Emil i Joasia",
                new FrameConfiguration(1300,700), "/Style.css");

        StageController.getApplicationStage().setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}
