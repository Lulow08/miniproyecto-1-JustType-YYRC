package com.lulow.justtype.view.animations;

/**
 * Contract for a reusable, single-method UI animation.
 */
public interface IAnimator {

    /**
     * Plays the animation. Implementations should restart it
     * if it is already running.
     */
    void play();
}