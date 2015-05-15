import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/*
 * This is a base class for any object within the game (characters, projectiles, terrain, etc)
 * All in-game objects should extend Entity
 * 
 * An entity's behavior is defined in its act() method
 * An entity's appearance is defined in its draw() method
 */


public abstract class Entity
{
	private final int SPEED = 3;
	
	protected double xPosition;
	protected double yPosition;
	
	protected int width;
	protected int height;
	
	protected double centerXPosition;
	protected double centerYPosition;
	
	protected double mass;
	
	protected double xVelocity;
	protected double yVelocity;
	
	protected static Faction defaultFaction = new Faction("Default", Color.WHITE);
	protected Faction faction = defaultFaction;
	
	protected Rectangle bounds;

	protected BufferedImage sprite;
	
	protected static SphereOfInfluence visionRange;
	//A boolean method to say if it's inside region r: isInside(Region r) or something
	
	public void draw(Graphics g)
	{
		
	}
	
	public double getMass()
	{
		return mass;
	}
	
	public Rectangle getBounds()
	{
		bounds = new Rectangle ((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);
		return bounds;
	}
	
	public double getDistanceFrom(Entity e)
	{
		double xDiff = getXDistanceFrom(e);
		double yDiff = getYDistanceFrom(e);
		
		return Math.sqrt((xDiff * xDiff) + (yDiff * yDiff));
	}
	
	public double getXDistanceFrom(Entity e)
	{
		return e.xPosition - this.xPosition;
	}
	
	public double getYDistanceFrom(Entity e)
	{
		return e.yPosition - this.yPosition;
	}
	
	public double getXPosition()
	{
		return xPosition;
	}
	
	public void setXPosition(double x)
	{
		xPosition = x;
	}
	
	public double getYPosition()
	{
		return yPosition;
	}
	
	public void setYPosition(double y)
	{
		yPosition = y;
	}
	
	public double getWidth()
	{
		return width;
	}

	public double getHeight()
	{
		return height;
	}
	
	public double getVelocity()
	{
		return Math.sqrt(Math.pow(xVelocity, 2) + Math.pow(yVelocity, 2));
	}
	
	public void setVelocity(double vX, double vY)
	{
		setXVelocity(vX);
		setYVelocity(vY);
	}
	
	public double getXVelocity()
	{
		return xVelocity;
	}
	
	public void setXVelocity(double vX)
	{
		xVelocity = vX;
	}
	
	public double getYVelocity()
	{
		return yVelocity;
	}

	public void setYVelocity(double vY)
	{
		yVelocity = vY;
	}
	
	public Faction getFaction()
	{
		return faction;
	}

	public void setFaction(Faction f)
	{
		faction = f;
	}
	
	public boolean isTouching(Entity e)
	{
		return this.getBounds().intersects(e.getBounds());
	}
	public int getVisionRange()
	{
		return SphereOfInfluence.getRange(this);
	}
	public void move(double vX, double vY, double multiplier)
	{
		setVelocity(vX * multiplier, vY * multiplier);
	}
	public void moveTowards(Entity target)
	{
		double angleToTarget = calcAngleToEntity(target);

		move(SPEED * Math.cos(angleToTarget), -SPEED * Math.sin(angleToTarget), 1);

		//xVelocity = SPEED * Math.cos(angleToTarget);
		//yVelocity = -SPEED * Math.sin(angleToTarget);
	}
	public void moveToPoint(int x, int y)
	{
		double angleToTarget = calcAngleToPoint(x, y);

		move(SPEED * Math.cos(angleToTarget), -SPEED * Math.sin(angleToTarget), 1);
	}
	public double calcAngleToPoint(int x, int y)
	{
		double xDifference = x - this.getXPosition();
		double yDifference = y - this.getYPosition();
		
		return Math.atan2(yDifference, xDifference);
	}
	public double calcAngleToEntity(Entity target)
	{
		double xDifference = getXDistanceFrom(target);
		double yDifference = getYDistanceFrom(target);
		return Math.atan2(yDifference, xDifference);
	}
	
}
