package com.lulow.justtype.view.particles;

import com.lulow.justtype.model.particles.AshParticle;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AshFX extends ParticleEmitterAdapter {

    private static final double MIN_SIZE      = 3.0;
    private static final double MAX_SIZE      = 7.0;
    private static final double MIN_OPACITY   = 0.4;
    private static final double MAX_OPACITY   = 0.8;
    private static final double MIN_SPEED     = 0.09;
    private static final double MAX_SPEED     = 0.4;
    private static final double MAX_DRIFT     = 0.1;
    private static final double MIN_FADE_RATE = 0.002;
    private static final double MAX_FADE_RATE = 0.006;

    private final Pane   layer;
    private final double screenWidth;
    private final double screenHeight;
    private final Random random = new Random();

    private final List<AshParticle> particles = new ArrayList<>();

    private boolean active            = false;
    private int     particlesPerFrame = 1;
    private double  speedMultiplier   = 0.5;

    public AshFX(Pane layer, double screenWidth, double screenHeight) {
        this.layer        = layer;
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;

        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (active) spawnBatch();
                updateParticles();
            }
        };
        loop.start();
    }

    @Override
    public void play(double seconds) {
        active = true;
    }

    @Override
    public void stop() {
        active = false;
    }

    @Override
    public void setIntensity(int newParticlesPerFrame, double newSpeedMultiplier) {
        this.particlesPerFrame = newParticlesPerFrame;
        this.speedMultiplier   = newSpeedMultiplier;
    }

    private void spawnBatch() {
        for (int spawnIndex = 0; spawnIndex < particlesPerFrame; spawnIndex++) {
            double xPos     = random.nextDouble() * screenWidth;
            double yPos     = screenHeight;
            double size     = MIN_SIZE + random.nextDouble() * (MAX_SIZE - MIN_SIZE);
            double opacity  = MIN_OPACITY + random.nextDouble() * (MAX_OPACITY - MIN_OPACITY);
            double speed    = (MIN_SPEED + random.nextDouble() * (MAX_SPEED - MIN_SPEED)) * speedMultiplier;
            double drift    = (random.nextDouble() * 2 - 1) * MAX_DRIFT;
            double fadeRate = MIN_FADE_RATE + random.nextDouble() * (MAX_FADE_RATE - MIN_FADE_RATE);

            Rectangle render = new Rectangle(size, size);
            render.setFill(Color.WHITE);
            render.setOpacity(opacity);
            render.setX(xPos);
            render.setY(yPos);

            AshParticle particle = new AshParticle(xPos, yPos, drift, speed, opacity, fadeRate, render);
            particles.add(particle);
            layer.getChildren().add(render);
        }
    }

    private void updateParticles() {
        Iterator<AshParticle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            AshParticle particle = iterator.next();
            particle.update();
            if (particle.isDead()) {
                layer.getChildren().remove(particle.getRender());
                iterator.remove();
            }
        }
    }
}