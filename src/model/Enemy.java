package model;

import java.awt.Graphics2D;
import java.awt.Image;

public class Enemy extends GameObject {

    private boolean facingRight = true;

    private final double speed;

    private int dir = 0;

    private int turnBias = 1;

    public Enemy(double startX, double startY, int size, double speed, Image sprite) {
        super(startX, startY, size, sprite);
        this.speed = speed;
    }

    public void update(double dt, Walkable walkable) {
        if (Math.random() < 0.01) {
            turnBias *= -1;
        }

        for (int attempts = 0; attempts < 4; attempts++) {
            double dx = 0, dy = 0;

            if (dir == 0) dx = speed * dt;
            else if (dir == 1) dy = speed * dt;
            else if (dir == 2) dx = -speed * dt;
            else dy = -speed * dt;

            double nx = x + dx;
            double ny = y + dy;

            if (walkable.isWalkable(nx, ny, size, size)) {
                x = nx;
                y = ny;

                if (dir == 0) facingRight = true;
                if (dir == 2) facingRight = false;
                return;
            }

            if (dx > 0) facingRight = true;
            if (dx < 0) facingRight = false;

            dir = (dir + turnBias + 4) % 4;
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (sprite == null) return;

        if (facingRight) {
            g2.drawImage(sprite, (int) x, (int) y, size, size, null);
        } else {
            g2.drawImage(sprite, (int) (x + size), (int) y, -size, size, null);
        }
    }

    public void push(double dx, double dy, Walkable walkable) {
        double pushDistance = 80;
        double newX = x + dx * pushDistance;
        double newY = y + dy * pushDistance;

        if (walkable.isWalkable(newX, newY, size, size)) {
            x = newX;
            y = newY;
            
            if(dir == 0) {
            	dir = 2;
            }
            else if(dir == 2) {
            	dir = 0;
            }
            else if(dir == 1) {
            	dir = 3;
            }
            else dir = 1;
        }
    }
}
