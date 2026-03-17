package com.lulow.justtype.controller;

import com.lulow.justtype.view.SceneManager;
import com.lulow.justtype.view.WinView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the win screen.
 * Receives game result data from {@link GameController}, delegates rendering
 * to {@link WinView}, and handles navigation back to the game or main menu
 * via keyboard input.
 */
public class WinController {

    private static final Logger LOGGER = Logger.getLogger(WinController.class.getName());

    @FXML private AnchorPane rootPane;
    @FXML private Label      titleLabel;
    @FXML private Label      statsLabel;
    @FXML private Label      hintLabel;

    /** Manages the visual setup and animations of the win screen. */
    private WinView winView;

    /**
     * Initializes the controller after FXML injection.
     * Creates the win view and registers keyboard handlers for ENTER and ESCAPE.
     */
    @FXML
    public void initialize() {
        winView = new WinView(rootPane, titleLabel, statsLabel, hintLabel);

        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) rootPane.requestFocus();
        });

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)  onPlayAgain();
            if (event.getCode() == KeyCode.ESCAPE) onGoMenu();
        });
    }

    /**
     * Populates the win screen with game result data.
     * Called by {@link GameController} after navigating to this screen.
     *
     * @param completedLevels the number of levels the player completed
     * @param timeRemaining   seconds remaining when the final level was passed
     * @param wordDisplay     the word display HBox carried over from the game screen
     */
    public void setup(int completedLevels, int timeRemaining, HBox wordDisplay) {
        winView.setup(completedLevels, timeRemaining, wordDisplay);
    }

    /**
     * Stops the win screen animations and restarts the game.
     */
    private void onPlayAgain() {
        winView.stop();
        try {
            SceneManager.getInstance().switchScene("game-view.fxml");
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load game screen", exception);
        }
    }

    /**
     * Stops the win screen animations and returns to the main menu.
     */
    private void onGoMenu() {
        winView.stop();
        try {
            SceneManager.getInstance().switchScene("menu-view.fxml");
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load menu screen", exception);
        }
    }
}