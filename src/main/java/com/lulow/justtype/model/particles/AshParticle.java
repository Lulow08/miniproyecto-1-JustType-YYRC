package com.lulow.justtype.model.particles;

import javafx.scene.shape.Rectangle;

/**
 * A particle that floats upward and fades out, used for the ash effect
 * in higher difficulty tiers.
 */
public class AshParticle extends ParticleAdapter {

    private final double xDrift;
    private final double ySpeed;
    private final double fadeRate;
    private double       opacity;

    /**
     * Creates an ash particle.
     *
     * @param xPos     initial x position
     * @param yPos     initial y position
     * @param xDrift   horizontal drift per frame
     * @param ySpeed   upward speed per frame
     * @param opacity  initial opacity
     * @param fadeRate how much opacity is lost per frame
     * @param render   the rectangle to display
     */
    public AshParticle(double xPos, double yPos, double xDrift, double ySpeed,
                       double opacity, double fadeRate, Rectangle render) {
        super(xPos, yPos, render);
        this.xDrift   = xDrift;
        this.ySpeed   = ySpeed;
        this.opacity  = opacity;
        this.fadeRate = fadeRate;
    }

    /** Moves the particle upward, drifts horizontally, and fades it out. */
    @Override
    public void update() {
        xPos    += xDrift;
        yPos    -= ySpeed;
        opacity -= fadeRate;

        render.setX(xPos);
        render.setY(yPos);
        render.setOpacity(Math.max(opacity, 0));
    }

    /**
     * Returns {@code true} when the particle is fully transparent.
     *
     * @return {@code true} if opacity has reached zero
     */
    @Override
    public boolean isDead() { return opacity <= 0; }
}