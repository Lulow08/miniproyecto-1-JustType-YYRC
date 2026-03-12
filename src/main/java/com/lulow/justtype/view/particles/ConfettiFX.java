package com.lulow.justtype.view.particles;

import com.lulow.justtype.model.Particle;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ConfettiFX extends ParticleEmitterAdapter{

    private static final int PARTICLES_PER_SPAWN = 4;
    private static final double MIN_OPACITY = 0.4;
    private static final double OPACITY_VARIANCE = 0.6;
    private static final double MIN_PARTICLE_SIZE = 6.0;
    private static final double MAX_PARTICLE_SIZE = 10.0;
    private static final double X_START_PADDING = 2.0;
    private static final double Y_START_PADDING = 10.0;
    private static final double OFF_SCREEN_THRESHOLD = 50.0;

    private final Pane layer;

    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();

    private final double screenWidth;
    private final double screenHeight;

    private boolean emitting = false;
    private long emitEndTime = 0;

    public ConfettiFX(Pane layer, double screenWidth, double screenHeight) {
        this.layer = layer;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        startLoop();
    }

    @Override
    public void play(double seconds) {
        emitting = true;
        emitEndTime = System.currentTimeMillis() + (long)(seconds * 1000);
    }

    private void spawnParticles() {
        for (int i = 0; i < PARTICLES_PER_SPAWN; i++) {

            int direction = random.nextBoolean() ? 1 : -1;

            double xPos = (direction == 1) ? screenWidth - X_START_PADDING : X_START_PADDING;
            double yPos = screenHeight - Y_START_PADDING;

            double size = MIN_PARTICLE_SIZE + (random.nextDouble() * (MAX_PARTICLE_SIZE - MIN_PARTICLE_SIZE));

            Rectangle particleRender = renderParticle(size, xPos, yPos);

            Particle particle = new Particle(xPos, yPos, direction, particleRender);
            particles.add(particle);

            layer.getChildren().add(particleRender);
        }
    }

    private void startLoop() {
        AnimationTimer timer = new AnimationTimer() {

            @Override public void handle(long now) {
                if (emitting) {
                    spawnParticles();

                    if (System.currentTimeMillis() > emitEndTime) {
                        emitting = false;
                    }
                }
                updateParticles();
            }
        };
        timer.start();
    }

    private void updateParticles() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle particle = it.next();
            particle.update();

            if (particle.getYPos() > screenHeight + OFF_SCREEN_THRESHOLD) {
                layer.getChildren().remove(particle.getParticleRender());
                it.remove();
            }
        }
    }

    private Rectangle renderParticle(double size, double xPos, double yPos) {

        Rectangle rectangle = new Rectangle(size, size);

        rectangle.setFill(Color.WHITE);
        rectangle.setOpacity(MIN_OPACITY + random.nextDouble() * OPACITY_VARIANCE);

        rectangle.setX(xPos);
        rectangle.setY(yPos);

        return rectangle;
    }
}