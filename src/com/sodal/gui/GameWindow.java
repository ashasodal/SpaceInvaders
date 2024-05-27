package com.sodal.gui;

import javax.swing.*;

public class GameWindow {

    private GameScreen gameScreen = new GameScreen();

    public GameWindow() {
        JFrame frame = new JFrame("Space Invaders");
        frame.add(gameScreen);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        gameScreen.requestFocusInWindow();
    }
}
