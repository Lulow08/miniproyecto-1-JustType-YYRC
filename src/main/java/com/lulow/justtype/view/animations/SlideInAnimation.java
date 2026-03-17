package com.lulow.justtype.view.animations;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class SlideInAnimation extends AnimatorAdapter {

    private static final int SLIDE_DURATION_MS  = 160;
    private static final int SETTLE_DURATION_MS = 90;

    private final TranslateTransition slide;
    private final TranslateTransition settle;

    private final Node   target;
    private final double fromY;

    public SlideInAnimation(Node target, double fromY, double overshoot) {
        this.target = target;
        this.fromY  = fromY;

        slide = new TranslateTransition(Duration.millis(SLIDE_DURATION_MS), target);
        slide.setToY(overshoot);

        settle = new TranslateTransition(Duration.millis(SETTLE_DURATION_MS), target);
        settle.setToY(0);

        slide.setOnFinished(finishEvent -> settle.play());
    }

    @Override
    public void play() {
        target.setTranslateY(fromY);
        settle.stop();
        slide.play();
    }
}