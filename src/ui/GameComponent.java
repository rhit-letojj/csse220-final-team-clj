package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;

import model.GameLogic;
import model.Enemy;
import model.Player;

public class GameComponent extends JComponent {

	
	
	private GameLogic model;
	
	int[][] tiles = {
			{1,1,1,1,1,1,1,1,2,1},
			{1,1,1,0,0,0,0,0,0,1},
			{1,1,1,1,0,0,1,1,1,1},
			{1,0,0,0,0,0,1,0,0,1},
			{1,1,1,0,0,0,1,1,0,1},
			{1,0,1,0,0,0,0,0,0,1},
			{1,0,0,0,0,0,0,0,0,1},
			{1,0,0,1,1,1,1,1,1,1},
			{1,0,0,1,0,0,0,0,0,1},
			{1,1,1,1,1,1,1,1,1,1}
	};
	private Image[] tileImages;
	private final int TILE_SIZE = 80;
	
	private Image playerSprite;
	private Image enemySprite;
		
	private Player player;
	private Enemy enemy;
	
	private final Timer timer;
	private long lastTickNanos;


	public GameComponent(GameLogic model) throws IOException {
	this.model = model;
	
	int w = tiles[0].length * TILE_SIZE;
	int h = tiles.length * TILE_SIZE;
	setPreferredSize(new Dimension(w, h));
	setFocusable(true);
	
	this.tileImages = new Image[3];
	try {
        tileImages[0] = ImageIO.read(getClass().getResource("grassTile.png"));
        tileImages[1] = ImageIO.read(getClass().getResource("rockTile.png"));
        tileImages[2] = ImageIO.read(getClass().getResource("exitTile.png"));
        
        playerSprite = ImageIO.read(getClass().getResource("steveSprite.png"));
        enemySprite = ImageIO.read(getClass().getResource("zombieSprite.png"));
        
    } catch (IOException e) {
        throw new RuntimeException("Failed to load tile images", e);
    }
	
	player = new Player(1 * TILE_SIZE, 8 * TILE_SIZE, 70, 100, playerSprite);
	enemy = new Enemy(7 * TILE_SIZE, 6 * TILE_SIZE, 70, 90, enemySprite);
	
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
	timer = new Timer(16, e ->{
		long now = System.nanoTime();
		double dt = (now - lastTickNanos) / 1_000_000_000.0;
		lastTickNanos = now;
		
		player.update(dt, this::isWalkableRect);
		enemy.update(dt,  this::isWalkableRect);
		
		repaint();
	});
	timer.start();
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		requestFocusInWindow();
	}
	
	private void setKey(int keyCode, boolean down) {
		switch(keyCode) {
		case KeyEvent.VK_A  -> player.setLeft(down);
		case KeyEvent.VK_D -> player.setRight(down);
		case KeyEvent.VK_W    -> player.setUp(down);
		case KeyEvent.VK_S  -> player.setDown(down);
		}
	}
	
	private boolean isWalkableRect(double x, double y, int w, int h) {
		if(x < 0 || y < 0) return false;
		if(x + w > getWidth() || y + h > getHeight()) return false;
		
		return isWalkablePoint(x, y)
				&& isWalkablePoint(x + w - 1, y)
				&& isWalkablePoint(x, y + h - 1)
				&& isWalkablePoint(x + w - 1, y + h - 1); 
	}
	
	private boolean isWalkablePoint(double px, double py) {
		int tx = (int) (px / TILE_SIZE);
		int ty = (int) (py / TILE_SIZE);
		
		int tileId = tiles[ty][tx];
		return tileId != 1;
	}
	


	@Override
	protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2 = (Graphics2D) g;
	for (int y = 0; y < tiles.length; y++) {
        for (int x = 0; x < tiles[y].length; x++) {
            int tileId = tiles[y][x];
            Image tileImage = tileImages[tileId];

            int dx = x * TILE_SIZE;
            int dy = y * TILE_SIZE;

            g.drawImage(tileImage, dx, dy, TILE_SIZE, TILE_SIZE, null);
        }
    }
	
	player.draw(g2);
	enemy.draw(g2);
	}
}