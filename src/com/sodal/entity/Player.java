package com.sodal.entity;

import com.sodal.handler.KeyHandler;

import java.awt.*;

public class Player extends Entity {

    private int xSpeed;

    private KeyHandler handler;

    private Bullet bullet;



    public Player(String imagePath, KeyHandler handler) {
        super(imagePath, 3);
        this.handler = handler;
        this.xSpeed = 5;
    }


    @Override
    public void update() {

        if (handler.isLeft()) {
            this.setLocation(this.getX() - this.xSpeed, this.getY());
        }
        if (handler.isRight()) {
            this.setLocation(this.getX() + this.xSpeed, this.getY());
        }
        if (handler.isShoot()) {

            createBullet();
        }

    }


    private void createBullet() {
        if (bullet == null) {
            bullet = new Bullet("./res/player/bullet/bulletSpaceShip.png", 2);
            bullet.setLocation(getX() + (getTileSize() - bullet.getWidth()) / 2, getY() - bullet.getHeight());
            Rectangle bulletRect = new Rectangle(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
            bullet.setBulletRect(bulletRect);

        }

    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(this.getBufferedImage(), this.getX(), this.getY(), null);
        if (bullet != null) {
            bullet.render(g2);
        }
    }

    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }




}
