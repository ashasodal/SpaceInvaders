package com.sodal.gui;

import com.sodal.entity.*;
import com.sodal.entity.Button;
import com.sodal.handler.KeyHandler;
import com.sodal.handler.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static com.sodal.entity.Entity.playSound;

public class GameScreen extends JPanel implements Runnable {

    private static int tileSize = Entity.getOriginalTileSize() * 3;
    private boolean isRunning;
    private int FPS = 60;
    private Thread gameLoop;
    private KeyHandler keyHandler = new KeyHandler();
    private Button originalButton = new Button("./res/button/originalButton.png", 1);
    private MouseHandler mouseHandler;
    private Player player;
    private static final int WIDTH = tileSize * 15, HEIGHT = tileSize * 16; //  720  x 768
    private Explosion[] explosions;
    private Background normalBackground;

    private Background background;
    private Background gameOverBackground;

    private int timer;
    private List<Bullet> enemyBullets = new ArrayList<>();

    private Explosion[] enemyBulletExplosion;

    private Explosion[] playerDeadExplosion;


    private static boolean gameOver;


    public GameScreen() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyHandler);
        mouseHandler = new MouseHandler(this, originalButton);
        this.addMouseListener(mouseHandler);

        player = new Player("./res/player/ss.png", keyHandler);

        //Background.
        normalBackground = new Background("./res/background/bg.png", 1);
        gameOverBackground = new Background("./res/background/gameOver.png", 1);
        background = normalBackground;
        addAllEnemies();

        //explosions.
        explosions = createExplosions((1.0 / 80) * tileSize);
        int bulletHeight = 12;
        enemyBulletExplosion = createExplosions((1.0 / 80) * bulletHeight);
        playerDeadExplosion = createExplosions((1.0 / 80) * 3 * tileSize);



        gameLoop = new Thread(this);
        gameLoop.start();

    }



    public  void restartGame() {
       // gameOver = false;
        Enemy.getEnemyList().clear();
        enemyBullets.clear();
        background = normalBackground;
        addAllEnemies();
        player =  new Player("./res/player/ss.png", keyHandler);
        gameOver = false;
    }

    private Enemy getRandomEnemy() {
        Random rand = new Random();
        int randIndex = rand.nextInt(Enemy.getEnemyList().size());
        return Enemy.getEnemyList().get(randIndex);
    }


    private Explosion[] createExplosions(double scale) {
        Explosion[] explosions = new Explosion[5];
        for (int i = 0; i < explosions.length; i++) {
            explosions[i] = new Explosion("./res/explosion/exp" + (i + 1) + ".png", scale);
            explosions[i].setBufferedImage(null);
        }
        return explosions;
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
        if(!gameOver) {
            player.update();
            enemiesUpdate();
            playerShoot();
            enemiesShoot();
            checkCollision();
        }

    }


    private void checkCollision() {
        checkIfEnemyHasBeenHit();
        checkIfPlayerHasBeenHit();
    }

    private void checkIfPlayerHasBeenHit() {
        Iterator<Bullet> bulletIterator = enemyBullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            Rectangle bulletRect = bullet.getBulletRect();
            Iterator<Rectangle> playerRectangles = player.getPlayerRectangles().iterator();
            while (playerRectangles.hasNext()) {
                Rectangle playerRect = playerRectangles.next();
                if (bulletRect.intersects(playerRect)) {
                    player.setLives(player.getLives() - 1);
                    bulletIterator.remove();
                    if (player.getLives() == 0) {
                        createExplosion(player.getX() - tileSize, player.getY() - tileSize, playerDeadExplosion, "./res/explosion/sound/explosion2.wav");
                        gameOver();
                    } else {
                        createExplosion(bullet.getX(), bullet.getY(), enemyBulletExplosion, "./res/explosion/sound/explosion2.wav");
                    }
                }
            }
        }
    }


    private void playerShoot() {
        if (player.getBullet() != null) {
            player.getBullet().update();
            if (player.getBullet().getY() <= 0) {
                player.setBullet(null);
                player.getHandler().setShoot(false);
            }
        }
    }

    private void checkIfEnemyHasBeenHit() {
        if (player.getBullet() != null) {
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
                        createExplosion(enemy.getX(), enemy.getY(), explosions, "./res/explosion/sound/explosion2.wav");

                        if(Enemy.getEnemyList().isEmpty()) {
                            gameOver();
                        }

                        return;
                    }
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
    }


    private void createExplosion(int x, int y, Explosion[] explosion, String filePath) {
        new Thread(() -> {
            playSound(filePath);
            for (int i = 0; i < explosion.length; i++) {
                explosion[i].setLocation(x, y);
                explosion[i].createBufferImage("./res/explosion/exp" + (i + 1) + ".png");
                sleep(100);
            }
            for (int i = 0; i < explosion.length; i++) {
                explosion[i].setBufferedImage(null);
            }
        }).start();
    }

    private void gameOver() {
        background = gameOverBackground;
        player.setXSpeed(0);
        gameOver = true;
    }

    private void enemiesShoot() {
        //make random enemy shoot.
        timer++;
        //create bullet after every second.
        if (timer == 60 && !Enemy.getEnemyList().isEmpty()) {
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

        background.render(g2);
        if(!gameOver) {
            player.render(g2);
        }
        else {
            //draw gameOver button.

        }

        //EXPLOSIONS.
        for (int i = 0; i < explosions.length; i++) {
            explosions[i].render(g2);
        }

        //EXPLOSIONS.
        for (int i = 0; i < playerDeadExplosion.length; i++) {
            playerDeadExplosion[i].render(g2);
        }

        /* g2.setColor(Color.pink);
        //draw grids.
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 15; j++) {
                g2.drawRect(tileSize * j, tileSize * i, tileSize, tileSize);
            }
        }*/

        //enemy 3
        for (int i = 0; i < Enemy.getEnemyList().size(); i++) {
            Enemy enemy = Enemy.getEnemyList().get(i);
            enemy.render(g2);
        }


        //ENEMY BULLETS
        for (Bullet bullet : enemyBullets) {
            bullet.render(g2);
        }



        for (int i = 0; i < enemyBulletExplosion.length; i++) {
            enemyBulletExplosion[i].render(g2);
        }




        originalButton.render(g2);

        //////////////////////
        g2.dispose();
    }

    public static int getTileSize() {
        return tileSize;
    }

    public static int getGameWidth() {
        return WIDTH;
    }


    public static boolean getGameOver() {
        return gameOver;
    }
}
