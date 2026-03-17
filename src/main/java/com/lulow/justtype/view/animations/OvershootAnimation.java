package com.lulow.justtype.view.animations;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Produces a bouncy pop effect by scaling a node to an overshoot value,
 * then slightly below normal, then settling back to 1.0.
 */
public class OvershootAnimation extends AnimatorAdapter {

    /** Duration of the initial scale-up hold in milliseconds. */
    private static final int HOLD_DURATION_MS   = 90;

    /** Duration of the scale-down phase in milliseconds. */
    private static final int SHRINK_DURATION_MS = 90;

    /** Duration of the final settle-to-normal phase in milliseconds. */
    private static final int SETTLE_DURATION_MS = 80;

    /** Holds the node at the overshoot scale. */
    private final ScaleTransition hold;

    /** Scales the node down to the undershoot value. */
    private final ScaleTransition shrink;

    /** Returns the node to scale 1.0. */
    private final ScaleTransition settle;

    /**
     * Creates an overshoot animation for the given node.
     *
     * @param target          the node to animate
     * @param overshootScale  scale factor to jump to at the start (e.g. 1.16)
     * @param undershootScale scale factor to dip to before settling (e.g. 0.96)
     */
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

    /**
     * Plays the animation. Cancels any in-progress shrink or settle first.
     */
    @Override
    public void play() {
        shrink.stop();
        settle.stop();
        hold.play();
    }
}