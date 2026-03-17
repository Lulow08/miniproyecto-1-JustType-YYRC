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
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameController {

    private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());

    @FXML private TextField  inputField;
    @FXML private HBox       wordDisplay;
    @FXML private HBox       timerPill;
    @FXML private Label      levelLabel;
    @FXML private Label      timerLabel;
    @FXML private Button     submitButton;
    @FXML private Button     resetButton;
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

        gameView.setupAnimations(inputField, submitButton, resetButton);

        inputField.textProperty().addListener((observable, oldText, newText) -> {
            gameView.colorizeChars(newText, gameLogic.getCurrentWord());
            if (newText.length() != oldText.length()) {
                AudioManager.getInstance().playSfx(AudioClips.KEY_PRESS);
            }
        });

        AudioManager.getInstance().playMusic(AudioClips.GAME_MUSIC, true);

        startNewGame();
    }

    @FXML private void onSubmitButtonClicked() { handlePlayerSubmit(inputField.getText()); }
    @FXML private void onEnterPressed()        { handlePlayerSubmit(inputField.getText()); }

    @FXML
    private void onResetButtonClicked() {
        gameTimer.stop();
        gameView.hideFeedback();
        gameLogic.reset();
        startNewGame();
    }

    private void handlePlayerSubmit(String input) {
        gameView.playInputAnimation();

        boolean isCorrect = gameLogic.submitAnswer(input);

        if (isCorrect) {
            AudioManager.getInstance().playSfx(AudioClips.CRUNCH);
            gameView.showFeedback(true);

            if (gameLogic.hasWon()) {
                gameLogic.recordTimeRemaining(gameTimer.getSecondsLeft());
                goToWinScreen();
            } else {
                confetti.play(0.02);
                nextRound();
            }
        } else {
            AudioManager.getInstance().playSfx(AudioClips.ERROR);
            gameView.shakeWordDisplay();
            gameView.showFeedback(false);
        }
    }

    private void onTimeUp() {
        String  currentInput = inputField.getText();
        boolean wasCorrect   = gameLogic.timeUpAnswer(currentInput);

        if (wasCorrect) {
            AudioManager.getInstance().playSfx(AudioClips.CRUNCH);
            if (gameLogic.hasWon()) {
                gameLogic.recordTimeRemaining(0);
                goToWinScreen();
            } else {
                confetti.play(0.02);
                nextRound();
            }
        } else {
            gameLogic.recordTimeRemaining(0);
            goToLoseScreen();
        }
    }

    private void goToLoseScreen() {
        gameView.hideFeedback();
        gameView.whitenChars();
        try {
            LoseController loseController = SceneManager.getInstance().switchScene("lose-view.fxml");
            loseController.setup(
                    gameLogic.getCompletedLevels(),
                    gameLogic.getCurrentWord(),
                    gameView.getWordDisplay()
            );
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load lose screen", exception);
        }
    }

    private void goToWinScreen() {
        gameView.whitenChars();
        try {
            WinController winController = SceneManager.getInstance().switchScene("win-view.fxml");
            winController.setup(
                    gameLogic.getCompletedLevels(),
                    gameLogic.getLastTimeRemaining(),
                    gameView.getWordDisplay()
            );
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load win screen", exception);
        }
    }

    private void onTick() {
        int secondsLeft = gameTimer.getSecondsLeft();
        gameView.updateHUD(gameLogic.getCurrentLevel(), secondsLeft);

        if (gameView.isCritical(secondsLeft)) {
            AudioManager.getInstance().playSfx(AudioClips.CRITICAL_BEEP);
        }
    }

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