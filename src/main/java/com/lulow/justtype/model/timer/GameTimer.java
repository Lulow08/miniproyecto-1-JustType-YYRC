package com.lulow.justtype.model.timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Countdown timer backed by a JavaFX {@link Timeline}.
 * Fires a tick callback each second and a time-up callback when it reaches zero.
 */
public class GameTimer extends TimerAdapter {

    private Timeline timeline;
    private int      secondsLeft;

    /** Called once per second while the timer is running. */
    private final Runnable onTick;

    /** Called when the countdown reaches zero. */
    private final Runnable onTimeUp;

    /**
     * Creates a new GameTimer with the given callbacks.
     *
     * @param onTick   invoked each second
     * @param onTimeUp invoked when time runs out
     */
    public GameTimer(Runnable onTick, Runnable onTimeUp) {
        this.onTick   = onTick;
        this.onTimeUp = onTimeUp;
    }

    /**
     * Starts the countdown from the specified number of seconds.
     * Stops any currently running timer first.
     *
     * @param seconds initial countdown value
     */
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

    /** Stops the timeline if it is running. */
    @Override
    public void stop() {
        if (timeline != null) { timeline.stop(); }
    }

    /**
     * Returns the number of seconds remaining.
     *
     * @return seconds left
     */
    @Override public int getSecondsLeft() { return secondsLeft; }
}