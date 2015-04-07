import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * This class is a modified JPanel that runs a game timer and draws the graphics
 */

public class GamePanel extends JPanel implements ActionListener, Runnable
{
	private static final long serialVersionUID = 1L;

	private Graphics g;

	private static int panelWidth;
	private static int panelHeight;

	private static boolean foodSpawn;
	private static boolean isPaused;
	private static boolean greenFaction;

	private static Random random = new Random();

	private static Timer timer;

	private static Controller controller;

	private static ArrayList<Entity> entities = new ArrayList<Entity>(); //Keeps track of all entities in the game
	
	private static ArrayList<Actor> actors = new ArrayList<Actor>();

	private static ArrayList<Faction> factions = new ArrayList<Faction>();
	
	private static ArrayList<Region> regions = new ArrayList<Region>();
	
	public GamePanel(int x, int y)
	{	
		panelWidth = x;
		panelHeight = y;

		setPreferredSize(new Dimension(panelWidth, panelHeight));
		setFocusable(true);

		controller = new Controller();

		addKeyListener(controller);
		addMouseListener(controller);

		setBackground(Color.LIGHT_GRAY);

		g = getGraphics();
		paint(g);
		
		//Maybe instead of messing around with panelWidth/Height, change the actual width and height of the panel lol
		panelWidth = (int)AIPlayground.getAvailableSpace().getWidth();
		panelHeight = (int)AIPlayground.getAvailableSpace().getHeight();
	}

	public void run()	//Starts the game timer. Call this to start the game
	{	
		resetWorld();

		timer = new Timer(10, this);
		timer.setInitialDelay(0);

		timer.start();
	}

	public static void resetWorld()
	{
		clearAll();
		init();
	}

	public static void init()
	{
		initializeActors();
		initializeMap();
	}
	
	//At some point prolly make a subclass of Entity called Actor, to separate shit that does/doesn't act
	public static void initializeActors()
	{
		factions = new ArrayList<Faction>();
		
		Faction redFaction = new Faction("Red", Color.RED);
		Faction blueFaction = new Faction("Blue", Color.BLUE);
		
		factions.add(redFaction);
		factions.add(blueFaction);
		
		Queen redQueen;
		Queen blueQueen;
		Queen greenQueen;

		if(greenFaction)
		{
			Faction greenFaction = new Faction("Green", Color.GREEN);
			factions.add(greenFaction);
			
			redQueen = new Queen(20, 600, redFaction);
			blueQueen = new Queen(panelWidth - 20, 600, blueFaction);
			greenQueen = new Queen(panelWidth / 2, 20, greenFaction);
			addEntity(greenQueen);
		}
		else
		{
			redQueen = new Queen(20, 300, redFaction);
			blueQueen = new Queen(panelWidth - 20, 300, blueFaction);
		}

		addEntity(redQueen);
		addEntity(blueQueen);
	}
	
	public static void initializeMap()
	{
		Faction redFaction = findFaction("Red", factions);
		Faction blueFaction = findFaction("Blue", factions);
		Faction greenFaction = findFaction("Green", factions);
		
		/*
		Region reg = new Region();
		reg.setBounds(new Rectangle(0, 50, 200, 50));
		
		
		Region reg2 = new Region();
		reg2.setBounds(new Rectangle(150, 50, 50, 500));
		
		
		reg.exclude(findFaction("Red", factions));
		reg.exclude(findFaction("Green", factions));
		
		reg2.exclude(findFaction("Red", factions));
		reg2.exclude(findFaction("Green", factions));
		
		addRegion(reg);
		addRegion(reg2);
		*/
	}

	public void actionPerformed(ActionEvent e) //This method is run every time the timer fires
	{   
		this.requestFocus();

		controller.update();
		
		updateActors();
		checkRegions(); //Bounce actors out of places they shouldn't be

		if(!isPaused)
		{
			if(foodSpawn)
			{
				spawnFood();
			}

			for(int i = 0; i < actors.size(); i++)
			{
				Actor actor = actors.get(i);
				actor.act();
			}
		}
		
		//if u want to add polygons together you convert them into Areas

		this.repaint(); //Calls paintComponent()
	}

