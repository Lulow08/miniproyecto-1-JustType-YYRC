package com.lulow.justtype.view.animations;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Fades a JavaFX node from fully transparent to fully opaque.
 */
public class FadeInAnimation extends AnimatorAdapter {

    /** Duration of the fade transition in milliseconds. */
    private static final int FADE_DURATION_MS = 1000;

    /** The underlying JavaFX transition. */
    private final FadeTransition fadeTransition;

    /**
     * Creates a fade-in animation for the given node.
     *
     * @param target the node to fade in
     */
    public FadeInAnimation(Node target) {
        fadeTransition = new FadeTransition(Duration.millis(FADE_DURATION_MS), target);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
    }

    /**
     * Plays the fade-in from the beginning.
     */
    @Override
    public void play() {
        fadeTransition.playFromStart();
    }
}