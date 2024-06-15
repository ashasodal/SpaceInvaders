package com.sodal.entity;

import com.sodal.gui.GameScreen;
import java.awt.*;

public class Button extends Entity {

    public Button(String imagePath, double scale) {
        super(imagePath, scale);
        int width = 100, height = 50;
        setX((GameScreen.getGameWidth() - width) / 2);
        setY((GameScreen.getHEIGHT() - height) / 2);
    }

    @Override
    public void render(Graphics2D g2) {
        if (GameScreen.getGameOver()) {
            g2.drawImage(getBufferedImage(), getX(), getY(), null);
        }
    }

    @Override
    public void update() {

    }

}
