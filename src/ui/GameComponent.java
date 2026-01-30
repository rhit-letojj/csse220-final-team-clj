package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import model.GameLogic;

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


	public GameComponent(GameLogic model) throws IOException {
	this.model = model;
	this.tileImages = new Image[3];
	try {
        tileImages[0] = ImageIO.read(getClass().getResource("grassTile.png"));
        tileImages[1] = ImageIO.read(getClass().getResource("rockTile.png"));
        tileImages[2] = ImageIO.read(getClass().getResource("exitTile.png"));
    } catch (IOException e) {
        throw new RuntimeException("Failed to load tile images", e);
    }
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
	}
}
