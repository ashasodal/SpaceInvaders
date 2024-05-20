package com.sodal.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Entity {

    private int x, y;
    private final int SCALE = 3;

    private int xSpeed, ySpeed;

    private BufferedImage bufferedImage;


    public Entity(int x, int y, String imagePath) {
        this.x = x;
        this.y = y;

        try {
            Image image = ImageIO.read(new File(imagePath));
            bufferedImage = new BufferedImage(image.getWidth(null) * SCALE, image.getHeight(null) * SCALE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
           g2.drawImage(image, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
           g2.dispose();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public abstract void update();
    public abstract void render(Graphics2D g2);


    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public void setYSpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    public int getYSpeed() {
        return ySpeed;
    }

    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getXSpeed() {
        return xSpeed;
    }

    public int getSCALE() {
        return SCALE;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
