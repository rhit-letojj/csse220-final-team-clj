package model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public abstract class GameObject {
    protected double x;
    protected double y;
    protected final int size;
    protected final Image sprite;

    public GameObject(double x, double y, int size, Image sprite) {
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

    public double getX() { return x; }
    public double getY() { return y; }
    public int getSize() { return size; }
}
