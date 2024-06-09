package com.sodal.handler;

import com.sodal.entity.Button;
import com.sodal.gui.GameScreen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;

public class MouseMotionHandler extends  MouseMotionAdapter {


    private static com.sodal.entity.Button originalButton;

    private static com.sodal.entity.Button darkerButton;

    private static com.sodal.entity.Button button;


   public MouseMotionHandler(com.sodal.entity.Button original, com.sodal.entity.Button darker) {
       originalButton = original;
       darkerButton = darker;
       button = originalButton;
    }

    public void mouseMoved(MouseEvent e) {
        Point point = e.getPoint();
        if(GameScreen.getGameOver() && button != darkerButton && point.x >= button.getX() && point.x <= button.getX() + button.getWidth() && point.y >= button.getY() && point.y <= button.getY() + button.getHeight()) {
            System.out.println("dark button");
            button = darkerButton;
        }
        else if( GameScreen.getGameOver() && button != originalButton && !(point.x >= button.getX() && point.x <= button.getX() + button.getWidth() && point.y >= button.getY() && point.y <= button.getY() + button.getHeight())) {
            button = originalButton;
            System.out.println("original button");
        }
    }


    public static Button getButton() {
       return button;
    }
}
