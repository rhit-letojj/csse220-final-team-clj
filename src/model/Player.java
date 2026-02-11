package model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

public class Player {
	
	private double x;
	private double y;
	private boolean facingRight = true;
	
	private final int size;
	private final double speed;
	private final Image sprite;
	public int lives=5;
	public int score=0;
	public boolean pushing;
	
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
		
		if (dx > 0) facingRight = true;
		if (dx < 0) facingRight = false;

	}
	
	private void tryMove(Walkable walkable, double dx, double dy) {
		double nx = x + dx;
		double ny = y + dy;
		
		if(walkable.isWalkable(nx,  ny, size, size)) {
			x = nx;
			y = ny;
		}
	}
	
	public void setLives(int lives) { this.lives = lives; }
	public void setScore(int score) { this.score = score; }
	
	public double getPushDX() {
	    if (right) return 1;
	    if (left) return -1;
	    return 0;
	}

	public double getPushDY() {
	    if (down) return 1;
	    if (up) return -1;
	    return 0;
	}
	
	public void draw(Graphics2D g2) {
	    if (sprite == null) return;

	    if (facingRight) {
	        g2.drawImage(sprite, (int)x, (int)y, size, size, null);
	    } else {
	        g2.drawImage(sprite,
	                (int)(x + size), (int)y, -size, size, null);
	    }
	}

	public Rectangle playerGetBounds() {
	    Rectangle p = new Rectangle((int)x,(int)y,size,size);
	    return p;
	}
	public Rectangle pushGetBounds() {
	    Rectangle pb = new Rectangle((int)x,(int)y,size+20,size+20);
	    return pb;
	}
	
	public int hurt() {
		lives--;
		return lives;
	}
	
	public void addPoints(int amount) {
		score += amount;
	}
	
	public void setLeft(boolean v) {left = v;}
	public void setRight(boolean v) { right = v; }
    public void setUp(boolean v)    { up = v; }
    public void setDown(boolean v)  { down = v; }
    public void setPush(boolean pushing)  { this.pushing =pushing; }
   

    public double getX() { return x; }
    public double getY() { return y; }
    public int getSize() { return size; }
    
    public int getLives() {return lives;}
    public int getScore() {return score;}

}

