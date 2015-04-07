import java.awt.Color;
import java.awt.Graphics;


public class Queen extends Actor
{
	private int foodCount = 5;
	private int queenVisionRange = 750;

	public Queen(double xPos, double yPos, Faction f)
	{
		xPosition = xPos;
		yPosition = yPos;

		faction = f;

		width = 30;
		height = 30;
		
		spawnDrones();
	}

	public void control()
	{
		if(TopMenu.hiveMind)
		{
			
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

	@Override
	public void act()
	{
		spawnWarriors();
		spawnDrones();
		control();
	}

	public void spawnDrones()
	{
		if(foodCount > 2)
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
		if(foodCount > 2)
		{
			Warrior newWarrior = new Warrior(xPosition, yPosition, faction, 10);

			GamePanel.addEntity(newWarrior);

			foodCount -= 2;
		}
		
	}
	
	@Override
	public boolean canCross(Region r)
	{
		if(faction.getForbiddenRegions().contains(r))
			return false;
		return true;
	}
	
	public void giveFood()
	{
		foodCount++;
	}
}
