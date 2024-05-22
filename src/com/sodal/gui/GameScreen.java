package com.sodal.gui;

import com.sodal.entity.Enemy;
import com.sodal.entity.Entity;
import com.sodal.entity.Player;
import com.sodal.handler.KeyHandler;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel implements Runnable {

    private boolean isRunning;
    private int FPS = 60;
    private Thread gameLoop;
    private KeyHandler keyHandler = new KeyHandler();

    private Player player;

    private static final int WIDTH = 500, HEIGHT = 700;

    public GameScreen() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyHandler);

        player = new Player("./res/player/standardSpaceShip.png", keyHandler);
        player.setLocation(300, 500);


        Enemy enemy = new Enemy("./res/alien/alien3.png");
        enemy.setLocation(0, 0);
        Enemy.getEnemyList().add(enemy);
        enemy = new Enemy("./res/alien/alien3.png");
        enemy.setLocation(enemy.getTileSize(),0);
        Enemy.getEnemyList().add(enemy);


        gameLoop = new Thread(this);
        gameLoop.start();
    }

    //game loop.
    @Override
    public void run() {
        isRunning = true;
        double drawInterval = 1000_000_000.0 / FPS; // 0.01666 seconds.
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        //display fps variables.
        long timer = 0;
        int drawCount = 0;

        while (isRunning) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            //displays FPS.
            if (timer >= 1_000_000_000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update();
        enemiesUpdate();
    }


    /*
     * if the speed is positive, the enemy to right must move first.
     * If negative speed, enemy to left must move first.
     */
    private void enemiesUpdate() {
        if (Enemy.getXSpeed() > 0) {
            for (int i = Enemy.getEnemyList().size() - 1; i >= 0; i--) {
                Enemy.getEnemyList().get(i).update();
            }
        } else {
            for (int i = 0; i < Enemy.getEnemyList().size(); i++) {
                Enemy.getEnemyList().get(i).update();
            }
        }
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //painting.
        //////////////////////


        g.setColor(Color.CYAN);
        g.fillRect(0, 0, 9, 9);
        g.fillRect(WIDTH - 9, 0, 9, 9);
        player.render(g2);

        for (int i = 0; i < Enemy.getEnemyList().size(); i++) {
            Enemy.getEnemyList().get(i).render(g2);
        }


        //////////////////////
        g2.dispose();


    }


    public static int getGameWidth() {
        return WIDTH;
    }


}
