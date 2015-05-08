
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;


public class Warrior extends Actor
{	
	private final int SPEED = 5;
	private final double MAX_HEALTH = 300;
	private final double DAMAGE = 5;
	private final double AGGRO_RANGE = 600;
	private final double TURN_SPEED = (2 * Math.PI) / 50;

	private final double SPEED_REDUCTION = 0.80;

	private double angle = 0;
	private double targetAngle = 0;

	private int turnCounter;
	private int wanderingTimeLeft;

	private int battalionCount;

	private static String battalion;

	private double health = MAX_HEALTH;
	private boolean hasFood;
	private boolean isFighting;
	private boolean isGrabbing;
	private boolean isDead;

	private Random random = new Random();

	private Drone enemy;
	private Warrior enemyWarrior;

	private Queen queen;

	public Warrior(double x, double y, Faction f, int size)
	{
		xPosition = x;
		yPosition = y;

		faction = f;

		width = 14;
		height = 14;

		battalionCount = size;
		health = MAX_HEALTH * battalionCount;

		faction.addWarrior(this);
	}

	public void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;

		//g2d.rotate(angle, xPosition, yPosition);

		Color outlineColor = Color.BLACK;

		g2d.setColor(faction.getColor());
		battalionCount = (int)health / (int)MAX_HEALTH;


		battalion = Integer.toString(battalionCount);


		int[] xVals = {(int)(xPosition - width / 2), (int)(xPosition + width / 2 + 4), (int)(xPosition - width / 2)};
		int[] yVals = {(int)(yPosition + height / 2), (int)(yPosition), (int)(yPosition - height / 2)};

		if(isDead)
		{
			g2d.setColor(Color.black);
		}
		g2d.fillPolygon(xVals, yVals, 3);
		g.setColor(Color.black);
		//g.fillRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);

		if(isFighting || isGrabbing)
		{
			drawFood(g);
		}

		g2d.setColor(outlineColor);
		g2d.drawPolygon(xVals, yVals, 3);

		if(hasFood)
		{
			drawFood(g2d);
		}
		g2d.setColor(Color.BLACK);

		//g2d.rotate(-angle, xPosition, yPosition);


