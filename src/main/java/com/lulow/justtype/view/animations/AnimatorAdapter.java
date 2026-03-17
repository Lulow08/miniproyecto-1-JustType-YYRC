package com.lulow.justtype.view.animations;

/**
 * Default no-op adapter for {@link IAnimator}.
 * Subclasses override {@link #play()} with their specific animation logic.
 */
public abstract class AnimatorAdapter implements IAnimator {

    /**
     * Default implementation does nothing.
     * Override in subclasses to define the animation behavior.
     */
    @Override
    public void play() {}
}