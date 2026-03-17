package com.lulow.justtype.view;

import com.lulow.justtype.model.AudioClips;
import com.lulow.justtype.model.AudioManager;
import com.lulow.justtype.view.animations.FadeInAnimation;
import com.lulow.justtype.view.animations.OvershootAnimation;
import com.lulow.justtype.view.particles.ConfettiFX;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class WinView {

    private static final double ANIMATION_DELAY_SECONDS   = 1.2;
    private static final int    BEAT_INTERVAL_MS          = 500;
    private static final double OVERSHOOT_SCALE           = 1.1;
    private static final double UNDERSHOOT_SCALE          = 0.98;
    private static final double FLASH_PEAK_OPACITY        = 0.04;
    private static final int    FLASH_FADE_MS             = 350;
    private static final double CONFETTI_DURATION_SECONDS = 0.9;
    private static final double WORD_DISPLAY_TOP          = 78.0;

    private final AnchorPane rootPane;
    private final Label      titleLabel;
    private final Label      statsLabel;
    private final Label      hintLabel;

    private final ConfettiFX confetti;
    private final Rectangle  flashOverlay;
    private Timeline         beatTimeline;

    public WinView(AnchorPane rootPane, Label titleLabel, Label statsLabel, Label hintLabel) {
        this.rootPane   = rootPane;
        this.titleLabel = titleLabel;
        this.statsLabel = statsLabel;
        this.hintLabel  = hintLabel;

        BackgroundGradient backgroundGradient = new BackgroundGradient(rootPane);
        backgroundGradient.updateForLevel(1);

        this.confetti = new ConfettiFX(rootPane, rootPane.getPrefWidth(), rootPane.getPrefHeight());

        flashOverlay = new Rectangle(rootPane.getPrefWidth(), rootPane.getPrefHeight());
        flashOverlay.setFill(Theme.COLOR_WHITE);
        flashOverlay.setOpacity(0);
        flashOverlay.setMouseTransparent(true);
        rootPane.getChildren().add(flashOverlay);
    }
    
    public void setup(int completedLevels, int timeRemaining, HBox wordDisplay) {
        String statsText = "Levels completed: " + completedLevels;
        if (timeRemaining > 0) {
            statsText += "  |  Time remaining: " + timeRemaining + "s";
        }
        statsLabel.setText(statsText);

        injectWordDisplay(wordDisplay);

        titleLabel.setOpacity(0);
        statsLabel.setOpacity(0);
        hintLabel.setOpacity(0);

        PauseTransition delay = getPauseTransition(wordDisplay);
        delay.play();
    }

    private PauseTransition getPauseTransition(HBox wordDisplay) {
        PauseTransition delay = new PauseTransition(Duration.seconds(ANIMATION_DELAY_SECONDS));

        delay.setOnFinished(event -> {
            titleLabel.setOpacity(1.0);
            new OvershootAnimation(titleLabel, 3.0, 0.88).play();
            new FadeInAnimation(statsLabel).play();
            new FadeInAnimation(hintLabel).play();

            confetti.playColorful(CONFETTI_DURATION_SECONDS);
            AudioManager.getInstance().playSfx(AudioClips.CONFETTI_POP);
            AudioManager.getInstance().playMusic(AudioClips.WIN_MUSIC, false);

            startBeat(wordDisplay);
        });
        return delay;
    }

    public void stop() {
        if (beatTimeline != null) beatTimeline.stop();
    }

    private void injectWordDisplay(HBox wordDisplay) {
        if (wordDisplay == null) return;
        wordDisplay.setAlignment(Pos.CENTER);
        rootPane.getChildren().add(wordDisplay);
        AnchorPane.setLeftAnchor(wordDisplay,  0.0);
        AnchorPane.setRightAnchor(wordDisplay, 0.0);
        AnchorPane.setTopAnchor(wordDisplay,   WORD_DISPLAY_TOP);
    }

    private void startBeat(HBox wordDisplay) {
        beatTimeline = new Timeline(new KeyFrame(Duration.millis(BEAT_INTERVAL_MS), event -> {
            new OvershootAnimation(titleLabel,  OVERSHOOT_SCALE, UNDERSHOOT_SCALE).play();
            new OvershootAnimation(wordDisplay, OVERSHOOT_SCALE, UNDERSHOOT_SCALE).play();
            pulseBackground();
        }));
        beatTimeline.setCycleCount(Timeline.INDEFINITE);
        beatTimeline.play();
    }

    private void pulseBackground() {
        flashOverlay.setOpacity(FLASH_PEAK_OPACITY);
        FadeTransition fade = new FadeTransition(Duration.millis(FLASH_FADE_MS), flashOverlay);
        fade.setFromValue(FLASH_PEAK_OPACITY);
        fade.setToValue(0);
        fade.play();
    }
}