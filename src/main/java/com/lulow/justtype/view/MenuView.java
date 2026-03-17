package com.lulow.justtype.view;

import com.lulow.justtype.view.animations.OvershootAnimation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Manages the visual state of the main menu screen.
 * Drives a beat animation on the title, handles menu item selection styling,
 * and applies a subtle white flash overlay on each beat.
 */
public class MenuView {

    /** Interval between each beat animation in milliseconds. */
    private static final int    BEAT_INTERVAL_MS   = 400;

    /** Scale factor for the overshoot phase of the beat. */
    private static final double OVERSHOOT_SCALE    = 1.1;

    /** Scale factor for the undershoot phase of the beat. */
    private static final double UNDERSHOOT_SCALE   = 0.98;

    /** Maximum opacity of the white flash overlay. */
    private static final double FLASH_PEAK_OPACITY = 0.02;

    /** Duration of the flash fade-out in milliseconds. */
    private static final int    FLASH_FADE_MS      = 350;

    /** The HBox containing the animated title labels. */
    private final HBox  titleDisplay;

    /** Label for the "New Game" menu option. */
    private final Label newGameLabel;

    /** Label for the "Exit" menu option. */
    private final Label exitLabel;

    /** Selection indicator shown next to the active "New Game" option. */
    private final Label newGameIndicator;

    /** Selection indicator shown next to the active "Exit" option. */
    private final Label exitIndicator;

    /** Transparent white rectangle used for the flash effect. */
    private final Rectangle flashOverlay;

    /** Timeline driving the periodic beat animation. */
    private Timeline beatTimeline;

    /**
     * Creates the menu view and sets up the background gradient and flash overlay.
     *
     * @param rootPane        the root pane of the menu scene
     * @param titleDisplay    the HBox displaying the game title
     * @param newGameLabel    the "New Game" menu label
     * @param exitLabel       the "Exit" menu label
     * @param newGameIndicator the indicator shown beside "New Game" when selected
     * @param exitIndicator   the indicator shown beside "Exit" when selected
     */
    public MenuView(AnchorPane rootPane, HBox titleDisplay,
                    Label newGameLabel, Label exitLabel,
                    Label newGameIndicator, Label exitIndicator) {
        this.titleDisplay     = titleDisplay;
        this.newGameLabel     = newGameLabel;
        this.exitLabel        = exitLabel;
        this.newGameIndicator = newGameIndicator;
        this.exitIndicator    = exitIndicator;

        new BackgroundGradient(rootPane).updateForLevel(1);

        flashOverlay = new Rectangle(rootPane.getPrefWidth(), rootPane.getPrefHeight());
        flashOverlay.setFill(Color.WHITE);
        flashOverlay.setOpacity(0);
        flashOverlay.setMouseTransparent(true);
        rootPane.getChildren().add(flashOverlay);
    }

    /**
     * Plays the initial title entrance animation and starts the beat timeline.
     */
    public void start() {
        new OvershootAnimation(titleDisplay, OVERSHOOT_SCALE, UNDERSHOOT_SCALE).play();
        startBeat();
    }

    /** Stops the beat animation timeline. */
    public void stop() {
        if (beatTimeline != null) beatTimeline.stop();
    }

    /**
     * Updates the visual state of menu items based on the current selection.
     * The active item receives the highlighted style; the inactive one is dimmed.
     *
     * @param selection the currently highlighted menu option
     */
    public void setSelection(MenuSelection selection) {
        boolean newGameSelected = selection == MenuSelection.NEW_GAME;
        newGameLabel.getStyleClass().setAll(newGameSelected ? "menu-item-active" : "menu-item-inactive");
        exitLabel.getStyleClass().setAll(newGameSelected    ? "menu-item-inactive" : "menu-item-active");
        newGameIndicator.setVisible(newGameSelected);
        exitIndicator.setVisible(!newGameSelected);
    }

    /**
     * Starts the looping beat timeline that pulses the title and flashes the overlay.
     */
    private void startBeat() {
        beatTimeline = new Timeline(new KeyFrame(Duration.millis(BEAT_INTERVAL_MS), e -> onBeat()));
        beatTimeline.setCycleCount(Timeline.INDEFINITE);
        beatTimeline.play();
    }

    /**
     * Executes one beat: plays the title overshoot animation and fades the flash overlay.
     */
    private void onBeat() {
        new OvershootAnimation(titleDisplay, OVERSHOOT_SCALE, UNDERSHOOT_SCALE).play();
        flashOverlay.setOpacity(FLASH_PEAK_OPACITY);
        FadeTransition fade = new FadeTransition(Duration.millis(FLASH_FADE_MS), flashOverlay);
        fade.setFromValue(FLASH_PEAK_OPACITY);
        fade.setToValue(0);
        fade.play();
    }

    /**
     * Enum representing the available menu options.
     */
    public enum MenuSelection { NEW_GAME, EXIT }
}