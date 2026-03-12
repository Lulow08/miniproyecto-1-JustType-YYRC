package com.lulow.justtype.controller;

import com.lulow.justtype.model.GameLogic;
import com.lulow.justtype.model.timer.GameTimer;
import com.lulow.justtype.view.particles.ConfettiFX;
import com.lulow.justtype.view.GameView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class GameController {

    @FXML private TextField inputField;
    @FXML private HBox      wordDisplay;
    @FXML private Label     levelLabel;
    @FXML private Label     timerLabel;
    @FXML private Button    submitButton;
    @FXML private AnchorPane rootPane;
    @FXML private StackPane  timerPane;

    private GameLogic gameLogic;
    private GameView gameView;
    private GameTimer gameTimer;
    private ConfettiFX confetti;

    @FXML
    public void initialize() {
        gameLogic = new GameLogic();
        gameView = new GameView(wordDisplay, levelLabel, timerLabel, timerPane, rootPane);
        gameTimer = new GameTimer(this::onTick, this::onTimeUp);
        confetti = new ConfettiFX(rootPane, rootPane.getPrefWidth(), rootPane.getPrefHeight());

        gameView.setupAnimations(inputField, submitButton);

        inputField.textProperty().addListener((observable, oldText, newText) ->
                gameView.colorizeChars(newText, gameLogic.getCurrentWord())
        );

        gameLogic.nextWord();
        refreshUI();
        startTimer();
    }

    @FXML private void onSubmitButtonClicked() { submitAnswer(inputField.getText()); }
    @FXML private void onEnterPressed() { submitAnswer(inputField.getText()); }

    private void submitAnswer(String input) {
        gameView.playInputAnimation();
        gameTimer.stop();

        boolean isCorrect = gameLogic.processAnswer(input);

        if (isCorrect) {
            gameLogic.levelUp();
            confetti.play(0.02);
        } else { gameLogic.reset(); }

        nextRound();
    }

    private void onTick() {
        gameView.updateHUD(gameLogic.getCurrentLevel(), gameTimer.getSecondsLeft());
    }

    private void onTimeUp() {
        gameLogic.reset();
        nextRound();
    }

    private void nextRound() {
        gameLogic.nextWord();
        inputField.clear();
        refreshUI();
        startTimer();
    }

    private void startTimer() {
        int maxTime = gameLogic.getMaxTimeForCurrentLevel();
        gameView.initTimer(maxTime);
        gameTimer.start(maxTime);
    }

    private void refreshUI() {
        gameView.renderWord(gameLogic.getCurrentChars(), gameLogic.getCurrentLevel());
        gameView.updateHUD(gameLogic.getCurrentLevel(), gameLogic.getMaxTimeForCurrentLevel());
    }
}