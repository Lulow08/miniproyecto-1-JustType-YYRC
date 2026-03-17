package com.lulow.justtype.model.timer;

/**
 * Default no-op adapter for {@link ITimer}.
 * Subclasses override only the methods they need.
 */
public abstract class TimerAdapter implements ITimer {
    @Override public void start(int seconds) {}
    @Override public void stop()             {}
    @Override public int  getSecondsLeft()   { return 0; }
}