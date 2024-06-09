package com.sodal.entity;

import com.sodal.gui.GameScreen;

import java.awt.*;

public class Bullet extends Entity {

    private int speed = 6;

    private static int enemyBulletSpeed = 4;

    private Rectangle bulletRect;

    private String imagePath;

    public Bullet(String imagePath, double scale) {
        super(imagePath, scale);
        this.imagePath = imagePath;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(getBufferedImage(), this.getX(), this.getY(), null);
        if (imagePath.equals("./res/player/bullet/bulletSpaceShip.png")) {
            update();
        }
    }

    @Override
    public void update() {
        //spaceShip bullet.
        if (imagePath.equals("./res/player/bullet/bulletSpaceShip.png")) {
            setLocation(getX(), getY() - speed);
            bulletRect.setLocation(getX(), getY());

            if (getY() <= 0) {
               Player.setBullet(null);
            }
        }
        //enemy bullet.
        else {
            this.setLocation(getX(), getY() + enemyBulletSpeed);
            this.bulletRect.y = this.getY();
        }
    }

    public void setBulletRect(Rectangle bulletRect) {
        this.bulletRect = bulletRect;
    }


    public Rectangle getBulletRect() {
        return bulletRect;
    }
}
