package model;

import java.awt.Graphics2D;
import java.awt.Image;

public class Player {
	
	private double x;
	private double y;
	
	private final int size;
	private final double speed;
	private final Image sprite;
	
	private boolean left, right, up, down;
	
	public Player(double startX, double startY, int size, double speed, Image sprite) {
		this.x = startX;
		this.y = startY;
		this.size = size;
		this.speed = speed;
		this.sprite = sprite;
	}
	
	public void update(double dt, Walkable walkable) {
		double dx = 0;
		double dy = 0;
		
		if(left)dx -= speed * dt;
		if(right) dx += speed * dt;
		if(up) dy -= speed * dt;
		if(down) dy += speed * dt;
		
		tryMove(walkable, dx, 0);
		tryMove(walkable, 0, dy);
	}
	
	private void tryMove(Walkable walkable, double dx, double dy) {
		double nx = x + dx;
		double ny = y + dy;
		
		if(walkable.isWalkable(nx,  ny, size, size)) {
			x = nx;
			y = ny;
		}
	}
	
	public void draw(Graphics2D g2) {
		if(sprite != null) {
			g2.drawImage(sprite, (int) x, (int) y, size, size, null);
		} else {
			
		}
	}
	
	public void setLeft(boolean v) {left = v;}
	public void setRight(boolean v) { right = v; }
    public void setUp(boolean v)    { up = v; }
    public void setDown(boolean v)  { down = v; }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getSize() { return size; }

}
