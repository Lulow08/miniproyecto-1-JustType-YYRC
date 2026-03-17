package com.lulow.justtype.model.particles;

import javafx.scene.shape.Rectangle;

public interface IParticle {
    void      update();
    boolean   isDead();
    // double    getXPos();
    double    getYPos();
    Rectangle getRender();
}
