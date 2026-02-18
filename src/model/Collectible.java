package model;

import java.awt.Graphics2D;
import java.awt.Image;

public class Collectible extends GameObject {

    public Collectible(double x, double y, int size, Image sprite) {
        super(x, y, size, sprite);
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
    }
}
