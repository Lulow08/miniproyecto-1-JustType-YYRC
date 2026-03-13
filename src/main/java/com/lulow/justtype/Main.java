package com.lulow.justtype;

import com.lulow.justtype.view.SceneManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
     public void start(Stage mainStage) throws IOException {

        Image favicon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/favicon.png")));

        mainStage.setTitle("JustType");
        mainStage.setResizable(false);
        mainStage.getIcons().add(favicon);

        SceneManager sceneManager = SceneManager.getInstance();
        sceneManager.setMainStage(mainStage);

        sceneManager.loadFonts();
        sceneManager.loadScene("game-view.fxml");
    }
}