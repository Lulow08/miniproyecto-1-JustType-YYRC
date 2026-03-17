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

/**
 * Particle emitter that spawns floating ash particles rising from the bottom of the screen.
 * Active during the HARD and EXPERT difficulty tiers.
 * Runs on a continuous {@link AnimationTimer}; emission can be toggled via {@link #play} and {@link #stop}.
 */
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

    /** The pane where particle rectangles are added. */
    private final Pane   layer;

    /** Screen width used to randomize horizontal spawn positions. */
    private final double screenWidth;

    /** Screen height used as the vertical spawn origin. */
    private final double screenHeight;

    private final Random random = new Random();

    /** Live particles currently on screen. */
    private final List<AshParticle> particles = new ArrayList<>();

    /** Whether the emitter is currently spawning new particles. */
    private boolean active            = false;

    /** Number of particles spawned per animation frame. */
    private int     particlesPerFrame = 1;

    /** Multiplier applied to each particle's upward speed. */
    private double  speedMultiplier   = 0.5;

    /**
     * Creates and starts the ash emitter on the given pane.
     * The internal animation loop runs continuously but only spawns when active.
     *
     * @param layer        the pane to add particle nodes to
     * @param screenWidth  width of the rendering area
     * @param screenHeight height of the rendering area
     */
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

    /**
     * Activates particle spawning. Emission continues until {@link #stop()} is called.
     *
     * @param seconds ignored; ash runs indefinitely until stopped
     */
    @Override
    public void play(double seconds) {
        active = true;
    }

    /**
     * Stops spawning new particles. Existing particles finish their lifecycle normally.
     */
    @Override
    public void stop() {
        active = false;
    }

    /**
     * Sets the emission rate and speed for newly spawned particles.
     *
     * @param newParticlesPerFrame particles to spawn each frame
     * @param newSpeedMultiplier   speed multiplier applied to each new particle
     */
    @Override
    public void setIntensity(int newParticlesPerFrame, double newSpeedMultiplier) {
        this.particlesPerFrame = newParticlesPerFrame;
        this.speedMultiplier   = newSpeedMultiplier;
    }

    /**
     * Spawns a batch of ash particles at the bottom of the screen.
     */
    private void spawnBatch() {
        for (int i = 0; i < particlesPerFrame; i++) {
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

    /**
     * Updates all live particles and removes dead ones from the scene.
     */
    private void updateParticles() {
        Iterator<AshParticle> it = particles.iterator();
        while (it.hasNext()) {
            AshParticle p = it.next();
            p.update();
            if (p.isDead()) {
                layer.getChildren().remove(p.getRender());
                it.remove();
            }
        }
    }
}