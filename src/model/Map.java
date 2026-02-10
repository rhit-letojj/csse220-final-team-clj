package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map implements Walkable {

    private char[][] tiles;
    private int width;
    private int height;
    public static final int TILE_SIZE = 80;

    public Map(String filename) throws FileNotFoundException {
        List<String> lines = new ArrayList<>();

        // Directly use File
        Scanner sc = new Scanner(new File(filename));

        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }
        sc.close();

        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Level file is empty: " + filename);
        }

        height = lines.size();
        width = lines.get(0).length();
        tiles = new char[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = lines.get(y).charAt(x);
            }
        }
    }

    public char getTile(int x, int y) {
        return tiles[y][x];
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    @Override
    public boolean isWalkable(double x, double y, int w, int h) {
        // stay in bounds
        if (x < 0 || y < 0) return false;
        if (x + w > width * TILE_SIZE) return false;
        if (y + h > height * TILE_SIZE) return false;

        return isWalkablePoint(x, y)
            && isWalkablePoint(x + w - 1, y)
            && isWalkablePoint(x, y + h - 1)
            && isWalkablePoint(x + w - 1, y + h - 1);
    }

    private boolean isWalkablePoint(double px, double py) {
        int tx = (int)(px / TILE_SIZE);
        int ty = (int)(py / TILE_SIZE);

        char t = tiles[ty][tx];

        // treat spaces as grass if your file has them
        if (t == ' ') t = '0';

        return t != 'w' && t != 'b';
    }

}
