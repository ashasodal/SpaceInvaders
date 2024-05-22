package com.sodal.gui;

import com.sodal.entity.Enemy;
import com.sodal.entity.Entity;
import com.sodal.entity.Player;
import com.sodal.handler.KeyHandler;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel implements Runnable {

    private static int tileSize = Entity.getOriginalTileSize() * 3;
    private boolean isRunning;
    private int FPS = 60;
    private Thread gameLoop;
    private KeyHandler keyHandler = new KeyHandler();

    private Player player;

    private static final int WIDTH = tileSize * 11, HEIGHT = tileSize* 14;

    public GameScreen() {



        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyHandler);

        player = new Player("./res/player/standardSpaceShip.png", keyHandler);
        player.setLocation(tileSize * 5 , tileSize * 12);

      /*  Rectangle rect1 = new Rectangle(0,player.getY(),tileSize, tileSize);
        Rectangle rect2 = new Rectangle(tileSize -1,player.getY(),tileSize, tileSize);

        if(rect1.intersects(rect2)) {
            System.out.println("intersected!!!");
        }*/


        Enemy enemy = new Enemy("./res/alien/alien3.png");
        enemy.setLocation(0, 0);
        Enemy.getEnemyList().add(enemy);
      /*  enemy = new Enemy("./res/alien/alien3.png");
        enemy.setLocation(enemy.getTileSize(),0);
        Enemy.getEnemyList().add(enemy);*/


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
               // System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update();
        if(player.getBullet() != null) {
            player.getBullet().update();
            if(player.getBullet().getY() <= 0) {
                System.out.println("bulletY: " + player.getBullet().getY());
                player.setBullet(null);
            }
        }
      //  enemiesUpdate();
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
        player.render(g2);

        for (int i = 0; i < Enemy.getEnemyList().size(); i++) {
            Enemy.getEnemyList().get(i).render(g2);
        }

        if(player.getBullet() != null) {
            player.getBullet().render(g2);
        }


        g2.setColor(Color.blue);
        int tileSize = GameScreen.tileSize;
        for(int i = 0; i <= 10; i++) {
            if(i == 0 || i == 1) {
                g2.setColor(Color.pink);
                g2.drawRect(i * tileSize, player.getY(), tileSize, tileSize);
                g2.setColor(Color.blue);
                continue;
            }
            g2.drawRect(i * tileSize, player.getY(), tileSize, tileSize);
        }



        g2.setColor(Color.ORANGE);
        g2.fillRect( Enemy.getEnemyList().get(0).getX() + 12, Enemy.getEnemyList().get(0).getY() + 27,24,15 );




        //////////////////////
        g2.dispose();
    }


    public static int getGameWidth() {
        return WIDTH;
    }


}
