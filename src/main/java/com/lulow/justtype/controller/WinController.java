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

public class WinController {

    private static final Logger LOGGER = Logger.getLogger(WinController.class.getName());

    @FXML private AnchorPane rootPane;
    @FXML private Label      titleLabel;
    @FXML private Label      statsLabel;
    @FXML private Label      hintLabel;

    private WinView winView;

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

    public void setup(int completedLevels, int timeRemaining, HBox wordDisplay) {
        winView.setup(completedLevels, timeRemaining, wordDisplay);
    }

    private void onPlayAgain() {
        winView.stop();
        try {
            SceneManager.getInstance().switchScene("game-view.fxml");
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load game screen", exception);
        }
    }

    private void onGoMenu() {
        winView.stop();
        try {
            SceneManager.getInstance().switchScene("menu-view.fxml");
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load menu screen", exception);
        }
    }
}