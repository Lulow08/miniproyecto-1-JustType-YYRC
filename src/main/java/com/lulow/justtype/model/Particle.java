package com.lulow.justtype.model;

import javafx.scene.shape.Rectangle;

public class Particle {

    private static final double GRAVITY = 0.16;
    private static final double ROTATION_SPEED = 4.0;
    private static final double MIN_X_VELOCITY = 1.0;
    private static final double MIN_Y_VELOCITY = -2.0;
    private static final double MAX_X_VELOCITY_VARIANCE = 3.0;
    private static final double MAX_Y_VELOCITY_VARIANCE = -4.0;

    private double xPos, yPos, xVelocity, yVelocity;
    private final Rectangle particleRender;

    public Particle(double xPos, double yPos, int direction, Rectangle particleRender) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.particleRender = particleRender;

        this.xVelocity = -direction * (MIN_X_VELOCITY + Math.random() * MAX_X_VELOCITY_VARIANCE);
        this.yVelocity = MIN_Y_VELOCITY + Math.random() * MAX_Y_VELOCITY_VARIANCE;
    }

    public void update() {
        yVelocity += GRAVITY;
        xPos += xVelocity;
        yPos += yVelocity;

        particleRender.setX(xPos);
        particleRender.setY(yPos);
        particleRender.setRotate(particleRender.getRotate() + ROTATION_SPEED);
    }

    public double getYPos() { return yPos; }
    public double getXPos() { return xPos; }
    public Rectangle getParticleRender() { return particleRender; }
}