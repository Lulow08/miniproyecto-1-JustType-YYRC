package com.lulow.justtype.controller;

import com.lulow.justtype.view.LoseView;
import com.lulow.justtype.view.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class LoseController {

    @FXML private AnchorPane rootPane;
    @FXML private Label      titleLabel;
    @FXML private Label      answerLabel;
    @FXML private Label      hintLabel;

    private LoseView loseView;

    @FXML
    public void initialize() {
        loseView = new LoseView(rootPane, titleLabel, answerLabel, hintLabel);

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)  onContinue();
            if (event.getCode() == KeyCode.ESCAPE) onGoMenu();
        });

        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) rootPane.requestFocus();
        });
    }

    public void setup(String word, String wrongAnswer, HBox wordDisplay) {
        loseView.setup(wrongAnswer, wordDisplay);
    }

    private void onContinue() {
        try {
            SceneManager.getInstance().loadScene("game-view.fxml");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void onGoMenu() {
        try {
            SceneManager.getInstance().loadScene("menu-view.fxml");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}