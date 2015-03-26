import java.awt.Color;
import java.awt.Graphics;


public class Food extends Entity
{
	private int foodValue = 5;
	
	public Food(double x, double y)
	{
		xPosition = x;
		yPosition = y;

		width = foodValue + 3;
		height = foodValue + 3;
	}

	public void draw(Graphics g)
	{
		g.setColor(Color.BLACK);	
		g.drawRect((int)xPosition - (width / 2), (int)yPosition - (height / 2), width, height);
	}
	
	public void reduce()
	{
		foodValue--;
		width = foodValue + 3;
		height = foodValue + 3;
		
		if(foodValue == 0)
		{
			destroy();
		}
	}
	
	public void destroy()
	{
		GamePanel.removeEntity(this);
	}
}
