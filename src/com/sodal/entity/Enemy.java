package com.sodal.entity;

import com.sodal.gui.GameScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Enemy extends Entity {

    private static int xSpeed;
    private static int ySpeed;
    private static List<Enemy> enemyList = new ArrayList<>();

    public Enemy(int x, int y, String imagePath) {
        super(x, y, imagePath);
       xSpeed = 1;
    }


    /**
     * there are 3 transparent px. to the left and right of images.
     * because we scaled the image by 3, that means we now have SCALE*SCALE transparent px.
     * to the left and right of images.
     */

    @Override
    public void update() {
        if (this.getX() == (GameScreen.getGameWidth() - this.getWidth()) + (this.getSCALE() * this.getSCALE())) {
           xSpeed = -1 * xSpeed;
           ySpeed += 20;
        }
        if (this.getX() == (-1 * this.getSCALE() * this.getSCALE()) && xSpeed < 0) {
            xSpeed = -1 * xSpeed;
            ySpeed += 20;
        }

        System.out.println("enemy xPos: " + this.getX());
        this.setX(this.getX() + xSpeed);
        this.setY(ySpeed);

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


}
