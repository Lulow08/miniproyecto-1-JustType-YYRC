package com.lulow.justtype.model.timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameTimer extends TimerAdapter {

    private Timeline timeline;
    private int      secondsLeft;

    private final Runnable onTick;
    private final Runnable onTimeUp;

    public GameTimer(Runnable onTick, Runnable onTimeUp) {
        this.onTick   = onTick;
        this.onTimeUp = onTimeUp;
    }

    @Override
    public void start(int seconds) {
        stop();
        secondsLeft = seconds;

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), keyFrameEvent -> {
            secondsLeft--;
            if (secondsLeft <= 0) {
                secondsLeft = 0;
                stop();
                onTimeUp.run();
            } else {
                onTick.run();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @Override
    public void stop() {
        if (timeline != null) { timeline.stop(); }
    }

    @Override public int getSecondsLeft() { return secondsLeft; }
}