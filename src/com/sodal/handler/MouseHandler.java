package com.sodal.handler;

import com.sodal.entity.Button;
import com.sodal.gui.GameScreen;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler extends MouseAdapter {

    private Button button;

    private GameScreen screen;


    public MouseHandler(GameScreen screen, Button button) {
      this.button = button;
      this.screen = screen;
    }


    public void mousePressed(MouseEvent e) {
        Point point = e.getPoint();
        if(GameScreen.getGameOver() && point.x >= button.getX() && point.x <= button.getX() + button.getWidth() && point.y >= button.getY() && point.y <= button.getY() + button.getHeight()) {
            screen.restartGame();
        }
    }

}
