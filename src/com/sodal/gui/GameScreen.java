package com.sodal.gui;

import com.sodal.entity.*;
import com.sodal.entity.Button;
import com.sodal.handler.KeyHandler;
import com.sodal.handler.MouseHandler;

import javax.swing.*;
import java.awt.*;
import java.util.*;


public class GameScreen extends JPanel implements Runnable {

    //SIZES.
    private static int tileSize = Entity.getOriginalTileSize() * 3;
    private static final int WIDTH = tileSize * 15, HEIGHT = tileSize * 16; //  720  x 768

    //HANDLERS.
    private KeyHandler keyHandler = new KeyHandler();
    private MouseHandler mouseHandler;

    //GAME OVER FLAG.
    private static boolean gameOver;

    //GAME LOOP.
    private boolean isRunning;
    private int FPS = 60;
    private Thread gameLoop;

    //PLAYER
    private static Player player;

    //BACKGROUND.
    private Background background;
    private static Background gameOverText;

    //EXPLOSIONS.
    private Explosion[] enemyDeadExplosion;
    private Explosion[] enemyBulletExplosion;
    private Explosion[] playerDeadExplosion;

    //BUTTONS.
    private Button originalButton;


    private static volatile boolean bulletRendered;

    public GameScreen() {

        setUpGameScreen();

        player = new Player("./res/player/player.png", keyHandler);
        originalButton = new Button("./res/button/originalButton.png", 1);
        mouseHandler = new MouseHandler(this, originalButton);

        //Background.
        createAllBackgrounds();
        createAllExplosions();
        addAllEnemies();

        gameLoop = new Thread(this);
        gameLoop.start();
    }

    private void setUpGameScreen() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(keyHandler);
    }

    private void createAllBackgrounds() {
        background = new Background("./res/background/background.png", 1);
        gameOverText = new Background("./res/background/gameOverText.png", 1);
    }

    private void createAllExplosions() {
        enemyDeadExplosion = createExplosions((1.0 / 80) * tileSize);
        int bulletHeight = 12;
        enemyBulletExplosion = createExplosions((1.0 / 80) * bulletHeight);
        playerDeadExplosion = createExplosions((1.0 / 80) * 3 * tileSize);
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

    public void restartGame() {
        addAllEnemies();
        player = new Player("./res/player/player.png", keyHandler);
        gameOver = false;
        this.removeMouseListener(mouseHandler);
        Enemy.resetTimer();
    }

    private void addAllEnemy3() {

        int x = tileSize * 3 - (tileSize / 2);
        int y = tileSize * 2;

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 1; j++) {
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
        if (!gameOver) {
            playerUpdate();
            //enemiesUpdate();
           // checkCollision();
        }
    }

    private void playerUpdate() {
        player.update();
        playerShoot();
    }

    private void checkCollision() {
        checkIfEnemyHasBeenHit();
        checkIfPlayerHasBeenHit();
    }

    private void checkIfPlayerHasBeenHit() {
        Iterator<Bullet> bulletIterator = Enemy.getEnemyBullets().iterator();
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
                        Entity.playSound("./res/explosion/sound/explosion.wav");
                        createExplosion(player.getX() - tileSize, player.getY() - tileSize, playerDeadExplosion);
                        gameOver();
                        return;
                    } else {
                        Entity.playSound("./res/explosion/sound/explosion.wav");
                        createExplosion(bullet.getX(), bullet.getY(), enemyBulletExplosion);
                        break;
                    }
                }
            }
        }
    }

    private void playerShoot() {
        if ( bulletRendered && player.getBullet() != null) {
            player.getBullet().update();
            if (player.getBullet().getY() <= 0) {
                player.setBullet(null);
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
                        Entity.playSound("./res/explosion/sound/explosion.wav");
                        createExplosion(enemy.getX(), enemy.getY(), enemyDeadExplosion);
                        if (Enemy.getEnemyList().isEmpty()) {
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
        Enemy.enemiesShoot();
    }

    private void createExplosion(int x, int y, Explosion[] explosion) {
        new Thread(() -> {
            for (int i = 0; i < explosion.length; i++) {
                explosion[i].setLocation(x, y);
                explosion[i].createBufferImage("./res/explosion/exp" + (i + 1) + ".png");
                sleep(70);
            }
            for (int i = 0; i < explosion.length; i++) {
                explosion[i].setBufferedImage(null);
            }
        }).start();
    }

    private void gameOver() {
        Enemy.getEnemyList().clear();
        Enemy.getEnemyBullets().clear();
        this.addMouseListener(mouseHandler);
        player.setXSpeed(0);
        gameOver = true;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //painting.
        //////////////////////
        background.render(g2);
        player.render(g2);

        if (player.getBullet() != null) {
            player.getBullet().render(g2);
            GameScreen.setBulletRendered(true);
        }

        //ENEMY BULLETS
        for (Bullet bullet : Enemy.getEnemyBullets()) {
            bullet.render(g2);
        }

        //enemy 3
        for (int i = 0; i < Enemy.getEnemyList().size(); i++) {
            Enemy enemy = Enemy.getEnemyList().get(i);
            enemy.render(g2);
        }

        //EXPLOSIONS (all the explosions have the same length, 5.)
        for (int i = 0; i < 5; i++) {
            enemyDeadExplosion[i].render(g2);
            playerDeadExplosion[i].render(g2);
            enemyBulletExplosion[i].render(g2);
        }

        //RESTART BUTTON.
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

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static Background getGameOverText() {
        return gameOverText;
    }

    public static Player getPlayer() {{
        return player;
    }}

    public static boolean getBulletRendered() {
        return bulletRendered;
    }

    public static void setBulletRendered(boolean reset) {

            bulletRendered = reset;
    }
}
