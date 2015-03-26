import java.awt.Color;
import java.awt.Graphics;


public class Queen extends Actor
{
	private int foodCount = 5;

	public Queen(double xPos, double yPos, Faction f)
	{
		xPosition = xPos;
		yPosition = yPos;

		faction = f;

		width = 30;
		height = 30;
		
		spawnDrones();
	}

	public void draw(Graphics g)
	{
		g.setColor(faction.getColor());

		g.fillRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);

		g.setColor(Color.BLACK);
		g.drawRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);
	}

	@Override
	public void act()
	{
		spawnDrones();
	}

	public void spawnDrones()
	{
		if(foodCount > 2)
		{
			Drone newDrone = new Drone(xPosition, yPosition, faction);

			GamePanel.addEntity(newDrone);

			foodCount -= 2;
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
