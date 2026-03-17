package com.lulow.justtype.view;

import com.lulow.justtype.model.LevelConfig;
import com.lulow.justtype.view.animations.OvershootAnimation;
import com.lulow.justtype.view.animations.PressAnimation;
import com.lulow.justtype.view.animations.ShakeAnimation;
import com.lulow.justtype.view.animations.SlideInAnimation;
import com.lulow.justtype.view.particles.AshFX;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Manages all visual output for the main game screen.
 * Responsible for rendering words character by character, colorizing input,
 * updating the HUD (level and timer), displaying feedback messages,
 * running entrance animations, and managing particle effects.
 */
public class GameView {

    // --- CSS style class constants ---
    private static final String CSS_CHAR_NORMAL    = "char-normal";
    private static final String CSS_CHAR_SMALL     = "char-small";
    private static final String CSS_CHAR_NEUTRAL   = "char-neutral";
    private static final String CSS_CHAR_CORRECT   = "char-correct";
    private static final String CSS_CHAR_WRONG     = "char-wrong";
    private static final String CSS_CHAR_WHITE     = "char-white";
    private static final String CSS_TIMER_NORMAL   = "timer-label";
    private static final String CSS_TIMER_CRITICAL = "timer-label-critical";
    private static final String CSS_FEEDBACK_OK    = "feedback-ok";
    private static final String CSS_FEEDBACK_ERROR = "feedback-error";

    /** Text shown on correct answer. */
    private static final String FEEDBACK_CORRECT = "Correct!";

    /** Text shown on incorrect answer. */
    private static final String FEEDBACK_WRONG   = "Incorrect!";

    /** Top anchor for the word display at normal font size. */
    private static final double WORD_TOP_MARGIN_NORMAL  = 78.0;

    /** Top anchor for the word display at small font size (EXPERT tier). */
    private static final double WORD_TOP_MARGIN_SMALL   = 92.0;

    /** Starting Y offset for the timer pill slide-in animation. */
    private static final double TIMER_PILL_SLIDE_FROM   = -80.0;

    /** Seconds at which the timer is considered critical. */
    private static final int    CRITICAL_TIME_THRESHOLD = 5;

    /** Bottom anchor for the floating feedback label. */
    private static final double FEEDBACK_ANCHOR_BOTTOM = 72.0;

    /** Milliseconds the feedback label stays fully visible. */
    private static final double FEEDBACK_VISIBLE_MS    = 600.0;

    /** Milliseconds for the feedback fade-out transition. */
    private static final double FEEDBACK_FADE_MS       = 250.0;

    /** Ash particles per frame during the HARD tier. */
    private static final int    ASH_HARD_PARTICLES     = 1;

    /** Ash speed multiplier during the HARD tier. */
    private static final double ASH_HARD_SPEED         = 1.2;

    /** Ash particles per frame during the EXPERT tier. */
    private static final int    ASH_EXPERT_PARTICLES   = 1;

    /** Ash speed multiplier during the EXPERT tier. */
    private static final double ASH_EXPERT_SPEED       = 2.0;

    // --- FXML node references ---
    private final HBox       wordDisplay;
    private final HBox       timerPill;
    private final Label      levelLabel;
    private final Label      timerLabel;
    private final StackPane  timerPane;
    private final AnchorPane rootPane;

    /** Labels rendered for each character of the current word. */
    private Label[] charLabels = new Label[0];

    /** Press animation wired to the input field. */
    private PressAnimation inputAnimation;

    /** Press animation wired to the submit button. */
    private PressAnimation submitButtonAnimation;

    /** Press animation wired to the reset button. */
    private PressAnimation resetButtonAnimation;

    /** Circular arc that visually tracks the timer countdown. */
    private final TimerArc           timerArc;

    /** Manages the animated background color transition per tier. */
    private final BackgroundGradient backgroundGradient;

    /** Ash particle emitter for high-difficulty tiers. */
    private final AshFX              ashFX;

    /** Dynamically created label for "Correct!" / "Incorrect!" messages. */
    private Label   feedbackLabel;

    /** Whether a feedback animation is currently in progress. */
    private boolean feedbackRunning = false;

    /** The next feedback message to show after the current one finishes. */
    private String  pendingMessage  = null;

    /** Whether the pending feedback message is a success. */
    private Boolean pendingSuccess  = null;

    /** The last ash tier applied, used to avoid redundant emitter updates. */
    private LevelConfig.Tier lastAshTier = null;

