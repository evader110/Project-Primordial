
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.List;


public class Drone extends Actor
{
	private final int SPEED = 3;
	private final double MAX_HEALTH = 160;
	private final double DAMAGE = 5;
	private final double AGGRO_RANGE = 250;
	private final double FOOD_RANGE = 250;
	private final double SPEED_REDUCTION = 0.80;

	private int turnCounter;
	private int wanderingTimeLeft;

	private double health = MAX_HEALTH;

	private boolean takingOrders;
	private boolean hasFood;
	private boolean isFighting;
	private boolean isGrabbing;
	private boolean isDead;

	private Entity target;

	private Random random = new Random();

	private Drone enemy;

	private Queen queen;

	private List<Polygon> spheresOfInfluence = new ArrayList<Polygon>();

	public Drone(double x, double y, Faction f)
	{
		xPosition = x;
		yPosition = y;

		faction = f;

		width = 12;
		height = 12;

		faction.addDrone(this);
		queen = getQueen();
	}

	public void draw(Graphics g)
	{
		Color outlineColor;

		g.setColor(faction.getColor());

		if(isDead)
		{
			g.setColor(Color.black);
		}

		g.fillRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);

		if(isFighting || isGrabbing)
		{
			outlineColor = Color.white;
		}
		else
		{
			outlineColor = Color.black;
		}

		g.setColor(outlineColor);
		g.drawRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);

		if(hasFood)
		{
			drawFood(g);
		}
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

		if(!isDead)
		{

			if(!GamePanel.getHiveMind())
			{
				decideBehavior();
			}
			else
			{
				takingOrders = true;
				decideBehavior();
			}
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
		this.getQueen();
	}

	public Queen getQueen()
	{
		Queen queen = null;
		ArrayList<Entity> entities = new ArrayList<Entity>(GamePanel.getEntities());
		for(Entity e : entities)
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

	//This needs a complete rework whenever I feel like it
	public void decideBehavior()
	{
		ArrayList<Entity> entities = new ArrayList<Entity>(GamePanel.getEntities());
		//When there is not a Hive Mind
		if(!GamePanel.getHiveMind())
		//Notes sklalaklkl;dfslkafskljdfsakl;adsfkljdfsalkadfskldfsafddklfafjkadfsdfsajkl
		//Well if you want to see if it's in a region it can't go to you need to know what regions there are, and then check what's up
		//But if it worries about itself then you need to handle this in GamePanel
		/*if(isInForbiddenRegion())
		{
			isFighting = false;
			isGrabbing = false;
			wander(random.nextInt(100));
		}
		else*/ if(wanderingTimeLeft > 0)
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
			else if(hasFood && queen != null)
			{
				bringFoodToQueen();
			}
			else
			{
				if(isNearFood())
				{
					goTowardsFood();
				}
				else
				{
					randomMovement(1);
				}
			}
				randomMovement(1);

			wanderingTimeLeft--;
		}
		else if(hasFood && queen != null)
		{
			bringFoodToQueen();
		}
		//When there is a Hive Mind
		else
		{
			if(isNearFood())
			{
				goTowardsFood();
			}
			if(hasFood && queen != null)
			{
				bringFoodToQueen();
				//target = null;
			}
			else
			{
				if(target != null && !entities.contains(target))
				{
					target = null;
				}
				else if(target instanceof Food)
				{
					goTowardsFood();
				}
				else
					moveWithinBounds(SphereOfInfluence.getRange(queen), queen);
			}
		}
	}

	public boolean intersects(Region r)
	{
		return intersects( new ArrayList<Region>(Arrays.asList(r)) );
	}

	public boolean intersects(ArrayList<Region> regions)
	{
		for(Region r : regions)
		{
			Polygon bounds = Region.rectangleToPolygon(this.getBounds());
			if(r.intersects(bounds))
				return true;
		}
		return false;
	}

	/*
	@Override
	public boolean canCross(Region r)
	{
		if(faction.getForbiddenRegions().contains(r))
			return false;
		return true;
	}
=======
	}*/

	public void bringFoodToQueen()
	{
		if(isTouching(this.getQueen()))
		{
			System.out.println("Touching");
			queen.giveFood();
			hasFood = false;
		}
		else
		{
			double angleToTarget = calcAngleTo(queen);

			move(SPEED * Math.cos(angleToTarget), -SPEED * Math.sin(angleToTarget), SPEED_REDUCTION);

			//xVelocity = (SPEED * Math.cos(angleToTarget)) * SPEED_REDUCTION;
			//yVelocity = (-SPEED * Math.sin(angleToTarget)) * SPEED_REDUCTION;
		}
	}

	public boolean isNearEnemy()
	{
		boolean foundEnemy = false;
		ArrayList<Entity> entities = new ArrayList<Entity>(GamePanel.getEntities());
		
		
		for(Entity e : entities)
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

	public boolean isNearFood()
	{
		ArrayList<Entity> entities = new ArrayList<Entity>(GamePanel.getEntities());
		for(Entity e : entities)
		{
			if(e instanceof Food)
			{
				if(getDistanceFrom(e) <= FOOD_RANGE)
				{
					return true;
				}
			}
		}
		return false;
	}

	public void wander(int time)
	{
		isFighting = false;
		isGrabbing = false;

		wanderingTimeLeft = time;
	}

	public void goTowardsFood()
	{
		isGrabbing = true;
		if(!(this.target instanceof Food))
		{
			target = getClosestFood();
		}	
		if(target != null)
		{
			if(isTouching(target))
			{
				((Food) target).reduce();
				hasFood = true;
			}
			else
			{
				moveTowards(target);
			}
		}
		target = null;
	}

	public Entity getClosestFood()
	{
		Entity closestOne = null;
		ArrayList<Entity> entities = new ArrayList<Entity>(GamePanel.getEntities());
		for(Entity e : entities)
		{
				if(closestOne == null)
				{
					if(e instanceof Food)
						closestOne = e;
				}
				if(e instanceof Food && this.getDistanceFrom(e) < this.getDistanceFrom(closestOne))
					closestOne = e;
		}
		return closestOne;
		
		/*
		Entity target = null;

		ArrayList<Entity> foodSources = new ArrayList<Entity>();
		ArrayList<Entity> entities = new ArrayList<Entity>(GamePanel.getEntities());
		for(Entity e : entities)
		{
			if(e instanceof Food)
			{
				foodSources.add(e);
			}
		}

		if(foodSources.size() != 0)
		{
			target = foodSources.get(0);

			for(Entity e : foodSources)
			{
				if(this.getDistanceFrom(e) < this.getDistanceFrom(target))
				{
					target = e;
				}
			}
		}

		return target;
		*/
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

	public void moveWithinBounds(double distanceFrom, Entity source)
	{
		if(getDistanceFrom(source) > distanceFrom)
		{	
			moveTowards(source);
		}
		else
		{
			this.randomMovement(1);
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

	public void getFood()
	{

	}
	public void setTarget(Entity target)
	{
		this.target = target;
	}
	public void destroy()
	{
		GamePanel.removeEntity(this);
		faction.removeDrone(this);
	}

	public boolean isDead()
	{
		return isDead;
	}
	public String toString()
	{
		return "Drone";
	}
}