		g2d.drawString(battalion, (int)xPosition + 6, (int)yPosition);
	}

	public void drawFood(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect((int)xPosition - 2, (int)yPosition - 2, 5, 5);
		g.setColor(Color.BLACK);
		g.drawRect((int)xPosition - 2, (int)yPosition - 2, 5, 5);
	}

	@Override
	public void act()
	{
		isFighting = false;
		isGrabbing = false;

		turnCounter++;
		if (battalionCount == 0)
		{
			isDead = true;
		}
		if(!isDead)
		{
			queen = getQueen();

			decideBehavior();

			bounceOffEdges();
			updatePosition();
		}
		else
		{
			if(turnCounter > 200)
			{
				destroy();
			}
		}

		updateAngle();
	}

	public void updateAngle()
	{
		targetAngle = Math.atan2(yVelocity, xVelocity);

		if(angle < targetAngle)
		{
			angle += TURN_SPEED;
		}
		else if(angle > targetAngle)
		{
			angle -= TURN_SPEED;
		}
	}

	public Queen getQueen()
	{
		Queen queen = null;

		for(Entity e : GamePanel.getEntities())
		{
			if(e instanceof Queen)
			{
				Queen possibleQueen = (Queen)e;

				if(possibleQueen.getFaction().equals(faction))
				{
					queen = possibleQueen;
				}
			}
		}

		return queen;
	}


	public void decideBehavior()
	{
		if(isInForbiddenRegion())
		{
			isFighting = false;
			isGrabbing = false;
			wander(random.nextInt(100));
		}
		else if(wanderingTimeLeft > 0)
		{
			if(hasFood)				
				randomMovement(SPEED_REDUCTION);
			else
				randomMovement(1);

			wanderingTimeLeft--;
		}

		else
		{
			if(isNearEnemyWarrior())
			{
				fightWarrior(enemyWarrior);
			}
			else if(isNearEnemy())
			{
				fight(enemy);
			}
			else if(isNearEnemyWarrior())
			{
				fightWarrior(enemyWarrior);
			}
			else
			{
				randomMovement(1);
			}
		}
	}

	public boolean isInForbiddenRegion()
	{
		for(Region r : faction.getForbiddenRegions())
		{
			Polygon bounds = Region.rectangleToPolygon(this.getBounds());
			if(r.intersects(bounds))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean canCross(Region r)
	{
		if(faction.getForbiddenRegions().contains(r))
			return false;
		return true;
	}

	public boolean isNearEnemyWarrior()
	{
		boolean foundEnemy = false;
		for(Entity e : GamePanel.getEntities())
		{
			if(e instanceof Warrior)
			{
				Warrior d = (Warrior) e;

				if(getDistanceFrom(d) <= AGGRO_RANGE && (!d.getFaction().equals(faction)) && (!d.isDead()))
				{
					if(!foundEnemy)
					{
						foundEnemy = true;
						enemyWarrior = d;
					}
					else
					{
						if(getDistanceFrom(d) < getDistanceFrom(enemyWarrior))
						{
							enemyWarrior = d;
						}
					}
				}
			}
		}
		return foundEnemy;
	}

	public boolean isNearEnemy()
	{
		boolean foundEnemy = false;

		for(Entity e : GamePanel.getEntities())
		{
			if(e instanceof Drone)
			{
				Drone d = (Drone) e;

				if(getDistanceFrom(d) <= AGGRO_RANGE && (!d.getFaction().equals(faction)) && (!d.isDead()))
				{
					if(!foundEnemy)
					{
						foundEnemy = true;
						enemy = d;
					}
					else
					{
						if(getDistanceFrom(d) < getDistanceFrom(enemy))
						{
							enemy = d;
						}
					}
				}
			}

		}
		return foundEnemy;
	}



	public void wander(int time)
	{
		isFighting = false;
		isGrabbing = false;

		wanderingTimeLeft = time;
	}

	public void fight(Drone enemy)
	{
		isFighting = true;
		moveTowards(enemy);

		if(getDistanceFrom(enemy) <= 6)
		{
			double damageModifier = random.nextDouble() * DAMAGE;

			enemy.takeDamage(DAMAGE + damageModifier);
			setVelocity(0, 0);
		}
	}

	public void fightWarrior(Warrior enemyWarrior)
	{
		isFighting = true;
		moveTowards(enemyWarrior);

		if(getDistanceFrom(enemyWarrior) <= 6)
		{
			double damageModifier = random.nextDouble() * DAMAGE;

			enemyWarrior.takeDamage(DAMAGE + damageModifier);
			setVelocity(0, 0);
		}
	}




	public void moveTowards(Entity target)
	{
		double angleToTarget = calcAngleTo(target);

		move(SPEED * Math.cos(angleToTarget), -SPEED * Math.sin(angleToTarget), 1);

		//xVelocity = SPEED * Math.cos(angleToTarget);
		//yVelocity = -SPEED * Math.sin(angleToTarget);
	}

	public double calcAngleTo(Entity target)
	{
		double targetAngleInRadians;

		double xDifference = getXDistanceFrom(target);
		double yDifference = getYDistanceFrom(target);

		if(xDifference < 0)
		{
			targetAngleInRadians = Math.PI + Math.atan(-yDifference / xDifference);
		}
		else if(xDifference > 0)
		{
			targetAngleInRadians = Math.atan(-yDifference / xDifference);
		}
		else
		{
			if(yDifference > 0)
			{
				targetAngleInRadians = Math.PI / 2;
			}
			else
			{
				targetAngleInRadians = -Math.PI / 2;
			}
		}

		return targetAngleInRadians;
	}

	public void randomMovement(double multiplier)
	{
		if(turnCounter >= 40)
		{
			Random random = new Random();
			int randXVel = (random.nextInt(SPEED + SPEED + 1) - SPEED);
			int randYVel = (random.nextInt(SPEED + SPEED + 1) - SPEED);

			move(randXVel, randYVel, multiplier);

			turnCounter = 0;
		}
	}

	public void move(double vX, double vY, double multiplier)
	{
		setVelocity(vX * multiplier, vY * multiplier);
	}

	public void bounceOffEdges()
	{
		int panelWidth = GamePanel.getPanelWidth();
		int panelHeight = GamePanel.getPanelHeight();

		if(xPosition - width/2 <= 0)
		{
			xPosition = width/2;
			xVelocity *= -1;
		}
		else if(xPosition + width/2 >= panelWidth)
		{
			xPosition = panelWidth - width/2;
			xVelocity *= -1;
		}
		if(yPosition - height/2 <= 0)
		{
			yPosition = height/2;
			yVelocity *= -1;
		}
		else if(yPosition + height/2 >= panelHeight)
		{
			yPosition = panelHeight - height/2;
			yVelocity *= -1;
		}	
	}

	public void updatePosition()
	{
		xPosition += xVelocity;
		yPosition += yVelocity;
	}

	public void takeDamage(double d)
	{
		health -= d;

		if(health <= 0)
		{
			isDead = true;
		}
	}

	public void destroy()
	{
		GamePanel.removeEntity(this);
		faction.removeWarrior(this);
	}

	public boolean isDead()
	{
		return isDead;
	}
}
