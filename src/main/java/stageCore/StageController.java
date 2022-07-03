package stageCore;

import exceptions.Announcements;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import viewProperties.FrameConfiguration;
import viewProperties.StageConfiguration;

import java.io.IOException;


public class StageController {
    public static Stage getApplicationStage() {
        return StageConfiguration.getApplicationStage();
    }

    public static FrameConfiguration getFrameConfiguration() {
        return StageConfiguration.getFrameConfiguration();
    }

    public static String getGlobalCssStyling() {
        return StageConfiguration.getGlobalCssStyling();
    }

    public static void buildStage(Stage stage, String filePath, String title,
                                  FrameConfiguration configuration, String cssFilePath) {
        try {
            StageConfiguration.buildStage(stage, filePath, title, configuration, cssFilePath);
        } catch (IOException | IllegalStateException e) {
            Announcements.messageBox("Stage Building Error",
                    "Stage cannot be properly built", Alert.AlertType.ERROR);
        }
    }

    public static void loadStage(String filePath, String title) {
        try {
            StageConfiguration.loadStage(filePath, title);
        } catch (IOException | IllegalStateException e) {
            Announcements.messageBox("Stage Loading Error",
                    "Stage cannot be properly loaded", Alert.AlertType.ERROR);
        }
    }

    public static void reloadStage(String filePath, String title) {
        try {
            StageConfiguration.reloadStage(filePath, title);
        } catch (IOException | IllegalStateException e) {
            Announcements.messageBox("Stage Reloading Error",
                    "Stage cannot be properly reloaded", Alert.AlertType.ERROR);
        }
    }

}
