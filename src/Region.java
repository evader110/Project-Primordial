import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;


public class Region{ //extends Area //A region on the map
	//Uses blacklist. Any faction not known can cross, unless the region is uncrossable

	protected Area border;

	private Color color;

	private static ArrayList<Actor> knownActors; //Actors this Region knows exist, might remove this out I don't see why not
	private static ArrayList<Faction> knownFactions; //Factions this region knows about

	private ArrayList<Actor> forbiddenActors = new ArrayList<Actor>(); //Actors that can't enter this region
	private ArrayList<Faction> forbiddenFactions = new ArrayList<Faction>();

	private boolean uncrossable = false;

	protected static Color defaultColor = new Color(105, 105, 105);

	//If you need constructors other than these just use the no args and then use { set___(), set___(), etc }
	//Master Constructor, used by all other constructors
	public Region(Area area, ArrayList<Faction> allowedFacs, /*type,*/ Color color)
	{
		this.border = area;	
		this.setColor(color);

		//Actors
		//this.knownActors = bleh im deleting this probs

		//Factions
		this.knownFactions = allowedFacs;

		forbidAllFactions();
		allowFaction(allowedFacs);

		//Types
		//sadkfals
	}

	public Region()
	{
		this(null, defaultColor);
	}

	public Region(Area area)
	{
		this(area, defaultColor);
	}

	public Region(Faction allowedFaction)
	{
		this(new Area(), new ArrayList<Faction>(Arrays.asList(allowedFaction)), defaultColor);
	}

	public Region(Area area, Color color)
	{
		this(area, new ArrayList<Faction>(), color);
	}

	public Region(Area area, Faction allowedFaction, Color color)
	{
		this(area, new ArrayList<Faction>(Arrays.asList(allowedFaction)), color);
	}

	//General Methods
	public void updateKnowledge(ArrayList<Faction> factions) //This will have unit types too someday, maybe actors
	{
		setKnownFactions(factions);
	}

	public boolean intersects(Shape shape)

	{
		return doShapesIntersect(this.getBorder(), shape);
	}

	public void add(Region r)
	{
		Area a1 = new Area(this.getBorder());
		Area a2 = new Area(r.getBorder());

		a1.add(a2);

		this.setBorder(a1);
	}

	//public void remove(Region r)

	public Region combine(Region r)
	{
		if(this.getBorder() != null && r.getBorder() != null){
			Area a1 = new Area(this.getBorder());
			Area a2 = new Area(r.getBorder());

			a1.add(a2);

			return new Region(a1, averageColor(this.getColor(), r.getColor()));
		}
		System.out.println("Region combination failed");
		return null;
	}

	//Allow/disallow all
	/*public void allow()
	{
		Check to see what type this is and do the appropriate thing based on that
	}*/

	//public void forbid()

	public void allowAll()
	{
		//Make allowToEnter(Region r) in Actor and Faction
	}

	public void forbidAll()
	{
		this.forbidActor(knownActors);
		this.forbidFaction(knownFactions);
	}

	public boolean allows(Actor a)
	{
		if(forbiddenActors.contains(a))
			return false;
		if(a.faction != null && !a.faction.equals(Entity.defaultFaction))
			if(Region.this.allows(a.faction))
				return false;
		//if(a implements type)
		//blah blah
		return true;
	}

	//Manage specific actors
	public void setKnownActors(ArrayList<Actor> actors)
	{
		this.knownActors = actors;
	}

	public void addKnownActor(Actor a)
	{
		addKnownActor( new ArrayList<Actor>(Arrays.asList(a)) );
	}

	public void addKnownActor(ArrayList<Actor> actors)
	{
		if(actors == null || actors.isEmpty())
			return; 

		this.knownActors.remove(actors);
		this.knownActors.addAll(actors);
	}

	public void removeKnownActor(Actor a)
	{
		removeKnownActor( new ArrayList<Actor>(Arrays.asList(a)) );
	}

	public void removeKnownActor(ArrayList<Actor> actors)
	{
		this.knownActors.remove(actors);
	}

	public void allowActor(Actor a)
	{
		allowActor( new ArrayList<Actor>(Arrays.asList(a)) );
	}

	public void allowActor(ArrayList<Actor> actors)
	{
		forbiddenActors.remove(actors);
		addKnownActor(actors);
	}

	public void forbidActor(Actor a)
	{
		forbidActor( new ArrayList<Actor>(Arrays.asList(a)) );
	}

	public void forbidActor(ArrayList<Actor> actors)
	{
		if(actors == null || actors.isEmpty())
			return;

		this.forbiddenActors.remove(actors);
		this.forbiddenActors.addAll(actors);

		addKnownActor(actors);
	}

