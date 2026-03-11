package com.lulow.justtype.controller;

import com.lulow.justtype.model.GameLogic;
import com.lulow.justtype.model.timer.GameTimer;
import com.lulow.justtype.view.particles.ConfettiFX;
import com.lulow.justtype.view.GameView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class GameController {

    @FXML private TextField inputField;
    @FXML private HBox      wordDisplay;
    @FXML private Label     levelLabel;
    @FXML private Label     timerLabel;
    @FXML private AnchorPane rootPane;

    private final GameLogic gameLogic = new GameLogic();
    private GameView gameView;
    private GameTimer gameTimer;
    private ConfettiFX confetti;

    @FXML
    public void initialize() {
        gameView = new GameView(wordDisplay, levelLabel, timerLabel);
        gameTimer = new GameTimer(this::onTick, this::onTimeUp);
        confetti = new ConfettiFX(rootPane, 640, 360);

        gameLogic.nextWord();
        refreshUI();
        startTimer();

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

        if (correct) {
            gameLogic.levelUp();
            confetti.play(0.02);
        }
        else { gameLogic.reset(); }

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
        gameTimer.start(gameLogic.getMaxTimeForCurrentLevel());
    }

    private void refreshUI() {
        gameView.renderWord(gameLogic.getCurrentChars(), gameLogic.getCurrentLevel());
        gameView.updateHUD(gameLogic.getCurrentLevel(), gameLogic.getMaxTimeForCurrentLevel());
    }
}