package com.lulow.justtype;

import com.lulow.justtype.model.AppInitializer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage mainStage) throws IOException {
        new AppInitializer().start(mainStage);
    }
}
