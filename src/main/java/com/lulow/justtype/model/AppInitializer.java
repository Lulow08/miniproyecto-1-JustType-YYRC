package com.lulow.justtype.model;

import com.lulow.justtype.view.SceneManager;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Handles the initial setup of the application.
 * Configures the primary stage and loads the first scene.
 */
public class AppInitializer {

    /** Application window title. */
    private static final String APP_TITLE    = "JustType";

    /** Path to the application icon. */
    private static final String FAVICON_PATH = "/icons/favicon.png";

    /** FXML file for the entry scene. */
    private static final String ENTRY_SCENE  = "menu-view.fxml";

    /**
     * Sets up the primary stage and navigates to the main menu.
     *
     * @param stage the primary stage to configure
     * @throws IOException if the entry scene cannot be loaded
     */
    public void start(Stage stage) throws IOException {
        Image favicon = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(FAVICON_PATH)
        ));

        stage.setTitle(APP_TITLE);
        stage.setResizable(false);
        stage.getIcons().add(favicon);

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setMainStage(stage);
        sceneManager.loadFonts();
        sceneManager.switchScene(ENTRY_SCENE);
    }
}