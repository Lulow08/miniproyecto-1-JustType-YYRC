package com.lulow.justtype.view.animations;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Slides a node in from a vertical offset, overshoots slightly, then settles at Y=0.
 * Used for entrance animations on HUD elements.
 */
public class SlideInAnimation extends AnimatorAdapter {

    /** Duration of the initial slide movement in milliseconds. */
    private static final int SLIDE_DURATION_MS  = 160;

    /** Duration of the settle-to-zero phase in milliseconds. */
    private static final int SETTLE_DURATION_MS = 90;

    /** Moves the node from the offset toward the overshoot position. */
    private final TranslateTransition slide;

    /** Settles the node back to translateY = 0. */
    private final TranslateTransition settle;

    /** The node being animated. */
    private final Node   target;

    /** The starting Y offset (negative = above, positive = below). */
    private final double fromY;

    /**
     * Creates a slide-in animation for the given node.
     *
     * @param target    the node to animate
     * @param fromY     the starting Y translation offset
     * @param overshoot the Y value to briefly reach before settling (e.g. 1.0 for a small bounce)
     */
    public SlideInAnimation(Node target, double fromY, double overshoot) {
        this.target = target;
        this.fromY  = fromY;

        slide = new TranslateTransition(Duration.millis(SLIDE_DURATION_MS), target);
        slide.setToY(overshoot);

        settle = new TranslateTransition(Duration.millis(SETTLE_DURATION_MS), target);
        settle.setToY(0);

        slide.setOnFinished(e -> settle.play());
    }

    /**
     * Repositions the node at {@code fromY} and plays the slide-in sequence.
     */
    @Override
    public void play() {
        target.setTranslateY(fromY);
        settle.stop();
        slide.play();
    }
}