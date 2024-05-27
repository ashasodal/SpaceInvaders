package com.sodal.entity;

import java.awt.*;

public class Bullet extends Entity {

    private int speed;

    private Rectangle bulletRect;

    public Bullet(String imagePath, double scale) {
        super(imagePath, scale);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(getBufferedImage(), this.getX(), this.getY(), null);
    }

    @Override
    public void update() {
        setLocation(getX(), getY() - speed);
        bulletRect.setLocation(getX(), getY());
        speed = 6;
    }

    public void setBulletRect(Rectangle bulletRect) {
        this.bulletRect = bulletRect;
    }


    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Rectangle getBulletRect() {
        return bulletRect;
    }

   }


