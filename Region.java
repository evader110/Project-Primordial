import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;


public class Region{ //A region on the map

	private Polygon poly;

	private Color color;

	private ArrayList<Faction> excludedFactions = new ArrayList<Faction>(); //Factions that can't enter this region
	
	private boolean uncrossable; //If this is true, nothing can cross this region

	//If you need constructors other than these just use the no args and then use { set___(), set___(), etc }
	public Region()
	{
		this(null, null, new Color(105, 105, 105));
	}

	public Region(Polygon poly, ArrayList<Faction> excludedFacs, Color color)
	{
		this.poly = poly;
		exclude(excludedFacs);
		this.setColor(color);
	}

	public void exclude(Faction f)
	{
		excludedFactions.add(f);
		
		f.setForbidden(Region.this);
	}

	//Adds all factions in an arraylist to excludedFactions, and adds this region to all factions in the arraylist
	public void exclude(ArrayList<Faction> factions)
	{
		if(factions == null)
			return; 

		this.excludedFactions.remove(factions);
		this.excludedFactions.addAll(factions);
		
		for(Faction f : factions)
		{
			f.setForbidden(Region.this);
		}
	}
	
	public void setUncrossable()
	{
		uncrossable = true;
	}
	
	public void setCrossable()
	{
		uncrossable = false;
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
	
	public boolean intersects(Polygon poly)
	{
		return doPolygonsIntersect(this.getPolygon(), poly);
	}
	
	public static boolean doPolygonsIntersect(Polygon p1, Polygon p2)
    {
        Point p;
        for(int i = 0; i < p2.npoints;i++)
        {
            p = new Point(p2.xpoints[i],p2.ypoints[i]);
            if(p1.contains(p))
                return true;
        }
        for(int i = 0; i < p1.npoints;i++)
        {
            p = new Point(p1.xpoints[i],p1.ypoints[i]);
            if(p2.contains(p))
                return true;
        }
        return false;
    }

	public Polygon getPolygon()
	{
		return this.poly;
	}

	public void setPolygon(Polygon poly)
	{
		this.poly = poly;
	}

	public void setBounds(Rectangle rect)
	{
		this.poly = rectangleToPolygon(rect);
	}

	public Rectangle getBounds()
	{
		return poly.getBounds();
	}

	public Color getColor()
	{
		return this.color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}
}
