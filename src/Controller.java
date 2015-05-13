import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*
 * 	This class is used to listen for input from the keyboard and react accordingly
 */

public class Controller implements KeyListener, MouseListener
{
	boolean[] keys = new boolean[255];

	//	-Assign ASCII key codes to variables to make the code more readable

	final int W = 87;
	final int A = 65;
	final int S = 83;
	final int D = 68;

	final int ENTER = 10;

	private String entityType = "Food";

	public void keyPressed(KeyEvent e) 
	{
		keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) 
	{
		keys[e.getKeyCode()] = false;
	}

	public void keyTyped(KeyEvent e) {}


	public void mouseClicked(MouseEvent event)
	{
		if(entityType.equals("Food"))
		{
			GamePanel.addEntity(new Food(event.getX(), event.getY()));
		}
		else if(entityType.equals("Drone"))
		{
			GamePanel.addEntity(new Drone(event.getX(), event.getY(), new Faction("Red", Color.RED)));
		}
		else if(entityType.equals("Queen"))
		{
			GamePanel.addEntity(new Queen(event.getX(), event.getY(), new Faction("Red", Color.RED)));
		}
	}

	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}

	public void update() //This function is called from GamePanel's actionPerformed() method
	{	
		if(keys[ENTER])
		{
			GamePanel.resetWorld();
		}
	}
	
	public void setEntityType(String s)
	{
		entityType = s;
	}
}