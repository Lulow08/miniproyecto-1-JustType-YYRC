package com.lulow.justtype.view;

import com.lulow.justtype.model.LevelConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BackgroundGradient {

    private static final Color COLOR_T1 = Theme.COLOR_BASE;
    private static final Color COLOR_T2 = Color.web("#0e0914");
    private static final Color COLOR_T3 = Color.web("#120a1c");
    private static final Color COLOR_T4 = Color.web("#8B80F9");
    private static final Color COLOR_T5 = Color.web("#F45B69");

    private static final int ANIMATION_DURATION_MS = 1200;
    private static final int ANIMATION_STEPS = 24;

    private final Pane target;
    private Color currentColor = COLOR_T1;

    public BackgroundGradient(Pane target) {
        this.target = target;
        applyGradient(COLOR_T1);
    }

    public void updateForLevel(int level) {
        Color nextColor = colorForLevel(level);
        if (nextColor.equals(currentColor)) return;
        animateTo(nextColor);
        currentColor = nextColor;
    }

    private void animateTo(Color targetColor) {
        Color startColor = currentColor;
        Timeline timeline = new Timeline();

        for (int steps = 0; steps <= ANIMATION_STEPS; steps++) {
            double progress = (double) steps / ANIMATION_STEPS;
            Color interpolated = startColor.interpolate(targetColor, progress);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(ANIMATION_DURATION_MS * progress), event -> applyGradient(interpolated))
            );
        }
        timeline.play();
    }

    private void applyGradient(Color gradientColor) {
        target.setStyle(
                "-fx-background-color: linear-gradient(to top, " +
                        toHex(gradientColor) + " 0%, " + Theme.HEX_BASE + " 50%);"
        );
    }

    private Color colorForLevel(int level) {
        return switch (LevelConfig.getTierForLevel(level)) {
            case T1 -> COLOR_T1;
            case T2 -> COLOR_T2;
            case T3 -> COLOR_T3;
            case T4 -> COLOR_T4;
            default -> COLOR_T5;
        };
    }

    private String toHex(Color c) {
        return String.format("#%02X%02X%02X",
                (int)(c.getRed()   * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue()  * 255)
        );
    }
}