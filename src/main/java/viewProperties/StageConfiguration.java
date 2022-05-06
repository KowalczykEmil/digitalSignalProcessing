package viewProperties;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageConfiguration {
    /*------------------------ FIELDS REGION ------------------------*/
    private static Stage applicationStage;
    private static FrameConfiguration frameConfiguration;
    private static String globalCssStyling;

    /*------------------------ METHODS REGION ------------------------*/
    private StageConfiguration() {
    }

    public static Stage getApplicationStage() {
        return applicationStage;
    }

    public static void setApplicationStage(Stage applicationStage) {
        StageConfiguration.applicationStage = applicationStage;
    }

    public static FrameConfiguration getFrameConfiguration() {
        return frameConfiguration;
    }

    public static void setFrameConfiguration(FrameConfiguration frameConfiguration ) {
        StageConfiguration.frameConfiguration = frameConfiguration;
    }

    public static String getGlobalCssStyling() {
        return globalCssStyling;
    }

    public static void setGlobalCssStyling(String globalCssStyling) {
        StageConfiguration.globalCssStyling = globalCssStyling;
    }

    /**
     * Method load fxml file.
     */
    private static Parent loadFxml(String fxml) throws IOException {
        return new FXMLLoader(StageConfiguration.class.getResource(fxml)).load();
    }

    /**
     * Method prepare Stage by setting all required parameters.
     */
    private static void prepareStage(String filePath, String title,
                                     FrameConfiguration proportions) throws IOException {
        Scene scene = new Scene(loadFxml(filePath));
        if (globalCssStyling != null) {
            scene.getStylesheets().add(globalCssStyling);
        }

        applicationStage.setScene(scene);
        applicationStage.setTitle(title);
        applicationStage.setWidth(proportions.getWidth());
        applicationStage.setHeight(proportions.getHeight());
        applicationStage.show();
    }

    /**
     * Method load stage from scratch and set `applicationStage` - use on startup of application
     * stage is passed from start method from Main class.
     */
    public static void buildStage(Stage stage, String filePath,
                                  String title, FrameConfiguration dimensions,
                                  String cssFilePath) throws IOException {
        setApplicationStage(stage);
        setFrameConfiguration(dimensions);
        setGlobalCssStyling(cssFilePath);
        prepareStage(filePath, title, frameConfiguration);
    }

    /**
     * Method load new stage and set `applicationStage` to a new one but leave the previous one
     * open.
     */
    public static void loadStage(String filePath, String title) throws IOException {
        setApplicationStage(new Stage());
        prepareStage(filePath, title, frameConfiguration);
    }

    /**
     * Method close previous stage and load new stage and set `applicationStage` to a new one.
     */
    public static void reloadStage(String filePath, String title) throws IOException {
        applicationStage.close();
        loadStage(filePath, title);
    }
}
