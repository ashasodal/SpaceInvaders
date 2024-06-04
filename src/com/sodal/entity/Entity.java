package com.sodal.entity;

import com.sodal.gui.GameScreen;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Entity {

    private int x, y;
    private double scale;
    private static int originalTileSize = 16;

    private volatile BufferedImage bufferedImage;

    private int width;
    private int height;


    public Entity(String imagePath, double scale) {

        this.scale = scale;
        try {
            Image image = ImageIO.read(new File(imagePath));
            width = (int) (image.getWidth(null) * scale);
            height = (int) (image.getHeight(null) * scale);

            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
            g2.drawImage(image, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);
            g2.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void createBufferImage(String imagePath) {

        try {
            Image image = ImageIO.read(new File(imagePath));
            width = (int) (image.getWidth(null) * scale);
            height = (int) (image.getHeight(null) * scale);

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


    public int getWidth() {
        return width;
    }

    public double getSCALE() {
        return scale;
    }


    public int getTileSize() {
        return (int) scale * originalTileSize;
    }


    public void setScale(int scale) {
        this.scale = scale;
    }


    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public static int getOriginalTileSize() {
        return originalTileSize;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


    public void setWidth(int width) {
        this.width = width;
    }


    public static void playSound(String filePath) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath);
                try {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
