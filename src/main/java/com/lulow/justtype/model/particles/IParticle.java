package com.lulow.justtype.model.particles;

import javafx.scene.shape.Rectangle;

/**
 * Contract for a single particle in a particle system.
 */
public interface IParticle {

    /** Updates the particle's position and state for the current frame. */
    void update();

    /**
     * Returns whether the particle should be removed from the scene.
     *
     * @return {@code true} if the particle is no longer visible or active
     */
    boolean isDead();

    /**
     * Returns the vertical position of the particle.
     *
     * @return y coordinate
     */
    double getYPos();

    /**
     * Returns the visual node representing this particle.
     *
     * @return the {@link Rectangle} render object
     */
    Rectangle getRender();
}