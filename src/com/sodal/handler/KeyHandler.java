package com.sodal.handler;

import com.sodal.entity.Player;
import com.sodal.gui.GameScreen;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    private boolean up, down, left, right, shoot;


    public KeyHandler() {


    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) left = true;
        if (keyCode == KeyEvent.VK_RIGHT) right = true;
        if (keyCode == KeyEvent.VK_UP) up = true;
        if (keyCode == KeyEvent.VK_DOWN) down = true;
        if (keyCode == KeyEvent.VK_SPACE) shoot = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) left = false;
        if (keyCode == KeyEvent.VK_RIGHT) right = false;
        if (keyCode == KeyEvent.VK_UP) up = false;
        if (keyCode == KeyEvent.VK_DOWN) down = false;
        if (keyCode == KeyEvent.VK_SPACE) shoot = false;

    }

    public void setShoot(boolean shoot) {
        this.shoot = shoot;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isShoot() {
        return shoot;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isUp() {
        return up;
    }
}
