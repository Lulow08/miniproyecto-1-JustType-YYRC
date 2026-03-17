package com.lulow.justtype.view.particles;

/**
 * Contract for a particle emitter that spawns and manages particles on a pane.
 */
public interface IParticleEmitter {

    /**
     * Starts emitting particles for the given duration.
     *
     * @param seconds how long to emit, in seconds; use {@link Double#MAX_VALUE} for indefinite
     */
    void play(double seconds);

    /** Stops spawning new particles. Existing particles continue until they die. */
    void stop();

    /**
     * Adjusts the emission rate and speed of newly spawned particles.
     *
     * @param particlesPerFrame number of particles to spawn each frame
     * @param speedMultiplier   multiplier applied to particle speed
     */
    void setIntensity(int particlesPerFrame, double speedMultiplier);
}