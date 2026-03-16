package com.lulow.justtype.controller;

import com.lulow.justtype.view.SceneManager;
import com.lulow.justtype.view.WinView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class WinController {

    @FXML private AnchorPane rootPane;
    @FXML private Label      titleLabel;
    @FXML private Label      hintLabel;

    private WinView winView;

    @FXML
    public void initialize() {
        winView = new WinView(rootPane, titleLabel, hintLabel);

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)  onPlayAgain();
            if (event.getCode() == KeyCode.ESCAPE) onGoMenu();
        });

        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) rootPane.requestFocus();
        });
    }

    public void setup(HBox wordDisplay) {
        winView.setup(wordDisplay);
    }

    private void onPlayAgain() {
        winView.stop();
        try {
            SceneManager.getInstance().loadScene("game-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onGoMenu() {
        winView.stop();
        try {
            SceneManager.getInstance().loadScene("menu-view.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}