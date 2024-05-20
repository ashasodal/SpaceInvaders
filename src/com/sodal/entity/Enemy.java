package com.sodal.entity;

import com.sodal.gui.GameScreen;

import java.awt.*;

public class Enemy extends Entity {
    public Enemy(int x, int y, String imagePath) {
        super(x, y, imagePath);
        this.setXSpeed(1);
    }


    /**
     * there are 3 transparent px. to the left and right of images.
     * because we scaled the image by 3, that means we now have SCALE*SCALE transparent px.
     * to the left and right of images.
     */
    @Override
    public void update() {
        if (this.getX() == (GameScreen.getGameWidth() - this.getWidth()) + (this.getSCALE() * this.getSCALE())) {
            this.setXSpeed(-1 * this.getXSpeed());
        }
        if (this.getX() == ( -1* this.getSCALE() * this.getSCALE()) && this.getXSpeed() < 0) {
            this.setXSpeed(-1 * this.getXSpeed());
        }

        this.setX(this.getX() + this.getXSpeed());
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(this.getBufferedImage(), this.getX(), this.getY(), null);
    }
}