    /**
     * Creates the game view, initializing the timer arc, background gradient,
     * ash emitter, and the floating feedback label.
     *
     * @param wordDisplay the HBox where character labels are rendered
     * @param levelLabel  the label displaying the current level
     * @param timerLabel  the label displaying the remaining seconds
     * @param timerPane   the StackPane containing the timer label and arc
     * @param rootPane    the root AnchorPane of the game scene
     * @param timerPill   the HBox styled as the timer pill container
     */
    public GameView(HBox wordDisplay, Label levelLabel, Label timerLabel,
                    StackPane timerPane, AnchorPane rootPane, HBox timerPill) {
        this.wordDisplay = wordDisplay;
        this.timerPill   = timerPill;
        this.levelLabel  = levelLabel;
        this.timerLabel  = timerLabel;
        this.timerPane   = timerPane;
        this.rootPane    = rootPane;

        this.timerArc           = new TimerArc();
        this.backgroundGradient = new BackgroundGradient(rootPane);
        this.ashFX              = new AshFX(rootPane, rootPane.getPrefWidth(), rootPane.getPrefHeight());

        setupTimerArc();
        setupFeedbackLabel();
    }

    /**
     * Inserts the timer arc into the timer pane and aligns it to the left.
     */
    private void setupTimerArc() {
        timerPane.getChildren().add(0, timerArc.getContainer());
        Node arcNode = timerPane.getChildren().get(0);
        StackPane.setAlignment(arcNode, Pos.CENTER_LEFT);
        StackPane.setMargin(arcNode, new Insets(0, 0, 0, 8));
    }

    /**
     * Creates and anchors the feedback label to the root pane.
     * Starts hidden (opacity 0).
     */
    private void setupFeedbackLabel() {
        feedbackLabel = new Label();
        feedbackLabel.setMaxWidth(Double.MAX_VALUE);
        feedbackLabel.setAlignment(Pos.CENTER);
        feedbackLabel.setOpacity(0);
        AnchorPane.setBottomAnchor(feedbackLabel, FEEDBACK_ANCHOR_BOTTOM);
        AnchorPane.setLeftAnchor(feedbackLabel,   0.0);
        AnchorPane.setRightAnchor(feedbackLabel,  0.0);
        rootPane.getChildren().add(feedbackLabel);
    }

    /**
     * Creates press animations and wires mouse-press handlers for interactive controls.
     *
     * @param inputField   the text input field
     * @param submitButton the submit answer button
     * @param resetButton  the reset/restart button
     */
    public void setupAnimations(TextField inputField, Button submitButton, Button resetButton) {
        inputAnimation        = new PressAnimation(inputField, 0.96);
        submitButtonAnimation = new PressAnimation(submitButton, 0.9);
        resetButtonAnimation  = new PressAnimation(resetButton, 0.9);
        submitButton.setOnMousePressed(e -> submitButtonAnimation.play());
        resetButton.setOnMousePressed(e -> resetButtonAnimation.play());
    }

    /** Plays the press animation on the input field. */
    public void playInputAnimation() { if (inputAnimation != null) inputAnimation.play(); }

    /**
     * Plays the entrance animations for the timer pill, word display, input field,
     * and submit button. Called once when a new game starts.
     */
    public void playEntranceAnimations() {
        new SlideInAnimation(timerPill, TIMER_PILL_SLIDE_FROM, 1).play();
        new OvershootAnimation(wordDisplay, 1.16, 0.96).play();
        inputAnimation.play();
        submitButtonAnimation.play();
    }

    /** Plays a horizontal shake animation on the word display to signal a wrong answer. */
    public void shakeWordDisplay() {
        new ShakeAnimation(wordDisplay).play();
    }

    /**
     * Initializes the timer arc for a new level.
     *
     * @param totalSeconds the total countdown seconds for this level
     */
    public void initTimer(int totalSeconds) {
        timerArc.init(totalSeconds);
    }

    /**
     * Renders the current word as individual character labels inside the word display HBox.
     * Applies a smaller font for EXPERT-tier words and plays a pop animation.
     *
     * @param chars the character array of the word to display
     * @param level the current level, used to determine font size
     */
    public void renderWord(char[] chars, int level) {
        String activeCharStyle = level > 35 ? CSS_CHAR_SMALL : CSS_CHAR_NORMAL;
        AnchorPane.setTopAnchor(wordDisplay, level > 35 ? WORD_TOP_MARGIN_SMALL : WORD_TOP_MARGIN_NORMAL);

        charLabels = new Label[chars.length];
        wordDisplay.getChildren().clear();

        for (int i = 0; i < chars.length; i++) {
            Label charLabel = new Label(String.valueOf(chars[i]));
            charLabel.getStyleClass().addAll(activeCharStyle, CSS_CHAR_NEUTRAL);
            charLabel.setAlignment(Pos.CENTER);
            charLabels[i] = charLabel;
            wordDisplay.getChildren().add(charLabel);
        }
        new OvershootAnimation(wordDisplay, 1.16, 0.96).play();
    }

    /**
     * Colors each character label based on whether the typed character matches
     * the expected character at the same position.
     *
     * @param typed  the text currently in the input field
     * @param target the correct word
     */
    public void colorizeChars(String typed, String target) {
        for (int i = 0; i < charLabels.length; i++) {
            String colorClass;
            if (i < typed.length()) {
                colorClass = (typed.charAt(i) == target.charAt(i))
                        ? CSS_CHAR_CORRECT : CSS_CHAR_WRONG;
            } else {
                colorClass = CSS_CHAR_NEUTRAL;
            }
            setCharColor(charLabels[i], colorClass);
        }
    }

