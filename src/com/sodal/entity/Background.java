package com.sodal.entity;

import java.awt.*;

public class Background extends Entity {
    public Background(String imagePath, double scale) {
        super(imagePath, scale);
    }

    @Override
    public void render(Graphics2D g2) {

        g2.drawImage(getBufferedImage(), getX(), getY(),null);

    }

    @Override
    public void update() {

    }
}
