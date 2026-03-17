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

public class LoseController {

    private static final Logger LOGGER = Logger.getLogger(LoseController.class.getName());

    @FXML private AnchorPane rootPane;
    @FXML private Label      titleLabel;
    @FXML private Label      answerLabel;
    @FXML private Label      statsLabel;
    @FXML private Label      hintLabel;

    private LoseView loseView;

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

    public void setup(int completedLevels, String playerAnswer, HBox wordDisplay) {
        loseView.setup(completedLevels, playerAnswer, wordDisplay);
    }

    private void onPlayAgain() {
        try {
            SceneManager.getInstance().switchScene("game-view.fxml");
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load game screen", exception);
        }
    }

    private void onGoMenu() {
        try {
            SceneManager.getInstance().switchScene("menu-view.fxml");
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load menu screen", exception);
        }
    }
}