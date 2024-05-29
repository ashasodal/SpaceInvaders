package com.sodal.entity;

import com.sodal.gui.GameScreen;
import com.sodal.handler.KeyHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {

    private int xSpeed;

    private KeyHandler handler;

    private Bullet bullet;


    private List<Rectangle> playerRectangles = new ArrayList<>();

    public Player(String imagePath, KeyHandler handler) {
        super(imagePath, 3);
        this.handler = handler;
        this.xSpeed = 5;
        this.setLocation(GameScreen.getTileSize() * 7, GameScreen.getTileSize() * 14);
        addAllRectangles();
    }



    public void addAllRectangles() {
    Rectangle rect1 = new Rectangle(getX() + 21 , getY(), 3,3);
    playerRectangles.add(rect1);

    }

    @Override
    public void update() {
        if (handler.isLeft()) {
            this.setLocation(this.getX() - this.xSpeed, this.getY());
            for(int i = 0; i < playerRectangles.size(); i++) {
                Rectangle rect = playerRectangles.get(i);
                rect.x -= this.xSpeed;
            }
        }
        if (handler.isRight()) {
            this.setLocation(this.getX() + this.xSpeed, this.getY());
            for(int i = 0; i < playerRectangles.size(); i++) {
                Rectangle rect = playerRectangles.get(i);
                rect.x += this.xSpeed;
            }
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

    public KeyHandler getHandler() {
        return handler;
    }

    public List<Rectangle> getPlayerRectangles() {
        return playerRectangles;
    }

}