	public void spawnFood()
	{
		int bufferZone = 25;

		if(random.nextBoolean())
		{
			int xPos = random.nextInt(panelWidth - 2 * bufferZone) + bufferZone;
			int yPos = random.nextInt(panelHeight - 2 * bufferZone) + bufferZone;

			Point potentialFoodPoint = new Point(xPos, yPos);
			
			for(Region r : regions)
			{
				if(r.getPolygon().contains(potentialFoodPoint))
					return;
			}	
			addEntity(new Food(xPos, yPos));
		}
	}
	
	
	
	

	public void checkRegions() //Make sure no actors are going through regions they shouldn't be
	{
		for(Region r : regions)
		{
			for(Actor a: actors)
			{
				Polygon bounds = Region.rectangleToPolygon(a.getBounds());
				if(r.intersects(bounds) && !a.canCross(r))
				{
					a.setVelocity(a.getXVelocity() * -1, a.getYVelocity() * -1);
				}
			}
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		drawRegions(g);
		drawEntities(g);
		drawCounter(g);	
	}

	public static ArrayList<Entity> getEntities()
	{
		return entities;
	}

	public static void addEntity(Entity e)
	{		
		entities.add(e);
	}

	public static void removeEntity(Entity e)
	{
		entities.remove(e);
	}
	
	public static void updateActors()
	{
		actors.clear();
		
		for(Entity e: entities)
		{
			if(e instanceof Actor)
			{
				actors.add((Actor) e);
			}
		}
	}

	public static void addRegion(Region r) //Adds regions to the map
	{
		regions.add(r);
	}
	
	public static void drawRegions(Graphics g)
	{
		for(Region r : regions)
		{
			g.setColor(r.getColor());
			g.fillPolygon(r.getPolygon());
		}
	}
	
	public static void drawEntities(Graphics g)
	{
		for(Entity entity : entities)
		{
			entity.draw(g);
		}
	}
	
	public static void drawCounter(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawLine(5, 25, 70, 25);
		
		for(int i = 0; i < factions.size(); i++)
		{
			Faction f = factions.get(i);
			
			g.setColor(f.getColor());
			g.fillRect(5, 25 * (i + 1), 25, 25);
			g.setColor(Color.BLACK);
			g.drawRect(5, 25 * (i + 1), 25, 25);
			g.drawString("" + f.getDroneCount(), 35, 25 * (i + 2) - 5);
			
			g.drawLine(5, 25 * (i + 2), 70, 25 * (i + 2));
		}
	}
	
	public static Faction findFaction(String name, ArrayList<Faction> facs)
	{
		for(Faction f : facs)
		{
			if(f.getName().equals(name)){
				return f;
			}
		}
		
		System.out.println("Didn't find the faction.");
		return new Faction();
	}
	
	public static void togglePause()
	{
		if(isPaused)
		{
			isPaused = false;
		}
		else
		{
			isPaused = true;
		}
	}

	public static void toggleFoodSpawn()
	{
		if(foodSpawn)
		{
			foodSpawn = false;
		}
		else
		{
			foodSpawn = true;
		}
	}

	public static void toggleGreenFaction()
	{
		if(greenFaction)
		{
			greenFaction = false;
			resetWorld();
		}
		else
		{
			greenFaction = true;
			resetWorld();
		}
	}
	
	public static void clearAll()
	{
		clearRegions();
		clearEntities();
		clearActors();
	}
	
	public static void clearRegions()
	{
		regions.clear();
	}
	
	public static void clearEntities()
	{
		entities.clear();
	}
	
	public static void clearActors()
	{
		actors.clear();
	}
	
	public static int getPanelWidth()
	{
		return panelWidth;
	}

	public static int getPanelHeight()
	{
		return panelHeight;
	}
}
