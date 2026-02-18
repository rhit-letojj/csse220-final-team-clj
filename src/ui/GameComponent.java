package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import model.Collectible;
import model.Enemy;
import model.GameLogic;
import model.LifePickup;
import model.Map;
import model.Player;

public class GameComponent extends JComponent {

    private final GameLogic model;
    private final Map map;

    private final Player player;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<Collectible> gems;
    private final ArrayList<LifePickup> hearts;

    private final Runnable onWin;
    private final Runnable onGameOver;

    private Image grassTile;
    private Image wallTile;
    private Image wallTileB;
    private Image exitTile;

    private Timer timer;
    private long lastTickNanos;

    private int damageCooldownMs = 0;
    private boolean gameOver = false;
    private boolean win = false;
    
    private Sound sGem = new Sound("gem.wav");
    private Sound sHurt = new Sound("hurt.wav");
    private Sound sPush = new Sound("push.wav");
    private Sound sWin = new Sound("win.wav");
    private Sound sOver = new Sound("gameover.wav");
    private Sound sHeart = new Sound("heart.wav");


    public GameComponent(GameLogic model, Runnable onWin, Runnable onGameOver) throws IOException {
        this.model = model;
        this.map = model.getMap();
        this.player = model.getPlayer();
        this.enemies = new ArrayList<>(model.getEnemies());
        this.gems = new ArrayList<>(model.getGems());
        this.hearts = new ArrayList<>(model.getHearts());
        this.onWin = onWin;
        this.onGameOver = onGameOver;

        setPreferredSize(new Dimension(
                map.getWidth() * Map.TILE_SIZE,
                map.getHeight() * Map.TILE_SIZE
        ));
        setFocusable(true);

        grassTile = ImageIO.read(getClass().getResource("grassTile.png"));
        wallTile = ImageIO.read(getClass().getResource("wallTile.png"));
        wallTileB = ImageIO.read(getClass().getResource("wallTileBottom.png"));
        exitTile = ImageIO.read(getClass().getResource("exitTile.png"));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                setKey(e.getKeyCode(), true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                setKey(e.getKeyCode(), false);
            }
        });

        lastTickNanos = System.nanoTime();
        timer = new Timer(16, e -> update());
        timer.start();
    }

    private void update() {
        if (gameOver || win) return;

        long now = System.nanoTime();
        double dt = (now - lastTickNanos) / 1_000_000_000.0;
        lastTickNanos = now;

        if (damageCooldownMs > 0) damageCooldownMs -= 16;

        player.update(dt, map);

        for (Enemy enemy : enemies) {
            enemy.update(dt, map);
        }

        for (int i = 0; i < gems.size(); i++) {
            if (player.getBounds().intersects(gems.get(i).getBounds())) {
                gems.remove(i);
                sGem.play();
                player.addPoints(100);
                break;
            }
        }
        
        for (int i = 0; i < hearts.size(); i++) {
            if (player.getBounds().intersects(hearts.get(i).getBounds())) {
                hearts.remove(i);
                sHeart.play();
                player.addLife();
                break;
            }
        }

        if (player.isPushing()) {
            for (Enemy enemy : enemies) {
                if (player.pushGetBounds().intersects(enemy.getBounds())) {
                    double dx = player.getPushDX();
                    double dy = player.getPushDY();
                    if (dx != 0 || dy != 0) {
                    		enemy.push(dx, dy, map);
                    		sPush.play();
                    }
                }
            }
        }

        for (Enemy enemy : enemies) {
            if (player.getBounds().intersects(enemy.getBounds())) {
                if (damageCooldownMs <= 0) {
                    player.hurt();
                    damageCooldownMs = 2000;
                    sHurt.play();
                }
            }
        }

        model.syncPlayerStatsFromPlayer();

        if (player.getLives() <= 0) {
            gameOver = true;
            timer.stop();
            sOver.play();
            SwingUtilities.invokeLater(onGameOver);
        }

        if (model.getExit() != null && player.getBounds().intersects(model.getExit())) {
            win = true;
            timer.stop();
            sWin.play();
            SwingUtilities.invokeLater(onWin);
        }

        repaint();
    }

    private void setKey(int keyCode, boolean down) {
        switch (keyCode) {
            case KeyEvent.VK_A -> player.setLeft(down);
            case KeyEvent.VK_D -> player.setRight(down);
            case KeyEvent.VK_W -> player.setUp(down);
            case KeyEvent.VK_S -> player.setDown(down);
            case KeyEvent.VK_SPACE -> player.setPush(down);
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                char t = map.getTile(x, y);
                Image img = switch (t) {
                    case 'w' -> wallTile;
                    case 'b' -> wallTileB;
                    case 'e' -> exitTile;
                    default -> grassTile;
                };

                g2.drawImage(
                        img,
                        x * Map.TILE_SIZE,
                        y * Map.TILE_SIZE,
                        Map.TILE_SIZE,
                        Map.TILE_SIZE,
                        null
                );
            }
        }

        for (Collectible gem : gems) gem.draw(g2);
        for (LifePickup h : hearts) h.draw(g2);
        for (Enemy enemy : enemies) enemy.draw(g2);
        player.draw(g2);

        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(10, 10, 230, 95, 15, 15);

        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));

        g2.setColor(Color.BLACK);
        g2.drawString("Lives: " + player.getLives(), 22, 42);
        g2.drawString("Score: " + player.getScore(), 22, 67);

        g2.setColor(Color.WHITE);
        g2.drawString("Lives: " + player.getLives(), 20, 40);
        g2.drawString("Score: " + player.getScore(), 20, 65);

        g2.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));

        g2.setColor(Color.BLACK);
        g2.drawString("Push: SPACE", 22, 92);

        g2.setColor(Color.WHITE);
        g2.drawString("Push: SPACE", 20, 90);
    }
}
