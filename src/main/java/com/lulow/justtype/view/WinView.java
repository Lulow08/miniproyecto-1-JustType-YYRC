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

/**
 * Sets up and animates the win screen.
 * Fires colorful confetti, plays win music, pulses the title and word display
 * on a recurring beat, and displays level and time stats.
 */
public class WinView {

    /** Delay in seconds before win animations play. */
    private static final double ANIMATION_DELAY_SECONDS   = 1.2;

    /** Interval between beat pulses in milliseconds. */
    private static final int    BEAT_INTERVAL_MS          = 500;

    /** Overshoot scale for the beat animation. */
    private static final double OVERSHOOT_SCALE           = 1.1;

    /** Undershoot scale for the beat animation. */
    private static final double UNDERSHOOT_SCALE          = 0.98;

    /** Peak opacity of the white flash overlay on each beat. */
    private static final double FLASH_PEAK_OPACITY        = 0.04;

    /** Duration of the flash fade-out in milliseconds. */
    private static final int    FLASH_FADE_MS             = 350;

    /** Duration of the colorful confetti burst in seconds. */
    private static final double CONFETTI_DURATION_SECONDS = 0.9;

    /** Top anchor for the injected word display. */
    private static final double WORD_DISPLAY_TOP          = 78.0;

    /** The root pane of the win scene. */
    private final AnchorPane rootPane;

    /** Label showing the win title. */
    private final Label titleLabel;

    /** Label showing level count and time remaining. */
    private final Label statsLabel;

    /** Label showing keyboard navigation hints. */
    private final Label hintLabel;

    /** Confetti emitter used for the win celebration. */
    private final ConfettiFX confetti;

    /** Transparent white rectangle used for the beat flash effect. */
    private final Rectangle  flashOverlay;

    /** Timeline driving the periodic beat animation. */
    private Timeline beatTimeline;

    /**
     * Creates the win view, sets up the background, confetti emitter, and flash overlay.
     *
     * @param rootPane   the root AnchorPane of the win scene
     * @param titleLabel the win title label
     * @param statsLabel the stats label
     * @param hintLabel  the navigation hint label
     */
    public WinView(AnchorPane rootPane, Label titleLabel, Label statsLabel, Label hintLabel) {
        this.rootPane   = rootPane;
        this.titleLabel = titleLabel;
        this.statsLabel = statsLabel;
        this.hintLabel  = hintLabel;

        new BackgroundGradient(rootPane).updateForLevel(1);
        this.confetti = new ConfettiFX(rootPane, rootPane.getPrefWidth(), rootPane.getPrefHeight());

        flashOverlay = new Rectangle(rootPane.getPrefWidth(), rootPane.getPrefHeight());
        flashOverlay.setFill(Theme.COLOR_WHITE);
        flashOverlay.setOpacity(0);
        flashOverlay.setMouseTransparent(true);
        rootPane.getChildren().add(flashOverlay);
    }

    /**
     * Populates the win screen with game results and plays all win animations.
     *
     * @param completedLevels the number of levels completed
     * @param timeRemaining   seconds remaining when the last level was passed
     * @param wordDisplay     the word display HBox carried over from the game screen
     */
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

        getPauseTransition(wordDisplay).play();
    }

    /**
     * Builds the pause transition that delays the entrance animations.
     *
     * @param wordDisplay the word display used in the beat animation
     * @return a configured {@link PauseTransition}
     */
    private PauseTransition getPauseTransition(HBox wordDisplay) {
        PauseTransition delay = new PauseTransition(Duration.seconds(ANIMATION_DELAY_SECONDS));
        delay.setOnFinished(e -> {
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

    /** Stops the beat timeline. Should be called before navigating away from this screen. */
    public void stop() {
        if (beatTimeline != null) beatTimeline.stop();
    }

    /**
     * Adds the word display to the root pane, anchored to the standard game position.
     *
     * @param wordDisplay the HBox to inject; does nothing if {@code null}
     */
    private void injectWordDisplay(HBox wordDisplay) {
        if (wordDisplay == null) return;
        wordDisplay.setAlignment(Pos.CENTER);
        rootPane.getChildren().add(wordDisplay);
        AnchorPane.setLeftAnchor(wordDisplay,  0.0);
        AnchorPane.setRightAnchor(wordDisplay, 0.0);
        AnchorPane.setTopAnchor(wordDisplay,   WORD_DISPLAY_TOP);
    }

    /**
     * Starts a looping beat timeline that pulses the title and word display
     * and flashes the background overlay.
     *
     * @param wordDisplay the word display to animate on each beat
     */
    private void startBeat(HBox wordDisplay) {
        beatTimeline = new Timeline(new KeyFrame(Duration.millis(BEAT_INTERVAL_MS), e -> {
            new OvershootAnimation(titleLabel,  OVERSHOOT_SCALE, UNDERSHOOT_SCALE).play();
            new OvershootAnimation(wordDisplay, OVERSHOOT_SCALE, UNDERSHOOT_SCALE).play();
            pulseBackground();
        }));
        beatTimeline.setCycleCount(Timeline.INDEFINITE);
        beatTimeline.play();
    }

    /**
     * Briefly flashes the white overlay and fades it back out.
     */
    private void pulseBackground() {
        flashOverlay.setOpacity(FLASH_PEAK_OPACITY);
        FadeTransition fade = new FadeTransition(Duration.millis(FLASH_FADE_MS), flashOverlay);
        fade.setFromValue(FLASH_PEAK_OPACITY);
        fade.setToValue(0);
        fade.play();
    }
}