package com.sodal.entity;

import java.awt.*;

public class Bullet extends Entity {

    public Bullet(String imagePath, int scale) {
        super(imagePath, scale);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(getBufferedImage(), this.getX(), this.getY(), null);
    }

    @Override
    public void update() {
        setLocation(getX(),getY() -1);
    }


}
