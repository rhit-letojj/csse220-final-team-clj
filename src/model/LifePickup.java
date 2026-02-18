package model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class LifePickup {
    private double x;
    private double y;
    private final int size;
    private final Image sprite;

    public LifePickup(double x, double y, int size, Image sprite) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.sprite = sprite;
    }

    public void draw(Graphics2D g2) {
        if (sprite != null) {
            g2.drawImage(sprite, (int) x, (int) y, size, size, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, size, size);
    }
}
