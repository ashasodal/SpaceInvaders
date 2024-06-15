package com.sodal.entity;

import com.sodal.gui.GameScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Enemy extends Entity {

    private static int xSpeed;
    private static List<Enemy> enemyList = new ArrayList<>();
    private List<Rectangle> rectangleList = new ArrayList<>();
    private Bullet bullet;
    private static int timer;
    private static List<Bullet> enemyBullets = new ArrayList<>();

    private String imagePath;

    public Enemy(String imagePath) {
        super(imagePath, 3);
        this.imagePath = imagePath;
        xSpeed = 1;
    }

    @Override
    public void update() {
        if (this.getX() == (GameScreen.getGameWidth() - this.getWidth()) + (this.getSCALE() * this.getSCALE()) && xSpeed > 0) {
            xSpeed = -1 * xSpeed;
        }
        if (this.getX() == (-1 * this.getSCALE() * this.getSCALE()) && xSpeed < 0) {
            xSpeed = -1 * xSpeed;
        }
        // updateXPos();
    }

    private void updateXPos() {
        this.setX(this.getX() + xSpeed);
        for (int i = 0; i < this.rectangleList.size(); i++) {
            Rectangle enemyPart = this.rectangleList.get(i);
            enemyPart.x += xSpeed;
        }
    }


    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(this.getBufferedImage(), this.getX(), this.getY(), null);


    }

    public static java.util.List<Enemy> getEnemyList() {
        return enemyList;
    }

    public static int getXSpeed() {
        return xSpeed;
    }

    public void setRectangleList(Rectangle enemyBodyPart) {
        rectangleList.add(enemyBodyPart);
    }

    public List<Rectangle> rectangleList() {
        return rectangleList;
    }


    //32 pixels.
    public void createBullet() {
        bullet = new Bullet("./res/alien/bullet/bullet.png", 2);
        if (imagePath.equals("./res/alien/alien1.png")) {
            bullet.setLocation(getX() + (getTileSize() - bullet.getWidth()) / 2, getY() + getTileSize() - 12);
        }

        else if (imagePath.equals("./res/alien/alien3.png")) {
            bullet.setLocation(getX() + (getTileSize() - bullet.getWidth()) / 2, getY() + getTileSize() - 6);
        }

        bullet.setBulletRect(new Rectangle(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight()));
    }


    private static Enemy getRandomEnemy() {
        Random rand = new Random();
        int randIndex = rand.nextInt(Enemy.getEnemyList().size());
        return Enemy.getEnemyList().get(randIndex);
    }


    public static void enemiesShoot() {
        //make random enemy shoot.
        timer++;
        //create bullet after every second.
        if (timer == 60 && !Enemy.getEnemyList().isEmpty()) {
            Enemy randEnemy = getRandomEnemy();
            randEnemy.createBullet();
            enemyBullets.add(randEnemy.getBullet());
            timer = 0;
        }
        Iterator<Bullet> bulletIterator = enemyBullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            if (bullet.getY() == GameScreen.getHEIGHT()) {
                bulletIterator.remove();
            }
        }
    }


    public static List<Bullet> getEnemyBullets() {
        return enemyBullets;
    }


    public Bullet getBullet() {
        return bullet;
    }


    public static void resetTimer() {
        timer = 0;
    }


    public List<Rectangle> getRectangleList() {
        return rectangleList;
    }
}
