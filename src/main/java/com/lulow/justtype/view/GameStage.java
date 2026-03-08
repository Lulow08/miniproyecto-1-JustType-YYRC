package com.lulow.justtype.view;

import com.lulow.justtype.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameStage {
    public GameStage(Stage gameStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/game-view.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        gameStage.setTitle("JustType");
        gameStage.setScene(scene);
        gameStage.show();
    }
}
