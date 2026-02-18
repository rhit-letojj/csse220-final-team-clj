package model;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GameLogic {

    private final Image playerSprite;
    private final Image enemySprite;
    private final Image gemSprite;
    private final Image heartSprite;


    private Map map;
    private Player player;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Collectible> gems = new ArrayList<>();
    private List<LifePickup> hearts = new ArrayList<>();
    private Rectangle exit;

    private int lives = 3;
    private int score = 0;

    public GameLogic(Image playerSprite, Image enemySprite, Image gemSprite, Image heartSprite) {
        this.playerSprite = playerSprite;
        this.enemySprite = enemySprite;
        this.gemSprite = gemSprite;
        this.heartSprite = heartSprite;
    }

    public void restartGame() throws FileNotFoundException {
        lives = 5;
        score = 0;
        loadLevel("level1.txt");
    }

    public void loadLevel(String filename) throws FileNotFoundException {
        map = new Map(filename);
        enemies = new ArrayList<>();
        gems = new ArrayList<>();
        exit = null;
        hearts = new ArrayList<>();

        Player newPlayer = null;

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                char tile = map.getTile(x, y);
                int dx = x * Map.TILE_SIZE;
                int dy = y * Map.TILE_SIZE;

                if (tile == 'p') newPlayer = new Player(dx, dy, 60, 120, playerSprite);
                if (tile == 'z') enemies.add(new Enemy(dx, dy, 60, 100, enemySprite));
                if (tile == 'g') gems.add(new Collectible(dx+20, dy+20, 40, gemSprite));
                if (tile == 'e') exit = new Rectangle(dx, dy, Map.TILE_SIZE, Map.TILE_SIZE);
                if (tile == 'h') hearts.add(new LifePickup(dx+20, dy+20, 40, heartSprite));
            }
        }

        player = newPlayer;
        player.setLives(lives);
        player.setScore(score);
    }

    public void syncPlayerStatsFromPlayer() {
        lives = player.getLives();
        score = player.getScore();
    }

    public Map getMap() { return map; }
    public Player getPlayer() { return player; }
    public List<Enemy> getEnemies() { return enemies; }
    public List<Collectible> getGems() { return gems; }
    public Rectangle getExit() { return exit; }
    public List<LifePickup> getHearts() { return hearts; }
}
