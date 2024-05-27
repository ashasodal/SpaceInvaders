package com.sodal.entity;

import java.awt.*;

public class Explosion extends Entity {
    public Explosion(String imagePath, double scale) {
        super(imagePath, scale);
        ;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(this.getBufferedImage(), this.getX(), this.getY(), null);
    }

    @Override
    public void update() {

    }
}
