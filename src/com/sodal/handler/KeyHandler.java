package com.sodal.handler;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    private boolean up, down, left, right;

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) left = true;
        if (keyCode == KeyEvent.VK_RIGHT) right = true;
        if (keyCode == KeyEvent.VK_UP) up = true;
        if (keyCode == KeyEvent.VK_DOWN) down = true;
    }


    @Override
    public void keyReleased(KeyEvent e) {

        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) left = false;
        if (keyCode == KeyEvent.VK_RIGHT) right = false;
        if (keyCode == KeyEvent.VK_UP) up = false;
        if (keyCode == KeyEvent.VK_DOWN) down = false;

    }


    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isUp() {
        return up;
    }
}
