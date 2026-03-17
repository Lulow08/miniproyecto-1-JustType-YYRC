package com.lulow.justtype.model.timer;

/**
 * Contract for a countdown timer used during gameplay.
 */
public interface ITimer {

    /**
     * Starts the timer with the given number of seconds.
     *
     * @param seconds total countdown seconds
     */
    void start(int seconds);

    /** Stops the timer immediately. */
    void stop();

    /**
     * Returns the remaining seconds.
     *
     * @return seconds left
     */
    int getSecondsLeft();
}