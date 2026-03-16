package com.lulow.justtype.view.particles;

import com.lulow.justtype.model.particles.ConfettiParticle;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ConfettiFX extends ParticleEmitterAdapter {

    private static final int    PARTICLES_PER_SPAWN  = 4;
    private static final double MIN_OPACITY          = 0.4;
    private static final double OPACITY_VARIANCE     = 0.6;
    private static final double MIN_PARTICLE_SIZE    = 6.0;
    private static final double MAX_PARTICLE_SIZE    = 10.0;
    private static final double X_START_PADDING      = 2.0;
    private static final double Y_START_PADDING      = 10.0;
    private static final double OFF_SCREEN_THRESHOLD = 50.0;

    private static final Color[] PALETTE = {
            Color.web("#FF4C4C"), Color.web("#07DACC"), Color.web("#FFD700"),
            Color.web("#FF9900"), Color.web("#CC44FF"), Color.web("#44AAFF"),
            Color.web("#FF66AA"), Color.web("#AAFFAA")
    };

    private final Pane   layer;
    private final double screenWidth;
    private final double screenHeight;
    private final Random random = new Random();

    private final List<ConfettiParticle> particles = new ArrayList<>();

    private boolean emitting     = false;
    private long    emitEndTime  = 0;
    private boolean colorful     = false;

    public ConfettiFX(Pane layer, double screenWidth, double screenHeight) {
        this.layer        = layer;
        this.screenWidth  = screenWidth;
        this.screenHeight = screenHeight;
        startLoop();
    }

    @Override
    public void play(double seconds) {
        colorful    = false;
        emitting    = true;
        emitEndTime = System.currentTimeMillis() + (long)(seconds * 1000);
    }

    public void playColorful(double seconds) {
        colorful    = true;
        emitting    = true;
        emitEndTime = System.currentTimeMillis() + (long)(seconds * 1000);
    }

    @Override
    public void stop() {
        emitting = false;
    }

    private void spawnParticles() {
        for (int i = 0; i < PARTICLES_PER_SPAWN; i++) {
            int    direction = random.nextBoolean() ? 1 : -1;
            double xPos      = (direction == 1) ? screenWidth - X_START_PADDING : X_START_PADDING;
            double yPos      = screenHeight - Y_START_PADDING;
            double size      = MIN_PARTICLE_SIZE + random.nextDouble() * (MAX_PARTICLE_SIZE - MIN_PARTICLE_SIZE);

            Color color = colorful
                    ? PALETTE[random.nextInt(PALETTE.length)]
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