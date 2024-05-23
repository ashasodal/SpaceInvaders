package com.sodal.entity;

import com.sodal.gui.GameScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Enemy extends Entity {

    private static int xSpeed;
    private static int yPos;
    private static List<Enemy> enemyList = new ArrayList<>();
    private List<Rectangle> rectangleList = new ArrayList<>();

    public Enemy(String imagePath) {
        super(imagePath, 3);
        xSpeed = 1;
    }


    /**
     * there are 3 transparent px. to the left and right of images.
     * because we scaled the image by 3, that means we now have SCALE*SCALE transparent px.
     * to the left and right of images.
     */

    @Override
    public void update() {
        if ( xSpeed > 0 && this.getX() == (GameScreen.getGameWidth() - this.getWidth()) + (this.getSCALE() * this.getSCALE())) {
            xSpeed = -1 * xSpeed;
           // yPos += GameScreen.getTileSize() / 24;
        }
        if (this.getX() == (-1 * this.getSCALE() * this.getSCALE()) && xSpeed < 0) {
            xSpeed = -1 * xSpeed;
           // yPos += GameScreen.getTileSize() / 24;
        }
        this.setLocation(this.getX() + xSpeed,  getY());
        updateAllRectangles();
    }

    private void updateAllRectangles() {
        //move enemy rectangles accordingly.
        for (int i = 0; i < rectangleList.size(); i++) {
            Rectangle enemyPart = rectangleList.get(i);
            enemyPart.setLocation((int) (enemyPart.getX() + xSpeed), getY());
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
