package com.lulow.justtype.view.animations;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class OvershootAnimation extends AnimatorAdapter {

    private static final int HOLD_DURATION_MS   = 90;
    private static final int SHRINK_DURATION_MS = 90;
    private static final int SETTLE_DURATION_MS = 80;

    private final ScaleTransition hold;
    private final ScaleTransition shrink;
    private final ScaleTransition settle;

    public OvershootAnimation(Node target, double overshootScale, double undershootScale) {
        hold = new ScaleTransition(Duration.millis(HOLD_DURATION_MS), target);
        hold.setFromX(overshootScale);
        hold.setFromY(overshootScale);
        hold.setToX(overshootScale);
        hold.setToY(overshootScale);

        shrink = new ScaleTransition(Duration.millis(SHRINK_DURATION_MS), target);
        shrink.setToX(undershootScale);
        shrink.setToY(undershootScale);

        settle = new ScaleTransition(Duration.millis(SETTLE_DURATION_MS), target);
        settle.setToX(1.0);
        settle.setToY(1.0);

        hold.setOnFinished(e -> shrink.play());
        shrink.setOnFinished(e -> settle.play());
    }

    @Override
    public void play() {
        shrink.stop();
        settle.stop();
        hold.play();
    }
}