	public void forbidAllActors()
	{
		forbidActor(knownActors);
	}


	//Manage factions
	public boolean allows(Faction f)
	{
		if(forbiddenFactions.contains(f))
			return false;
		return true;
	}

	public void setKnownFactions(ArrayList<Faction> factions)
	{
		this.knownFactions = factions;
	}

	public void addKnownFaction(Faction f)
	{
		addKnownFaction( new ArrayList<Faction>(Arrays.asList(f)) );
	}

	public void addKnownFaction(ArrayList<Faction> factions)
	{
		if(factions == null || factions.isEmpty())
			return; 

		this.knownFactions.remove(factions);
		this.knownFactions.addAll(factions);
	}

	public void removeKnownFaction(Faction f)
	{
		removeKnownFaction( new ArrayList<Faction>(Arrays.asList(f)) );
	}

	public void removeKnownFaction(ArrayList<Faction> factions)
	{
		knownFactions.remove(factions);
	}

	public void allowFaction(Faction f)
	{
		allowFaction( new ArrayList<Faction>(Arrays.asList(f)) );
	}

	public void allowFaction(ArrayList<Faction> factions)
	{
		forbiddenFactions.remove(factions);
		addKnownFaction(factions);
	}

	public void allowAllFactions()
	{
		allowFaction(knownFactions);
	}

	public void forbidFaction(Faction f)
	{
		forbidFaction( new ArrayList<Faction>(Arrays.asList(f)) );
	}

	public void forbidFaction(ArrayList<Faction> factions)
	{
		if(factions == null || factions.isEmpty())
			return; 

		this.forbiddenFactions.remove(factions);
		this.forbiddenFactions.addAll(factions);

		addKnownFaction(factions);
	}

	public void forbidAllFactions()
	{
		forbidFaction(knownFactions);
	}


	//Getters/Setters
	public Shape getBorder()
	{
		return this.border;
	}

	public void setBorder(Area area)
	{
		this.border = area;
	}

	public void setBounds(Rectangle rect)
	{
		this.border = rectangleToArea(rect); //new Area(rect);
	}

	public Rectangle getBounds()
	{
		return border.getBounds();
	}

	public Color getColor()
	{
		return this.color;
	}

	public void setColor(Color color)


	{
		this.color = color;
	}
<<<<<<< HEAD
=======

>>>>>>> origin/master
	public String toString()
	{
		return "Region";
	}

	public void setUncrossable(boolean uncrossable) //No matter what, nothing can cross a region set to uncrossable
	{
		this.uncrossable = uncrossable;

		//Fopr this method probably just use this.uncrossable = true;
		//And then add the logic of the uncrossable boolean into actor

		//From FationRegion:
		//super.setUncrossable();
		//this.forbidFactions(knownFactions);
	}

	public boolean isUncrossable()
	{
		return uncrossable;
	}


	//Static
	public static Region combine(ArrayList<Region> regions)
	{
		if(regions.size() == 0)
			System.out.println("Empty list");

		Area a = new Area();

		for(Region r : regions)
		{
			if( !(r.getBorder() instanceof Area) )
				return null;

			Area a1 = (Area)r.getBorder();
			a.add(a1);
		}
		return new Region(a);
	}

	public static Area addAreas(ArrayList<Area> areas)
	{
		if(areas.size() == 0)
			System.out.println("Empty list");

		Area a = new Area();

		for(Area a1 : areas)
		{
			if( !(a1 instanceof Area) )
				return null;

			a.add(a1);
		}

		return a;
	}

	public static Color averageColor(Color c1, Color c2)
	{
		int r = ( c1.getRed() + c2.getRed() )/2;
		int g = ( c1.getGreen() + c2.getGreen() )/2;
		int b = ( c1.getBlue() + c2.getBlue() )/2;

		return new Color(r,g,b);
	}

	public static boolean doShapesIntersect(Shape shape1, Shape shape2)
	{
		Area areaA = new Area(shape1);
		areaA.intersect(new Area(shape2));
		return !areaA.isEmpty();
	}

	//Converts rectangles into polygons in case you want to use a rectangle as a constructor argument
	public static Polygon rectangleToPolygon(Rectangle rect)
	{
		Polygon result = new Polygon();

		result.addPoint(rect.x, rect.y);
		result.addPoint(rect.x + rect.width, rect.y);
		result.addPoint(rect.x + rect.width, rect.y + rect.height);
		result.addPoint(rect.x, rect.y + rect.height);

		return result;
	}

	public static Area rectangleToArea(Rectangle rect)
	{
		return new Area(rect);
	}
}