    /**
     * Sets all character labels to white, used when transitioning away from the game screen.
     */
    public void whitenChars() {
        for (Label label : charLabels) {
            label.getStyleClass().removeAll(CSS_CHAR_NEUTRAL, CSS_CHAR_CORRECT, CSS_CHAR_WRONG);
            label.getStyleClass().add(CSS_CHAR_WHITE);
        }
    }

    /**
     * Returns the word display HBox, used when injecting it into result screens.
     *
     * @return the word display container
     */
    public HBox getWordDisplay() { return wordDisplay; }

    /**
     * Shows a brief feedback message. If a feedback animation is already running,
     * the new message is queued and shown immediately after.
     *
     * @param isSuccess {@code true} for a correct-answer message, {@code false} for incorrect
     */
    public void showFeedback(boolean isSuccess) {
        String message = isSuccess ? FEEDBACK_CORRECT : FEEDBACK_WRONG;
        if (feedbackRunning) {
            pendingMessage = message;
            pendingSuccess = isSuccess;
            return;
        }
        displayFeedback(message, isSuccess);
    }

    /**
     * Displays the feedback label with the given message and schedules a fade-out.
     *
     * @param message   the text to display
     * @param isSuccess whether to apply the success or error style
     */
    private void displayFeedback(String message, boolean isSuccess) {
        feedbackRunning = true;
        feedbackLabel.setText(message);
        feedbackLabel.getStyleClass().removeAll(CSS_FEEDBACK_OK, CSS_FEEDBACK_ERROR);
        feedbackLabel.getStyleClass().add(isSuccess ? CSS_FEEDBACK_OK : CSS_FEEDBACK_ERROR);
        feedbackLabel.setOpacity(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(FEEDBACK_FADE_MS), feedbackLabel);
        fadeOut.setDelay(Duration.millis(FEEDBACK_VISIBLE_MS));
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            feedbackRunning = false;
            if (pendingMessage != null) {
                String  next    = pendingMessage;
                boolean success = pendingSuccess;
                pendingMessage  = null;
                pendingSuccess  = null;
                displayFeedback(next, success);
            }
        });
        fadeOut.play();
    }

    /**
     * Immediately hides the feedback label and clears any pending message.
     */
    public void hideFeedback() {
        feedbackRunning = false;
        pendingMessage  = null;
        pendingSuccess  = null;
        feedbackLabel.setOpacity(0);
    }

    /**
     * Returns whether the given time is at or below the critical threshold.
     *
     * @param secondsLeft the current countdown value
     * @return {@code true} if the timer is in its critical phase
     */
    public boolean isCritical(int secondsLeft) {
        return secondsLeft <= CRITICAL_TIME_THRESHOLD;
    }

    /**
     * Updates the level label, timer label, timer arc, and background gradient.
     * Applies critical styling and plays a pulse animation when time is low.
     *
     * @param level       the current game level
     * @param secondsLeft the remaining seconds for this level
     */
    public void updateHUD(int level, int secondsLeft) {
        levelLabel.setText("#" + level);
        timerLabel.setText(String.valueOf(secondsLeft));

        boolean critical = isCritical(secondsLeft);
        timerLabel.getStyleClass().removeAll(CSS_TIMER_NORMAL, CSS_TIMER_CRITICAL);
        timerLabel.getStyleClass().add(critical ? CSS_TIMER_CRITICAL : CSS_TIMER_NORMAL);

        if (critical) {
            new OvershootAnimation(timerLabel, 1.3, 0.94).play();
        }

        timerArc.update(secondsLeft, critical);
        backgroundGradient.updateForLevel(level);
    }

    /**
     * Starts or stops the ash particle emitter based on the current level's tier.
     * Only updates if the tier has changed since the last call.
     *
     * @param level the current game level
     */
    public void updateAshEmitter(int level) {
        LevelConfig.Tier tier = LevelConfig.getTierForLevel(level);
        if (tier == lastAshTier) return;
        lastAshTier = tier;

        if (tier == LevelConfig.Tier.HARD) {
            ashFX.setIntensity(ASH_HARD_PARTICLES, ASH_HARD_SPEED);
            ashFX.play(Double.MAX_VALUE);
        } else if (tier == LevelConfig.Tier.EXPERT) {
            ashFX.setIntensity(ASH_EXPERT_PARTICLES, ASH_EXPERT_SPEED);
            ashFX.play(Double.MAX_VALUE);
        } else {
            ashFX.stop();
        }
    }

    /**
     * Replaces the color style class on a character label.
     *
     * @param charLabel  the label to update
     * @param colorClass the new CSS color class to apply
     */
    private void setCharColor(Label charLabel, String colorClass) {
        charLabel.getStyleClass().removeAll(CSS_CHAR_NEUTRAL, CSS_CHAR_CORRECT, CSS_CHAR_WRONG);
        charLabel.getStyleClass().add(colorClass);
    }
}