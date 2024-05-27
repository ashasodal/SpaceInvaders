package com.sodal.gui;

import com.sodal.entity.Enemy;
import com.sodal.entity.Entity;
import com.sodal.entity.Player;
import com.sodal.handler.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class GameScreen extends JPanel implements Runnable {

    private static int tileSize = Entity.getOriginalTileSize() * 3;
    private boolean isRunning;
    private int FPS = 60;
    private Thread gameLoop;
    private KeyHandler keyHandler = new KeyHandler();
    private Player player;
    private static final int WIDTH = tileSize * 14, HEIGHT = tileSize * 16; //  672  x 768

    public GameScreen() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyHandler);

        player = new Player("./res/player/standardSpaceShip.png", keyHandler);
        player.setLocation(tileSize * 5, tileSize * 14);

        addAllEnemies();

        gameLoop = new Thread(this);
        gameLoop.start();
    }

    private void addAllEnemies() {
        addAllEnemy3();
    }


    private void addAllEnemy3() {

        int x = tileSize * 3;
        int y = tileSize * 2;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                Enemy enemy = new Enemy("./res/alien/alien3.png");
                enemy.setLocation(x, y);
                Enemy.getEnemyList().add(enemy);
                //enemy 3 stomach
                Rectangle rect = new Rectangle(enemy.getX() + 12, enemy.getY() + 27, 24, 15);
                enemy.setRectangleList(rect);
                //enemy 3 left hand
                rect = new Rectangle(enemy.getX() + 9, enemy.getY() + 30, 3, 3);
                enemy.setRectangleList(rect);
                //enemy 3 right hand
                rect = new Rectangle(enemy.getX() + 36, enemy.getY() + 30, 3, 3);
                enemy.setRectangleList(rect);
                x += tileSize;
            }
            y += tileSize;
            x = tileSize * 3;
        }
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
        if (player.getBullet() != null) {
            player.getBullet().update();
            if (player.getBullet().getY() <= 0) {
                player.setBullet(null);
                player.getHandler().setShoot(false);
                return;
            }
            //check collision between spaceShip bullet and enemies.
            checkCollision();
        }
        enemiesUpdate();
    }


    private void checkCollision() {
        Iterator<Enemy> enemyIterator = Enemy.getEnemyList().iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            Iterator<Rectangle> rectangleIterator = enemy.rectangleList().iterator();
            while (rectangleIterator.hasNext()) {
                Rectangle enemyBodyPart = rectangleIterator.next();
                if (enemyBodyPart.intersects(player.getBullet().getBulletRect())) {
                    Enemy.getEnemyList().remove(enemy);
                    player.setBullet(null);
                    player.getHandler().setShoot(false);
                    return;
                }
            }
        }
    }

    /*
     * if the speed is positive, the enemy to right most move first.
     * If negative speed, enemy to left most move first.
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
        g.setColor(Color.pink);
        //draw grids.
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 14; j++) {
                g2.drawRect(tileSize * j, tileSize * i, tileSize, tileSize);
            }
        }

        //enemy 3
        for (int i = 0; i < Enemy.getEnemyList().size(); i++) {
            Enemy enemy = Enemy.getEnemyList().get(i);
            enemy.render(g2);
            //enemy 3
            g2.setColor(Color.BLUE);
            for (int j = 0; j < enemy.rectangleList().size(); j++) {
                g2.fillRect((int) enemy.rectangleList().get(j).getX(), (int) enemy.rectangleList().get(j).getY(), (int) enemy.rectangleList().get(j).getWidth(), (int) enemy.rectangleList().get(j).getHeight());
            }
        }
        player.render(g2);
        //////////////////////

        g2.dispose();
    }

    public static int getTileSize() {
        return tileSize;
    }

    public static int getGameWidth() {
        return WIDTH;
    }
}
