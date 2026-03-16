package com.lulow.justtype.model;

import com.lulow.justtype.view.SceneManager;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AppInitializer {

    private static final String APP_TITLE   = "JustType";
    private static final String FAVICON_PATH = "/icons/favicon.png";
    private static final String ENTRY_SCENE = "menu-view.fxml";

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
        sceneManager.loadScene(ENTRY_SCENE);
    }
}