package com.sodal.handler;

import com.sodal.entity.Player;
import com.sodal.gui.GameScreen;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyHandler extends KeyAdapter {
    private boolean up, down, left, right;


    public KeyHandler() {


    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) left = true;
        if (keyCode == KeyEvent.VK_RIGHT) right = true;
        if (keyCode == KeyEvent.VK_UP) up = true;
        if (keyCode == KeyEvent.VK_DOWN) down = true;
        if (keyCode == KeyEvent.VK_SPACE){
            if(GameScreen.getPlayer().getBullet() == null) {
                GameScreen.getPlayer().createBullet();
            }
        }
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


}
