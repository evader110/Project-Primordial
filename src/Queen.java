import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Queen extends Actor
{
	private static int foodCount = 5;
	private int queenVisionRange = 750;
	private static String foodString;
	private static ArrayList<Faction> factions = GamePanel.getFactions();

	private Food food;

	private Drone enemy;

	private Drone nearestDrone;

	private boolean taskGiven;

	public Queen(double xPos, double yPos, Faction f)
	{
		xPosition = xPos;
		yPosition = yPos;

		faction = f;

		width = 30;
		height = 30;

		visionRange = new SphereOfInfluence(SphereOfInfluence.getDefaultQueenRange(), this);

		this.getFaction().addQueen(this);
	}

	public void control()
	{
		ArrayList<Entity> entitiesSeen = new ArrayList<Entity>(visionRange.getContents(this));
		for(Entity e : entitiesSeen)
		{
//			if(taskGiven)
//				nearestDrone = getNearestDrone();
			if(e instanceof Food)
			{
				//nearestDrone.setTarget(e);
				getNearestDroneFromLocation(e).setTarget(e);
				taskGiven = true;
			}
			else if(e instanceof Drone && e.getFaction() != this.getFaction())
			{
				getNearestDroneFromLocation(e).setTarget(e);
				taskGiven = true;
			}
		}
	}

	public void draw(Graphics g)
	{
		g.setColor(faction.getColor());

		g.fillRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);

		g.setColor(Color.BLACK);
		g.drawRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);
		g.setColor(faction.getColor());
		g.drawOval((int)this.xPosition - 375, (int)this.yPosition - 375, queenVisionRange, queenVisionRange);
		
	}
	
	
	
	public static void drawResources(Graphics g)
	{
		foodString = getFoodCount();
		factions = GamePanel.getFactions();
		GamePanel.getFactions();
		for(int i = 0; i < factions.size(); i++)
		{
			Faction f = factions.get(i);
			
			g.setColor(f.getColor());
			g.fillRect(105, 25 * (i + 1), 25, 25);
			g.setColor(Color.BLACK);
			g.drawRect(105, 25 * (i + 1), 25, 25);
			g.drawString("Resources: " + foodString, 135, 25 * (i + 2) - 5);
			
			
			g.drawLine(5, 25 * (i + 2), 70, 25 * (i + 2));
		}
		//visionRange.updateVision(this, g);
		//g.drawString("" + foodCount, (int)this.xPosition, (int)this.yPosition - 20);
	}

	@Override
	public void act()
	{
		spawnWarriors();
		spawnAssassins();
		spawnDrones();
		control();
		if(GamePanel.getHiveMind())
		{
			control();
		}
	}

	public void spawnDrones()
	{
		if(foodCount > 2 && faction.getDroneCount() < 30)
		{
			Drone newDrone = new Drone(xPosition, yPosition, faction);

			GamePanel.addEntity(newDrone);

			foodCount -= 1;
		}
		else
		{
			if(faction.getDroneCount() < 4)
			{
				Drone newDrone = new Drone(xPosition, yPosition, faction);

				GamePanel.addEntity(newDrone);
			}
		}
	}
	
	public void spawnWarriors()
	{
		if(foodCount > 3 && faction.getWarriorCount() < 20)
		{
			Warrior newWarrior = new Warrior(xPosition, yPosition, faction, 10);

			GamePanel.addEntity(newWarrior);

			foodCount -= 2;
		}
		
		else
		{
			if(faction.getWarriorCount() == 0)
			{
				Warrior newWarrior = new Warrior(xPosition, yPosition, faction, 10 );

				GamePanel.addEntity(newWarrior);
			}
		}
		
	}
	
	public void spawnAssassins()
	{
		if (foodCount >= 10)
		{
			Assassin newAssassin = new Assassin(xPosition, yPosition, faction);

			GamePanel.addEntity(newAssassin);
			
			foodCount -= 9;
			
		}
		
		
		
	}
	
	/*@Override
>>>>>>> Stashed changes
	public boolean canCross(Region r)
	{
		if(faction.getForbiddenRegions().contains(r))
			return false;
		return true;
<<<<<<< Updated upstream
	}

=======
	}*/
	
	public void giveFood()
	{
		foodCount++;
	}
	
	public static String getFoodCount()
	{
		return Integer.toString(foodCount);
	}
	public boolean getTaskGiven()
	{
		return taskGiven;
	}
	public void setTaskGiven(boolean b)
	{
		taskGiven = b;
	}
	public String toString()
	{
		return "Queen";
	}
	public Drone getNearestDrone()
	{
		Entity closestOne = null;
		ArrayList<Entity> entities = new ArrayList<Entity>(GamePanel.getEntities());
		for(Entity e : entities)
		{
				if(closestOne == null)
				{
					if(e instanceof Drone && e.getFaction().equals(this.getFaction()))
						closestOne = e;
				}
				if(e instanceof Drone && e.getFaction().equals(this.getFaction()) && this.getDistanceFrom(e) < this.getDistanceFrom(closestOne))
					closestOne = e;
		}
		return (Drone)closestOne;
	}
	public Drone getNearestDroneFromLocation(Entity source)
	{
		Entity closestOne = null;
		ArrayList<Entity> entities = new ArrayList<Entity>(GamePanel.getEntities());
		for(Entity e : entities)
		{
				if(closestOne == null)
				{
					if(e instanceof Drone && e.getFaction().equals(this.getFaction()))
						closestOne = e;
				}
				if(e instanceof Drone && e.getFaction().equals(this.getFaction()) && source.getDistanceFrom(e) < source.getDistanceFrom(closestOne))
					closestOne = e;
		}
		return (Drone)closestOne;
	}
}
