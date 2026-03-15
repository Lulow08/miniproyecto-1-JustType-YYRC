package com.lulow.justtype.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class SceneManager {

    private static SceneManager instance;
    private Stage mainStage;

    private String loseAnswer = "";
    private HBox loseWordDisplay = null;

    private SceneManager() {}

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public void setMainStage(Stage stage) { this.mainStage = stage; }

    public void setLoseData(String answer) {
        this.loseAnswer = answer;
    }

    public void setLoseWordDisplay(HBox wordDisplay) { this.loseWordDisplay = wordDisplay; }

    public String getLoseAnswer() { return loseAnswer; }
    public HBox getLoseWordDisplay() {
        HBox node = loseWordDisplay;
        loseWordDisplay = null;
        return node;
    }

    public void loadFonts() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/Determination-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/GeistMonoNerdFont-Regular.otf"), 14);
    }

    public void loadScene(String fxmlFile) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm());
        mainStage.setScene(scene);

        mainStage.show();
    }
}
