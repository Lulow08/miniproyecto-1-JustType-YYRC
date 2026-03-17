package com.lulow.justtype.view.animations;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Shakes a node horizontally to signal an incorrect answer.
 * Rapidly translates the node left and right, then returns it to its original position.
 */
public class ShakeAnimation extends AnimatorAdapter {

    /** Maximum horizontal displacement in pixels. */
    private static final double SHAKE_DISTANCE_PX = 8.0;

    /** Duration of each individual shake step in milliseconds. */
    private static final int    SHAKE_STEP_MS     = 40;

    /** The timeline that drives the shake keyframes. */
    private final Timeline timeline;

    /**
     * Creates a shake animation for the given node.
     *
     * @param target the node to shake
     */
    public ShakeAnimation(Node target) {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),               new KeyValue(target.translateXProperty(),  0)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS),   new KeyValue(target.translateXProperty(), -SHAKE_DISTANCE_PX)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS*2), new KeyValue(target.translateXProperty(),  SHAKE_DISTANCE_PX)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS*3), new KeyValue(target.translateXProperty(), -SHAKE_DISTANCE_PX)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS*4), new KeyValue(target.translateXProperty(),  SHAKE_DISTANCE_PX)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS*5), new KeyValue(target.translateXProperty(), -SHAKE_DISTANCE_PX)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS*6), new KeyValue(target.translateXProperty(),  0))
        );
    }

    /**
     * Plays the shake from the start.
     */
    @Override
    public void play() {
        timeline.playFromStart();
    }
}