package com.lulow.justtype.view.animations;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class ShakeAnimation extends AnimatorAdapter {

    private static final double SHAKE_DISTANCE_PX = 8.0;
    private static final int    SHAKE_STEP_MS     = 40;

    private final Timeline timeline;

    public ShakeAnimation(Node target) {
        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),                  new KeyValue(target.translateXProperty(),  0)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS),      new KeyValue(target.translateXProperty(), -SHAKE_DISTANCE_PX)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS * 2),  new KeyValue(target.translateXProperty(),  SHAKE_DISTANCE_PX)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS * 3),  new KeyValue(target.translateXProperty(), -SHAKE_DISTANCE_PX)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS * 4),  new KeyValue(target.translateXProperty(),  SHAKE_DISTANCE_PX)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS * 5),  new KeyValue(target.translateXProperty(), -SHAKE_DISTANCE_PX)),
                new KeyFrame(Duration.millis(SHAKE_STEP_MS * 6),  new KeyValue(target.translateXProperty(),  0))
        );
    }

    @Override
    public void play() {
        timeline.playFromStart();
    }
}