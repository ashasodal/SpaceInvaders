package com.sodal.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Entity {

    private int x, y;
    private final int SCALE = 3;

    private int xSpeed;

    private BufferedImage bufferedImage;

    private int width;
    private int height;


    public Entity(int x, int y, String imagePath) {
        this.x = x;
        this.y = y;

        try {
            Image image = ImageIO.read(new File(imagePath));
            width = image.getWidth(null) * SCALE;
            height = image.getHeight(null) * SCALE;
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
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

    public void setXSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getXSpeed() {
        return xSpeed;
    }

    public int getY() {
        return y;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }


    public int getWidth() {
        return width;
    }

    public int getSCALE() {
        return SCALE;
    }

}
