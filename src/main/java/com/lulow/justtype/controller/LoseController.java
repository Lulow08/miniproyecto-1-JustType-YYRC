package com.lulow.justtype.controller;

import com.lulow.justtype.view.LoseView;
import com.lulow.justtype.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the loose screen.
 * Receives game result data from {@link GameController}, delegates rendering
 * to {@link LoseView}, and handles navigation back to the game or main menu
 * via keyboard input.
 */
public class LoseController {

    private static final Logger LOGGER = Logger.getLogger(LoseController.class.getName());

    @FXML private AnchorPane rootPane;
    @FXML private Label      titleLabel;
    @FXML private Label      answerLabel;
    @FXML private Label      statsLabel;
    @FXML private Label      hintLabel;

    /** Manages the visual setup and animations of the lose screen. */
    private LoseView loseView;

    /**
     * Initializes the controller after FXML injection.
     * Creates the loose view and registers keyboard handlers for ENTER and ESCAPE.
     */
    @FXML
    public void initialize() {
        loseView = new LoseView(rootPane, titleLabel, answerLabel, statsLabel, hintLabel);

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)  onPlayAgain();
            if (event.getCode() == KeyCode.ESCAPE) onGoMenu();
        });

        rootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) rootPane.requestFocus();
        });
    }

    /**
     * Populates the loose screen with game result data.
     * Called by {@link GameController} after navigating to this screen.
     *
     * @param completedLevels the number of levels the player completed
     * @param playerAnswer    the text the player had typed when the game ended
     * @param wordDisplay     the word display HBox carried over from the game screen
     */
    public void setup(int completedLevels, String playerAnswer, HBox wordDisplay) {
        loseView.setup(completedLevels, playerAnswer, wordDisplay);
    }

    /**
     * Restarts the game by navigating to the game screen.
     */
    private void onPlayAgain() {
        try {
            SceneManager.getInstance().switchScene("game-view.fxml");
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load game screen", exception);
        }
    }

    /**
     * Returns to the main menu by navigating to the menu screen.
     */
    private void onGoMenu() {
        try {
            SceneManager.getInstance().switchScene("menu-view.fxml");
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load menu screen", exception);
        }
    }
}