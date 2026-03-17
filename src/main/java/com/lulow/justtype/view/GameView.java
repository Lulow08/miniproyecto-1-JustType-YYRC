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

public class GameView {

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

    private static final String FEEDBACK_CORRECT  = "Correct!";
    private static final String FEEDBACK_WRONG    = "Incorrect!";

    private static final double WORD_TOP_MARGIN_NORMAL  = 78.0;
    private static final double WORD_TOP_MARGIN_SMALL   = 92.0;
    private static final double TIMER_PILL_SLIDE_FROM   = -80.0;
    private static final int    CRITICAL_TIME_THRESHOLD = 5;

    private static final double FEEDBACK_ANCHOR_BOTTOM = 72.0;
    private static final double FEEDBACK_VISIBLE_MS    = 600.0;
    private static final double FEEDBACK_FADE_MS       = 250.0;

    private static final int    ASH_HARD_PARTICLES   = 1;
    private static final double ASH_HARD_SPEED       = 1.2;
    private static final int    ASH_EXPERT_PARTICLES = 1;
    private static final double ASH_EXPERT_SPEED     = 2.0;

    private final HBox       wordDisplay;
    private final HBox       timerPill;
    private final Label      levelLabel;
    private final Label      timerLabel;
    private final StackPane  timerPane;
    private final AnchorPane rootPane;

    private Label[]        charLabels = new Label[0];
    private PressAnimation inputAnimation;
    private PressAnimation submitButtonAnimation;
    private PressAnimation resetButtonAnimation;

    private final TimerArc           timerArc;
    private final BackgroundGradient backgroundGradient;
    private final AshFX              ashFX;

    private Label   feedbackLabel;
    private boolean feedbackRunning = false;
    private String  pendingMessage  = null;
    private Boolean pendingSuccess  = null;

    private LevelConfig.Tier lastAshTier = null;

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

    private void setupTimerArc() {
        timerPane.getChildren().add(0, timerArc.getContainer());
        Node arcNode = timerPane.getChildren().get(0);
        StackPane.setAlignment(arcNode, Pos.CENTER_LEFT);
        StackPane.setMargin(arcNode, new Insets(0, 0, 0, 8));
    }

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

    public void setupAnimations(TextField inputField, Button submitButton, Button resetButton) {
        inputAnimation  = new PressAnimation(inputField, 0.96);
        submitButtonAnimation = new PressAnimation(submitButton, 0.9);
        resetButtonAnimation = new PressAnimation(resetButton, 0.9);
        submitButton.setOnMousePressed(event -> submitButtonAnimation.play());
        resetButton.setOnMousePressed(mouseEvent -> resetButtonAnimation.play());
    }

    public void playInputAnimation() { if (inputAnimation != null) inputAnimation.play(); }

    public void playEntranceAnimations() {
        new SlideInAnimation(timerPill, TIMER_PILL_SLIDE_FROM, 1).play();
        new OvershootAnimation(wordDisplay, 1.16, 0.96).play();
        inputAnimation.play();
        submitButtonAnimation.play();
    }

    public void shakeWordDisplay() {
        new ShakeAnimation(wordDisplay).play();
    }

    public void initTimer(int totalSeconds) {
        timerArc.init(totalSeconds);
    }

    public void renderWord(char[] chars, int level) {
        String activeCharStyle = level > 35 ? CSS_CHAR_SMALL : CSS_CHAR_NORMAL;
        AnchorPane.setTopAnchor(wordDisplay, level > 35 ? WORD_TOP_MARGIN_SMALL : WORD_TOP_MARGIN_NORMAL);

        charLabels = new Label[chars.length];
        wordDisplay.getChildren().clear();

        for (int charIndex = 0; charIndex < chars.length; charIndex++) {
            Label charLabel = new Label(String.valueOf(chars[charIndex]));
            charLabel.getStyleClass().addAll(activeCharStyle, CSS_CHAR_NEUTRAL);
            charLabel.setAlignment(Pos.CENTER);
            charLabels[charIndex] = charLabel;
            wordDisplay.getChildren().add(charLabel);
        }
        new OvershootAnimation(wordDisplay, 1.16, 0.96).play();
    }

    public void colorizeChars(String typed, String target) {
        for (int charIndex = 0; charIndex < charLabels.length; charIndex++) {
            String colorClass;
            if (charIndex < typed.length()) {
                colorClass = (typed.charAt(charIndex) == target.charAt(charIndex))
                        ? CSS_CHAR_CORRECT : CSS_CHAR_WRONG;
            } else {
                colorClass = CSS_CHAR_NEUTRAL;
            }
            setCharColor(charLabels[charIndex], colorClass);
        }
    }

    public void whitenChars() {
        for (Label charLabel : charLabels) {
            charLabel.getStyleClass().removeAll(CSS_CHAR_NEUTRAL, CSS_CHAR_CORRECT, CSS_CHAR_WRONG);
            charLabel.getStyleClass().add(CSS_CHAR_WHITE);
        }
    }

    public HBox getWordDisplay() { return wordDisplay; }

    public void showFeedback(boolean isSuccess) {
        String message = isSuccess ? FEEDBACK_CORRECT : FEEDBACK_WRONG;

        if (feedbackRunning) {
            pendingMessage = message;
            pendingSuccess = isSuccess;
            return;
        }
        displayFeedback(message, isSuccess);
    }

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
        fadeOut.setOnFinished(event -> {
            feedbackRunning = false;
            if (pendingMessage != null) {
                String  nextMessage = pendingMessage;
                boolean nextSuccess = pendingSuccess;
                pendingMessage = null;
                pendingSuccess = null;
                displayFeedback(nextMessage, nextSuccess);
            }
        });
        fadeOut.play();
    }

    public void hideFeedback() {
        feedbackRunning = false;
        pendingMessage  = null;
        pendingSuccess  = null;
        feedbackLabel.setOpacity(0);
    }

    public boolean isCritical(int secondsLeft) {
        return secondsLeft <= CRITICAL_TIME_THRESHOLD;
    }

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

    private void setCharColor(Label charLabel, String colorClass) {
        charLabel.getStyleClass().removeAll(CSS_CHAR_NEUTRAL, CSS_CHAR_CORRECT, CSS_CHAR_WRONG);
        charLabel.getStyleClass().add(colorClass);
    }
}