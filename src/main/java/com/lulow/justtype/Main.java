package com.lulow.justtype;

import com.lulow.justtype.model.AppInitializer;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point of the JustType application.
 * Extends {@link Application} to launch the JavaFX runtime.
 */
public class Main extends Application {

    /**
     * Main method. Launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) { launch(args); }

    /**
     * JavaFX start method. Delegates initialization to {@link AppInitializer}.
     *
     * @param mainStage the primary stage provided by the JavaFX runtime
     * @throws IOException if the initial scene cannot be loaded
     */
    @Override
    public void start(Stage mainStage) throws IOException {
        new AppInitializer().start(mainStage);
    }
}