package com.sodal.handler;

import com.sodal.entity.Player;
import com.sodal.gui.GameScreen;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    private boolean  left, right;

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) left = true;
        if (keyCode == KeyEvent.VK_RIGHT) right = true;
        if (keyCode == KeyEvent.VK_SPACE){
            if( GameScreen.getPlayer().getLives() != 0 && GameScreen.getPlayer().getBullet() == null && !GameScreen.getGameOver()) {
                GameScreen.getPlayer().createBullet();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) left = false;
        if (keyCode == KeyEvent.VK_RIGHT) right = false;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }

}
