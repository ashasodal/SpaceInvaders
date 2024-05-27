package com.sodal.entity;

import com.sodal.gui.GameScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Enemy extends Entity {

    private static int xSpeed;
    private static List<Enemy> enemyList = new ArrayList<>();
    private List<Rectangle> rectangleList = new ArrayList<>();

    public Enemy(String imagePath) {
        super(imagePath, 3);
        xSpeed = 1;
    }

    @Override
    public void update() {
        if (this.getX() == (GameScreen.getGameWidth() - this.getWidth()) + (this.getSCALE() * this.getSCALE()) && xSpeed > 0) {
            xSpeed = -1 * xSpeed;
           // updateYPos();
        }
        if (this.getX() == (-1 * this.getSCALE() * this.getSCALE()) && xSpeed < 0) {
            xSpeed = -1 * xSpeed;
            //updateYPos();
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

    private void updateYPos() {
        for (int i = 0; i < enemyList.size(); i++) {
            Enemy enemy = enemyList.get(i);
            enemy.setY(enemy.getY() + GameScreen.getTileSize() / 24);
        }
        updateRectangleYPos();
    }

    private void updateRectangleYPos() {
        //move enemy rectangles accordingly.
        for (int i = 0; i < enemyList.size(); i++) {
            Enemy enemy = enemyList.get(i);
            for (int j = 0; j < enemy.rectangleList.size(); j++) {
                Rectangle enemyPart = enemy.rectangleList.get(j);
                enemyPart.y += GameScreen.getTileSize() / 24;
            }
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

}
