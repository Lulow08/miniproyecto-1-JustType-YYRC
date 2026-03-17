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

/**
 * Controller for the main game screen.
 * Coordinates the gameplay loop between {@link GameLogic}, {@link GameView},
 * and {@link GameTimer}. Handles player input events, timer callbacks,
 * and transitions to the win or lose screens.
 */
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

    /** Holds the game state and validation logic. */
    private GameLogic  gameLogic;

    /** Manages all visual updates for the game scene. */
    private GameView   gameView;

    /** Countdown timer that drives the per-level time limit. */
    private GameTimer  gameTimer;

    /** Confetti particle emitter triggered on correct answers. */
    private ConfettiFX confetti;

    /**
     * Initializes the controller after FXML injection.
     * Sets up game logic, view, timer, confetti, animations,
     * input listeners, background music, and starts the first game.
     */
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

        javafx.application.Platform.runLater(() -> inputField.requestFocus());
    }

    /**
     * Handles the submit button click event.
     * Delegates to {@link #handlePlayerSubmit(String)}.
     */
    @FXML
    private void onSubmitButtonClicked() { handlePlayerSubmit(inputField.getText()); }

    /**
     * Handles the Enter key press inside the input field.
     * Delegates to {@link #handlePlayerSubmit(String)}.
     */
    @FXML
    private void onEnterPressed() { handlePlayerSubmit(inputField.getText()); }

    /**
     * Handles the reset button click event.
     * Stops the timer, hides any feedback, resets game state, and starts a new game.
     */
    @FXML
    private void onResetButtonClicked() {
        gameTimer.stop();
        gameView.hideFeedback();
        gameLogic.reset();
        startNewGame();
    }

    /**
     * Processes the player's submitted answer.
     * Shows feedback, plays sounds, and either advances to the next round,
     * navigates to the win screen, or signals an incorrect answer.
     *
     * @param input the text currently in the input field
     */
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

    /**
     * Called by the timer when the countdown reaches zero.
     * Validates whatever the player had typed; navigates to win or lose accordingly.
     */
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
            goToLoseScreen(currentInput);
        }
    }

    /**
     * Navigates to the loose screen, passing the current word and word display.
     */
    private void goToLoseScreen(String wrongAnswer) {
        gameView.hideFeedback();
        gameView.whitenChars();
        try {
            LoseController loseController = SceneManager.getInstance().switchScene("lose-view.fxml");
            loseController.setup(
                    gameLogic.getCompletedLevels(),
                    wrongAnswer,
                    gameView.getWordDisplay()
            );
        } catch (IOException exception) {
            LOGGER.log(Level.SEVERE, "Failed to load lose screen", exception);
        }
    }

    /**
     * Navigates to the win screen, passing the completed levels and time remaining.
     */
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

    /**
     * Called by the timer once per second.
     * Updates the HUD and plays a beep if the timer is in its critical phase.
     */
    private void onTick() {
        int secondsLeft = gameTimer.getSecondsLeft();
        gameView.updateHUD(gameLogic.getCurrentLevel(), secondsLeft);

        if (gameView.isCritical(secondsLeft)) {
            AudioManager.getInstance().playSfx(AudioClips.CRITICAL_BEEP);
        }
    }

    /**
     * Advances to the next word, clears the input, refreshes the UI, and restarts the timer.
     */
    private void nextRound() {
        gameLogic.nextWord();
        inputField.clear();
        refreshUI();
        startTimer();
    }

    /**
     * Starts a brand new game from the first word, plays entrance animations,
     * and requests focus on the input field.
     */
    private void startNewGame() {
        gameLogic.nextWord();
        inputField.clear();
        refreshUI();
        startTimer();
        gameView.playEntranceAnimations();
        javafx.application.Platform.runLater(() -> inputField.requestFocus());
    }

    /**
     * Reads the time limit for the current level and starts the game timer.
     */
    private void startTimer() {
        int maxTime = gameLogic.getMaxTimeForCurrentLevel();
        gameView.initTimer(maxTime);
        gameTimer.start(maxTime);
    }

    /**
     * Refreshes all UI elements to reflect the current game state:
     * word display, HUD labels, and the ash particle emitter.
     */
    private void refreshUI() {
        gameView.renderWord(gameLogic.getCurrentChars(), gameLogic.getCurrentLevel());
        gameView.updateHUD(gameLogic.getCurrentLevel(), gameLogic.getMaxTimeForCurrentLevel());
        gameView.updateAshEmitter(gameLogic.getCurrentLevel());
    }
}