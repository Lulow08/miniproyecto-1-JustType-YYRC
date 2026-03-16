package com.lulow.justtype.view;

import com.lulow.justtype.model.AudioManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class SceneManager {

    private static SceneManager instance;
    private Stage mainStage;

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) instance = new SceneManager();
        return instance;
    }

    public void setMainStage(Stage stage) { this.mainStage = stage; }

    public void loadFonts() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/Determination-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/GeistMonoNerdFont-Regular.otf"), 14);
    }

    public <T> T loadScene(String fxmlFile) throws IOException {
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