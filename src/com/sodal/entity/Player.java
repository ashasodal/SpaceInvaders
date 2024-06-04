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

   private int lives = 3;


    private List<Rectangle> playerRectangles = new ArrayList<>();

    public Player(String imagePath, KeyHandler handler) {
        super(imagePath, 3);
        this.handler = handler;
        this.xSpeed = 5;
        this.setLocation(GameScreen.getTileSize() * 7, GameScreen.getTileSize() * 14);
        addAllRectangles();
    }


    public void addAllRectangles() {

        int xPixels = 7;
        int yPixels;
        int scale = (int) getSCALE();
        Rectangle rect1 = new Rectangle(getX() + (xPixels * scale), getY(), scale * 2, scale * 2);

        xPixels = 6;
        yPixels = 2;
        Rectangle rect2 = new Rectangle(getX() + (xPixels * scale), getY() + (yPixels * scale), scale * 4, scale);

        xPixels = 5;
        yPixels = 3;
        Rectangle rect3 = new Rectangle(getX() + (xPixels * scale), getY() + (yPixels * scale), scale * 6, scale);

        xPixels = 4;
        yPixels = 4;
        Rectangle rect4 = new Rectangle(getX() + (xPixels * scale), getY() + (yPixels * scale), scale * 8, scale * 3);

        xPixels = 3;
        yPixels = 7;
        Rectangle rect5 = new Rectangle(getX() + (scale * xPixels), getY() + (scale * yPixels), scale * 10, scale);

        xPixels = 2;
        yPixels = 8;
        Rectangle rect6 = new Rectangle(getX() + (scale * xPixels), getY() + (scale * yPixels), scale * 12, scale);

        xPixels = 1;
        yPixels = 9;
        Rectangle rect7 = new Rectangle(getX() + (scale * xPixels), getY() + (scale * yPixels), scale * 14, scale);

        xPixels = 0;
        yPixels = 10;
        Rectangle rect8 = new Rectangle(getX(), getY() + (scale * yPixels), scale * 16, scale * 4);

        xPixels = 1;
        yPixels = 14;
        Rectangle rect9 = new Rectangle(getX() + (scale * xPixels), getY() + (scale * yPixels), scale * 14, scale);
        playerRectangles.add(rect8);

    }

    @Override
    public void update() {
        if (handler.isLeft()) {
            this.setLocation(this.getX() - this.xSpeed, this.getY());
            for (int i = 0; i < playerRectangles.size(); i++) {
                Rectangle rect = playerRectangles.get(i);
                rect.x -= this.xSpeed;
            }
        }
        if (handler.isRight()) {
            this.setLocation(this.getX() + this.xSpeed, this.getY());
            for (int i = 0; i < playerRectangles.size(); i++) {
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

            playSound("./res/player/sound/laser.wav");
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(this.getBufferedImage(), this.getX(), this.getY(), null);
        //health bar
        if (lives == 3) {
            g2.setColor(Color.GREEN);
            g2.fillRect(this.getX(), this.getY() + GameScreen.getTileSize(), GameScreen.getTileSize(), 16);
        } else if (lives == 2) {
            g2.setColor(Color.GREEN);
            g2.fillRect(this.getX(), this.getY() + GameScreen.getTileSize(), 16, 16);
            g2.setColor(Color.GREEN);
            g2.fillRect(this.getX() + 16, this.getY() + GameScreen.getTileSize(), 16, 16);
            g2.setColor(Color.RED);
            g2.fillRect(this.getX() + 16 * 2, this.getY() + GameScreen.getTileSize(), 16, 16);
        } else if (lives == 1) {
            g2.setColor(Color.GREEN);
            g2.fillRect(this.getX(), this.getY() + GameScreen.getTileSize(), 16, 16);
            g2.setColor(Color.RED);
            g2.fillRect(this.getX() + 16, this.getY() + GameScreen.getTileSize(), 16, 16);
            g2.setColor(Color.RED);
            g2.fillRect(this.getX() + 16 * 2, this.getY() + GameScreen.getTileSize(), 16, 16);
        } else if (lives == 0) {
            g2.setColor(Color.RED);
            g2.fillRect(this.getX(), this.getY() + GameScreen.getTileSize(), GameScreen.getTileSize(), 16);
        }

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

    public void setXSpeed(int speed) {
        xSpeed = speed;
    }


    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return lives;
    }

}
