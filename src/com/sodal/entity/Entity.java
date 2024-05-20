package com.sodal.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Entity {

    private int x, y;
    private static final int SCALE = 3;
    private static int originalTileSize = 16;



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



    public abstract void render(Graphics2D g2);
    public abstract void update();




    public BufferedImage getBufferedImage() {
        return bufferedImage;
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


    public  static  int getTileSize() {
        return  SCALE * originalTileSize;
    }


    public void setY(int y) {
        this.y = y;
    }

}
