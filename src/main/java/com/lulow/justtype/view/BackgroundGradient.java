package com.lulow.justtype.view;

import com.lulow.justtype.model.LevelConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Animates the background gradient of a pane based on the current difficulty tier.
 * Smoothly interpolates between tier-specific colors over a fixed duration
 * each time the tier changes.
 */
public class BackgroundGradient {

    /** Total duration of each color transition in milliseconds. */
    private static final int ANIMATION_DURATION_MS = 1400;

    /** Number of interpolation steps in each transition. */
    private static final int ANIMATION_STEPS       = 24;

    /** The pane whose background style is being updated. */
    private final Pane target;

    /** The color currently applied to the background. */
    private Color currentColor = Theme.COLOR_BASE;

    /**
     * Creates a background gradient manager for the given pane
     * and applies the base color immediately.
     *
     * @param target the pane to manage the background of
     */
    public BackgroundGradient(Pane target) {
        this.target = target;
        applyGradient(Theme.COLOR_BASE);
    }

    /**
     * Updates the background color for the given level.
     * If the tier has not changed, nothing happens.
     *
     * @param level the current game level
     */
    public void updateForLevel(int level) {
        Color nextColor = colorForLevel(level);
        if (nextColor.equals(currentColor)) return;
        animateTo(nextColor);
        currentColor = nextColor;
    }

    /**
     * Animates the background from the current color to the target color.
     *
     * @param targetColor the destination color
     */
    private void animateTo(Color targetColor) {
        Color    startColor = currentColor;
        Timeline timeline   = new Timeline();

        for (int step = 0; step <= ANIMATION_STEPS; step++) {
            double progress     = (double) step / ANIMATION_STEPS;
            Color  interpolated = startColor.interpolate(targetColor, progress);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(ANIMATION_DURATION_MS * progress),
                            e -> applyGradient(interpolated))
            );
        }
        timeline.play();
    }

    /**
     * Applies a linear gradient CSS style to the target pane using the given color
     * as the bottom stop, blending into the base background color at 50%.
     *
     * @param gradientColor the bottom color of the gradient
     */
    private void applyGradient(Color gradientColor) {
        target.setStyle(
                "-fx-background-color: linear-gradient(to top, " +
                        Theme.toHex(gradientColor) + " 0%, " + Theme.HEX_BASE + " 50%);"
        );
    }

    /**
     * Returns the gradient color that corresponds to the given level's tier.
     *
     * @param level the current level
     * @return the target gradient color
     */
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