package com.lulow.justtype.controller;

import com.lulow.justtype.model.GameLogic;
import com.lulow.justtype.view.GameView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class GameController {

    @FXML private TextField inputField;
    @FXML private HBox      wordDisplay;
    @FXML private Label     levelLabel;
    @FXML private Label     timerLabel;

    private final GameLogic gameLogic = new GameLogic();
    private GameView gameView;

    @FXML
    public void initialize() {
        gameView = new GameView(wordDisplay, levelLabel, timerLabel);
        gameLogic.nextWord();
        refreshUI();

        inputField.textProperty().addListener((obs, oldText, newText) ->
                gameView.colorizeChars(newText, gameLogic.getCurrentWord())
        );
    }

    @FXML
    private void onHandleSubmitButton() { submitAnswer(inputField.getText()); }

    @FXML
    private void onHandleEnter() { submitAnswer(inputField.getText()); }

    private void submitAnswer(String input) {
        boolean correct = gameLogic.processAnswer(input);

        if (correct) { gameLogic.levelUp(); }
        else { gameLogic.reset(); }

        gameLogic.nextWord();
        inputField.clear();
        refreshUI();
    }

    private void refreshUI() {
        gameView.renderWord(gameLogic.getCurrentChars());
        gameView.updateHUD(gameLogic.getCurrentLevel(), gameLogic.getTimeForCurrentLevel());
    }
}