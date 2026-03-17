package com.lulow.justtype.model.timer;

public abstract class TimerAdapter implements ITimer {
    @Override public void start(int seconds) {}
    @Override public void stop()             {}
    @Override public int  getSecondsLeft()   { return 0; }
}