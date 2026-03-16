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

public class MenuView {

    private static final int    BEAT_INTERVAL_MS     = 400;
    private static final double OVERSHOOT_SCALE      = 1.1;
    private static final double UNDERSHOOT_SCALE     = 0.98;
    private static final double FLASH_PEAK_OPACITY   = 0.02;
    private static final int    FLASH_FADE_MS        = 350;

    private final HBox       titleDisplay;
    private final Label      newGameLabel;
    private final Label      exitLabel;
    private final Label      newGameIndicator;
    private final Label      exitIndicator;

    private final Rectangle      flashOverlay;
    private Timeline             beatTimeline;

    public MenuView(AnchorPane rootPane, HBox titleDisplay,
                    Label newGameLabel, Label exitLabel,
                    Label newGameIndicator, Label exitIndicator) {
        this.titleDisplay     = titleDisplay;
        this.newGameLabel     = newGameLabel;
        this.exitLabel        = exitLabel;
        this.newGameIndicator = newGameIndicator;
        this.exitIndicator    = exitIndicator;

        BackgroundGradient backgroundGradient = new BackgroundGradient(rootPane);
        backgroundGradient.updateForLevel(1);

        flashOverlay = new Rectangle(rootPane.getPrefWidth(), rootPane.getPrefHeight());
        flashOverlay.setFill(Color.WHITE);
        flashOverlay.setOpacity(0);
        flashOverlay.setMouseTransparent(true);
        rootPane.getChildren().add(flashOverlay);
    }

    public void start() {
        new OvershootAnimation(titleDisplay, OVERSHOOT_SCALE, UNDERSHOOT_SCALE).play();
        startBeat();
    }

    public void stop() {
        if (beatTimeline != null) beatTimeline.stop();
    }

    public void setSelection(MenuSelection selection) {
        boolean newGameSelected = selection == MenuSelection.NEW_GAME;

        newGameLabel.getStyleClass().setAll(newGameSelected ? "menu-item-active" : "menu-item-inactive");
        exitLabel.getStyleClass().setAll(newGameSelected ? "menu-item-inactive" : "menu-item-active");

        newGameIndicator.setVisible(newGameSelected);
        exitIndicator.setVisible(!newGameSelected);
    }

    private void startBeat() {
        beatTimeline = new Timeline(new KeyFrame(Duration.millis(BEAT_INTERVAL_MS), e -> onBeat()));
        beatTimeline.setCycleCount(Timeline.INDEFINITE);
        beatTimeline.play();
    }

    private void onBeat() {
        new OvershootAnimation(titleDisplay, OVERSHOOT_SCALE, UNDERSHOOT_SCALE).play();

        flashOverlay.setOpacity(FLASH_PEAK_OPACITY);
        FadeTransition fade = new FadeTransition(Duration.millis(FLASH_FADE_MS), flashOverlay);
        fade.setFromValue(FLASH_PEAK_OPACITY);
        fade.setToValue(0);
        fade.play();
    }

    public enum MenuSelection { NEW_GAME, EXIT }
}