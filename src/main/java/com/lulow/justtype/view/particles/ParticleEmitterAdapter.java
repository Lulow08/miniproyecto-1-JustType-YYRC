package com.lulow.justtype.view.particles;

/**
 * Default no-op adapter for {@link IParticleEmitter}.
 * Subclasses override only the methods they need.
 */
public abstract class ParticleEmitterAdapter implements IParticleEmitter {

    /** Default implementation does nothing. */
    @Override public void play(double seconds) {}

    /** Default implementation does nothing. */
    @Override public void stop() {}

    /** Default implementation does nothing. */
    @Override public void setIntensity(int particlesPerFrame, double speedMultiplier) {}
}