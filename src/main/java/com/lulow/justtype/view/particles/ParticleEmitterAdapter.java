package com.lulow.justtype.view.particles;

public abstract class ParticleEmitterAdapter implements IParticleEmitter {
    @Override public void play(double seconds) {}
    @Override public void stop() {}
    @Override public void setIntensity(int particlesPerFrame, double speedMultiplier) {}
}