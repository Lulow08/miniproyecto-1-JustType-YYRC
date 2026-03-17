package com.lulow.justtype.view.animations;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class PressAnimation extends AnimatorAdapter {

    private static final int PRESS_DURATION_MS   = 80;
    private static final int RELEASE_DURATION_MS = 100;

    private final ScaleTransition pressIn;
    private final ScaleTransition pressOut;

    public PressAnimation(Node target, double pressScale) {
        pressIn = new ScaleTransition(Duration.millis(PRESS_DURATION_MS), target);
        pressIn.setToX(pressScale);
        pressIn.setToY(pressScale);

        pressOut = new ScaleTransition(Duration.millis(RELEASE_DURATION_MS), target);
        pressOut.setToX(1.0);
        pressOut.setToY(1.0);

        pressIn.setOnFinished(finishEvent -> pressOut.play());
    }

    @Override
    public void play() {
        pressOut.stop();
        pressIn.play();
    }
}