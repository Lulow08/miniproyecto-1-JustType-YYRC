package com.lulow.justtype.model.particles;

import javafx.scene.shape.Rectangle;

/**
 * Default adapter for {@link IParticle}.
 * Holds common position and render fields; subclasses override update logic.
 */
public abstract class ParticleAdapter implements IParticle {

    /** Horizontal position. */
    protected double xPos;

    /** Vertical position. */
    protected double yPos;

    /** The visual rectangle rendered on screen. */
    protected final Rectangle render;

    /**
     * Constructs a particle at the given position with the given render node.
     *
     * @param xPos   initial x position
     * @param yPos   initial y position
     * @param render the rectangle to display
     */
    protected ParticleAdapter(double xPos, double yPos, Rectangle render) {
        this.xPos   = xPos;
        this.yPos   = yPos;
        this.render = render;
    }

    @Override public void      update()    {}
    @Override public boolean   isDead()    { return false; }
    @Override public double    getYPos()   { return yPos; }
    @Override public Rectangle getRender() { return render; }
}