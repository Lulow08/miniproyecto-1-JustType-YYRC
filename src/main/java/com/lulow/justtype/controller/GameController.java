package com.lulow.justtype.controller;

import com.lulow.justtype.model.AudioClips;
import com.lulow.justtype.model.AudioManager;
import com.lulow.justtype.model.GameLogic;
import com.lulow.justtype.model.timer.GameTimer;
import com.lulow.justtype.view.SceneManager;
import com.lulow.justtype.view.particles.ConfettiFX;
import com.lulow.justtype.view.GameView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class GameController {

    @FXML private TextField  inputField;
    @FXML private HBox       wordDisplay;
    @FXML private HBox       timerPill;
    @FXML private Label      levelLabel;
    @FXML private Label      timerLabel;
    @FXML private Button     submitButton;
    @FXML private AnchorPane rootPane;
    @FXML private StackPane  timerPane;

    private GameLogic  gameLogic;
    private GameView   gameView;
    private GameTimer  gameTimer;
    private ConfettiFX confetti;

    @FXML
    public void initialize() {
        gameLogic = new GameLogic();
        gameView  = new GameView(wordDisplay, levelLabel, timerLabel, timerPane, rootPane, timerPill);
        gameTimer = new GameTimer(this::onTick, this::onTimeUp);
        confetti  = new ConfettiFX(rootPane, rootPane.getPrefWidth(), rootPane.getPrefHeight());

        gameView.setupAnimations(inputField, submitButton);

        inputField.textProperty().addListener((obs, oldText, newText) -> {
            gameView.colorizeChars(newText, gameLogic.getCurrentWord());
            if (newText.length() != oldText.length()) {
                AudioManager.getInstance().playSfx(AudioClips.KEY_PRESS);
            }
        });

        AudioManager.getInstance().playMusic(AudioClips.GAME_MUSIC, true);

        startNewGame();
    }

    @FXML private void onSubmitButtonClicked() { submitAnswer(inputField.getText()); }
    @FXML private void onEnterPressed()        { submitAnswer(inputField.getText()); }

    private void submitAnswer(String input) {
        gameTimer.stop();
        gameView.playInputAnimation();

        boolean isCorrect = gameLogic.processAnswer(input);

        if (isCorrect) {
            AudioManager.getInstance().playSfx(AudioClips.CRUNCH);
            if (gameLogic.hasWon()) {
                goToWinScreen();
            } else {
                confetti.play(0.02);
                nextRound();
            }
        } else {
            goToLoseScreen(input);
        }
    }

    private void goToLoseScreen(String wrongAnswer) {
        gameView.whitenChars();
        try {
            LoseController loseController = SceneManager.getInstance().loadScene("lose-view.fxml");
            loseController.setup(gameLogic.getCurrentWord(), wrongAnswer, gameView.getWordDisplay());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToWinScreen() {
        gameView.whitenChars();
        try {
            WinController winController = SceneManager.getInstance().loadScene("win-view.fxml");
            winController.setup(gameView.getWordDisplay());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onTick() {
        gameView.updateHUD(gameLogic.getCurrentLevel(), gameTimer.getSecondsLeft());
    }

    private void onTimeUp() { submitAnswer(inputField.getText()); }

    private void nextRound() {
        gameLogic.nextWord();
        inputField.clear();
        refreshUI();
        startTimer();
    }

    private void startNewGame() {
        gameLogic.nextWord();
        inputField.clear();
        refreshUI();
        startTimer();
        gameView.playEntranceAnimations();
    }

    private void startTimer() {
        int maxTime = gameLogic.getMaxTimeForCurrentLevel();
        gameView.initTimer(maxTime);
        gameTimer.start(maxTime);
    }

    private void refreshUI() {
        gameView.renderWord(gameLogic.getCurrentChars(), gameLogic.getCurrentLevel());
        gameView.updateHUD(gameLogic.getCurrentLevel(), gameLogic.getMaxTimeForCurrentLevel());
        gameView.updateAshEmitter(gameLogic.getCurrentLevel());
    }
}