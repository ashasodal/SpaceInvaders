package com.sodal.entity;

import com.sodal.handler.KeyHandler;

import java.awt.*;

public class Player extends Entity {

    private KeyHandler handler;

    public Player(int x, int y, String imagePath, KeyHandler handler) {
        super(x, y, imagePath);
        this.handler = handler;
        this.setXSpeed(5);
    }


    @Override
    public void update() {

        if (handler.isLeft()) {
            this.setX(this.getX() - this.getXSpeed());
        }
        if (handler.isRight()) {
            this.setX(this.getX() + this.getXSpeed());
        }

    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(this.getBufferedImage(), this.getX(), this.getY(), null);
    }
}
