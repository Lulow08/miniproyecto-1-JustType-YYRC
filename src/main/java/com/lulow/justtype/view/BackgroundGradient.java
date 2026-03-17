package com.lulow.justtype.view;

import com.lulow.justtype.model.LevelConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BackgroundGradient {

    private static final int ANIMATION_DURATION_MS = 1400;
    private static final int ANIMATION_STEPS       = 24;

    private final Pane target;
    private Color      currentColor = Theme.COLOR_BASE;

    public BackgroundGradient(Pane target) {
        this.target = target;
        applyGradient(Theme.COLOR_BASE);
    }

    public void updateForLevel(int level) {
        Color nextColor = colorForLevel(level);
        if (nextColor.equals(currentColor)) return;
        animateTo(nextColor);
        currentColor = nextColor;
    }

    private void animateTo(Color targetColor) {
        Color    startColor = currentColor;
        Timeline timeline   = new Timeline();

        for (int step = 0; step <= ANIMATION_STEPS; step++) {
            double progress     = (double) step / ANIMATION_STEPS;
            Color  interpolated = startColor.interpolate(targetColor, progress);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(ANIMATION_DURATION_MS * progress),
                            event -> applyGradient(interpolated))
            );
        }
        timeline.play();
    }

    private void applyGradient(Color gradientColor) {
        target.setStyle(
                "-fx-background-color: linear-gradient(to top, " +
                        Theme.toHex(gradientColor) + " 0%, " + Theme.HEX_BASE + " 50%);"
        );
    }

    private Color colorForLevel(int level) {
        return switch (LevelConfig.getTierForLevel(level)) {
            case BEGINNER -> Theme.COLOR_BASE;
            case EASY     -> Theme.GRADIENT_EASY;
            case MEDIUM   -> Theme.GRADIENT_MEDIUM;
            case HARD     -> Theme.GRADIENT_HARD;
            default       -> Theme.GRADIENT_EXPERT;
        };
    }
}