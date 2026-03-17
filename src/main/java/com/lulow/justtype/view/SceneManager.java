package com.lulow.justtype.view;

import com.lulow.justtype.model.AudioManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Singleton that manages scene transitions on the primary {@link Stage}.
 * Also handles font loading and stylesheet injection for every new scene.
 */
public final class SceneManager {

    /** The single instance of this manager. */
    private static SceneManager instance;

    /** The application's primary stage. */
    private Stage mainStage;

    /** Private constructor to enforce singleton pattern. */
    private SceneManager() {}

    /**
     * Returns the singleton instance, creating it on first call.
     *
     * @return the shared {@code SceneManager}
     */
    public static SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }

    /**
     * Sets the primary stage that scenes will be displayed on.
     *
     * @param stage the application's main stage
     */
    public void setMainStage(Stage stage) { this.mainStage = stage; }

    /**
     * Pre-loads the custom fonts used throughout the application.
     * Must be called once before any scene is shown.
     */
    public void loadFonts() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/Determination-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/GeistMonoNerdFont-Regular.otf"), 14);
    }

    /**
     * Loads an FXML file, attaches the global stylesheet, sets it as the current scene,
     * and returns the associated controller. Also stops all audio before switching.
     *
     * @param <SceneController> the type of the FXML controller
     * @param fxmlFile          the FXML file name (looked up under {@code /fxml/})
     * @return the controller instance created by the {@link FXMLLoader}
     * @throws IOException if the FXML file cannot be found or parsed
     */
    public <SceneController> SceneController switchScene(String fxmlFile) throws IOException {
        AudioManager.getInstance().stopAll();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm()
        );

        mainStage.setScene(scene);
        mainStage.show();

        return loader.getController();
    }
}