package com.lulow.justtype.view.particles;

import com.lulow.justtype.model.particles.ConfettiParticle;
import com.lulow.justtype.view.Theme;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Particle emitter that fires confetti from the left and right edges of the screen.
 * Supports a white mode (used during gameplay) and a colorful mode (used on the win screen).
 * Particles follow a parabolic arc under gravity and are removed once they leave the screen.
 */
public class ConfettiFX extends ParticleEmitterAdapter {

    private static final int    PARTICLES_PER_SPAWN  = 4;
    private static final double MIN_OPACITY          = 0.4;
    private static final double OPACITY_VARIANCE     = 0.6;
    private static final double MIN_PARTICLE_SIZE    = 6.0;
    private static final double MAX_PARTICLE_SIZE    = 10.0;
    private static final double X_START_PADDING      = 2.0;
    private static final double Y_START_PADDING      = 10.0;
    private static final double OFF_SCREEN_THRESHOLD = 50.0;

    /** The pane where confetti rectangles are added. */
    private final Pane   layer;

    /** Screen width used to place spawn points at the edges. */
    private final double screenWidth;

    /** Screen height used to detect when particles fall off screen. */
    private final double screenHeight;

    private final Random random = new Random();

    /** Live confetti particles currently on screen. */
    private final List<ConfettiParticle> particles = new ArrayList<>();

    /** Whether the emitter is currently spawning particles. */
    private boolean emitting    = false;

    /** Timestamp (ms) after which emission stops. */
    private long    emitEndTime = 0;

    /** Whether to use the full color palette instead of white. */
    private boolean colorful    = false;

    /**
     * Creates the confetti emitter and starts its internal animation loop.
     *
     * @param layer        the pane to add particle nodes to
     * @param screenWidth  width of the rendering area
     * @param screenHeight height of the rendering area
     */
    public ConfettiFX(Pane layer, double screenWidth, double screenHeight) {
        this.layer        = layer;
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;
        startLoop();
    }

    /**
     * Emits white confetti for the given duration.
     *
     * @param seconds how long to emit, in seconds
     */
    @Override
    public void play(double seconds) {
        colorful    = false;
        emitting    = true;
        emitEndTime = System.currentTimeMillis() + (long)(seconds * 1000);
    }

    /**
     * Emits colorful confetti (using {@link Theme#CONFETTI_PALETTE}) for the given duration.
     *
     * @param seconds how long to emit, in seconds
     */
    public void playColorful(double seconds) {
        colorful    = true;
        emitting    = true;
        emitEndTime = System.currentTimeMillis() + (long)(seconds * 1000);
    }

    /**
     * Stops spawning new particles immediately.
     */
    @Override
    public void stop() {
        emitting = false;
    }

    /**
     * Spawns a batch of confetti particles from each side of the screen.
     */
    private void spawnParticles() {
        for (int i = 0; i < PARTICLES_PER_SPAWN; i++) {
            int    direction = random.nextBoolean() ? 1 : -1;
            double xPos      = (direction == 1) ? screenWidth - X_START_PADDING : X_START_PADDING;
            double yPos      = screenHeight - Y_START_PADDING;
            double size      = MIN_PARTICLE_SIZE + random.nextDouble() * (MAX_PARTICLE_SIZE - MIN_PARTICLE_SIZE);

            Color color = colorful
                    ? Theme.CONFETTI_PALETTE[random.nextInt(Theme.CONFETTI_PALETTE.length)]
                    : Color.WHITE;

            Rectangle render = new Rectangle(size, size);
            render.setFill(color);
            render.setOpacity(MIN_OPACITY + random.nextDouble() * OPACITY_VARIANCE);
            render.setX(xPos);
            render.setY(yPos);

            ConfettiParticle particle = new ConfettiParticle(xPos, yPos, direction, render);
            particles.add(particle);
            layer.getChildren().add(render);
        }
    }

    /**
     * Starts the animation loop that drives spawning and updates each frame.
     */
    private void startLoop() {
        new AnimationTimer() {
            @Override public void handle(long now) {
                if (emitting) {
                    spawnParticles();
                    if (System.currentTimeMillis() > emitEndTime) emitting = false;
                }
                updateParticles();
            }
        }.start();
    }

    /**
     * Updates all live particles and removes those that have fallen off screen.
     */
    private void updateParticles() {
        Iterator<ConfettiParticle> it = particles.iterator();
        while (it.hasNext()) {
            ConfettiParticle p = it.next();
            p.update();
            if (p.getYPos() > screenHeight + OFF_SCREEN_THRESHOLD) {
                layer.getChildren().remove(p.getRender());
                it.remove();
            }
        }
    }
}