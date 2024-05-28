package com.sodal.gui;

import com.sodal.entity.*;
import com.sodal.handler.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameScreen extends JPanel implements Runnable {

    private static int tileSize = Entity.getOriginalTileSize() * 3;
    private boolean isRunning;
    private int FPS = 60;
    private Thread gameLoop;
    private KeyHandler keyHandler = new KeyHandler();
    private Player player;
    private static final int WIDTH = tileSize * 15, HEIGHT = tileSize * 16; //  720  x 768
    private Explosion[] explosions = new Explosion[5];
    private Background background;

    private int timer;

    private List<Bullet> enemyBullets = new ArrayList<>();

    public GameScreen() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyHandler);

        player = new Player("./res/player/standardSpaceShip.png", keyHandler);
        player.setLocation(tileSize * 7, tileSize * 14);


        background = new Background("./res/background/bg.png", 1);
        addAllEnemies();
        createAllExplosion();


        gameLoop = new Thread(this);
        gameLoop.start();
    }


    private Enemy getRandomEnemy() {
        Random rand = new Random();
        int randIndex = rand.nextInt(Enemy.getEnemyList().size());
        return Enemy.getEnemyList().get(randIndex);
    }


    private void createAllExplosion() {
        explosions[0] = new Explosion("./res/explosion/exp1.png", (1.0 / 80) * tileSize);
        explosions[1] = new Explosion("./res/explosion/exp2.png", (1.0 / 80) * tileSize);
        explosions[2] = new Explosion("./res/explosion/exp3.png", (1.0 / 80) * tileSize);
        explosions[3] = new Explosion("./res/explosion/exp4.png", (1.0 / 80) * tileSize);
        explosions[4] = new Explosion("./res/explosion/exp5.png", (1.0 / 80) * tileSize);
        for (int i = 0; i < explosions.length; i++) {
            explosions[i].setBufferedImage(null);
        }
    }

    private void addAllEnemies() {
        addAllEnemy3();
    }

    private void addAllEnemy3() {

        int x = tileSize * 3 - (tileSize / 2);
        int y = tileSize * 2;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                Enemy enemy = new Enemy("./res/alien/alien3.png");
                enemy.setLocation(x, y);
                Enemy.getEnemyList().add(enemy);

                //enemy 3 head.
                Rectangle rect = new Rectangle(enemy.getX() + 9, enemy.getY(), 30, 24);
                enemy.setRectangleList(rect);

                //enemy 3 neck.
                rect = new Rectangle(enemy.getX() + 15, enemy.getY() + 24, 18, 3);
                enemy.setRectangleList(rect);

                //enemy 3 stomach
                rect = new Rectangle(enemy.getX() + 12, enemy.getY() + 27, 24, 15);
                enemy.setRectangleList(rect);

                //enemy 3 left hand
                rect = new Rectangle(enemy.getX() + 9, enemy.getY() + 30, 3, 3);
                enemy.setRectangleList(rect);

                //enemy 3 right hand
                rect = new Rectangle(enemy.getX() + 36, enemy.getY() + 30, 3, 3);
                enemy.setRectangleList(rect);

                x += tileSize + (tileSize / 2);
            }
            y += tileSize + (tileSize / 2);
            x = tileSize * 3 - (tileSize / 2);
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
        playerShoot();
        enemiesUpdate();
    }


    private void playerShoot() {
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
                    //explosion thread.
                    new Thread(() -> {
                        int imageNum = 1;
                        for (int i = 0; i < explosions.length; i++) {
                            explosions[i].setLocation(enemy.getX(), enemy.getY());
                            explosions[i].createBufferImage("./res/explosion/exp" + imageNum + ".png");
                            imageNum++;
                            sleep(100);
                        }
                        for (int i = 0; i < explosions.length; i++) {
                            explosions[i].setBufferedImage(null);
                        }
                    }).start();
                    return;
                }
            }
        }
    }

    private void sleep(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
       // enemiesShoot();
    }


    private void enemiesShoot() {
        //make random enemy shoot.
        timer++;
        //create bullet after every second.
        if (timer == 60) {
            Enemy randEnemy = getRandomEnemy();
            randEnemy.createBullet();
            enemyBullets.add(randEnemy.getBullet());
            timer = 0;
        }
        Iterator<Bullet> bulletIterator = enemyBullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update();
            if (bullet.getY() == HEIGHT) {
                bulletIterator.remove();
            }
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //painting.
        //////////////////////
        g.setColor(Color.pink);
        background.render(g2);
        for (int i = 0; i < explosions.length; i++) {
            explosions[i].render(g2);
        }
        //draw grids.
       /* for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 15; j++) {
                g2.drawRect(tileSize * j, tileSize * i, tileSize, tileSize);
            }
        }*/
        //enemy 3
        for (int i = 0; i < Enemy.getEnemyList().size(); i++) {
            Enemy enemy = Enemy.getEnemyList().get(i);
            enemy.render(g2);
            //enemy 3
          /*  g2.setColor(Color.BLUE);
            for (int j = 0; j < enemy.rectangleList().size(); j++) {
                g2.fillRect((int) enemy.rectangleList().get(j).getX(), (int) enemy.rectangleList().get(j).getY(), (int) enemy.rectangleList().get(j).getWidth(), (int) enemy.rectangleList().get(j).getHeight());
            }*/
        }
        player.render(g2);

        //enemy bullets
        for (Bullet bullet : enemyBullets) {
            bullet.render(g2);
        }
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
