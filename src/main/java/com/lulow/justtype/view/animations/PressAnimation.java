package com.lulow.justtype.view.animations;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Simulates a physical press by scaling a node down, then releasing it back to full size.
 * Intended for buttons and input fields.
 */
public class PressAnimation extends AnimatorAdapter {

    /** Duration of the scale-down phase in milliseconds. */
    private static final int PRESS_DURATION_MS   = 80;

    /** Duration of the scale-up release phase in milliseconds. */
    private static final int RELEASE_DURATION_MS = 100;

    /** Scales the node down to simulate a press. */
    private final ScaleTransition pressIn;

    /** Scales the node back up to simulate a release. */
    private final ScaleTransition pressOut;

    /**
     * Creates a press animation for the given node.
     *
     * @param target     the node to animate
     * @param pressScale the scale to shrink to on press (e.g. 0.96)
     */
    public PressAnimation(Node target, double pressScale) {
        pressIn = new ScaleTransition(Duration.millis(PRESS_DURATION_MS), target);
        pressIn.setToX(pressScale);
        pressIn.setToY(pressScale);

        pressOut = new ScaleTransition(Duration.millis(RELEASE_DURATION_MS), target);
        pressOut.setToX(1.0);
        pressOut.setToY(1.0);

        pressIn.setOnFinished(e -> pressOut.play());
    }

    /**
     * Plays the press-and-release animation.
     * Cancels any ongoing release transition before starting.
     */
    @Override
    public void play() {
        pressOut.stop();
        pressIn.play();
    }
}