package com.sodal.entity;

import com.sodal.gui.GameScreen;

import java.awt.*;

public class Button extends Entity {
    public Button(String imagePath, double scale) {
        super(imagePath, scale);
        setX(300);
        setY(500);
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
