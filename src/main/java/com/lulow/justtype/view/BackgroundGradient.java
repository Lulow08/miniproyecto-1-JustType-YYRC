package com.lulow.justtype.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BackgroundGradient {

    private static final Color COLOR_T1 = Color.web("#0C0910");
    private static final Color COLOR_T2 = Color.web("#0e0914");
    private static final Color COLOR_T3 = Color.web("#120a1c");
    private static final Color COLOR_T4 = Color.web("#8B80F9");
    private static final Color COLOR_T5 = Color.web("#F45B69");
    private static final int   ANIM_MS  = 1200;
    private static final int   STEPS    = 24;

    private final Pane target;
    private Color currentColor = COLOR_T1;

    public BackgroundGradient(Pane target) {
        this.target = target;
        applyGradient(COLOR_T1);
    }

    public void updateForLevel(int level) {
        Color next = colorForLevel(level);
        if (next.equals(currentColor)) return;
        animateTo(next);
        currentColor = next;
    }

    private void animateTo(Color to) {
        Color from = currentColor;
        Timeline timeline = new Timeline();
        for (int i = 0; i <= STEPS; i++) {
            double t = (double) i / STEPS;
            Color interpolated = from.interpolate(to, t);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(ANIM_MS * t), e -> applyGradient(interpolated))
            );
        }
        timeline.play();
    }

    private void applyGradient(Color gradientColor) {
        target.setStyle(
                "-fx-background-color: linear-gradient(to top, " +
                        toHex(gradientColor) + " 0%, #0C0910 50%);"
        );
    }

    private Color colorForLevel(int level) {
        if (level <= 5)  return COLOR_T1;
        if (level <= 10) return COLOR_T2;
        if (level <= 20) return COLOR_T3;
        if (level <= 35) return COLOR_T4;
        return COLOR_T5;
    }

    private String toHex(Color c) {
        return String.format("#%02X%02X%02X",
                (int)(c.getRed()   * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue()  * 255)
        );
    }
}