package com.lulow.justtype.model.particles;

import javafx.scene.shape.Rectangle;

public abstract class ParticleAdapter implements IParticle {

    protected double          xPos;
    protected double          yPos;
    protected final Rectangle render;

    protected ParticleAdapter(double xPos, double yPos, Rectangle render) {
        this.xPos   = xPos;
        this.yPos   = yPos;
        this.render = render;
    }

    @Override public void      update()    {}
    @Override public boolean   isDead()    { return false; }
    // @Override public double    getXPos()   { return xPos; }
    @Override public double    getYPos()   { return yPos; }
    @Override public Rectangle getRender() { return render; }
}