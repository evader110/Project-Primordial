import java.awt.Color;
import java.util.ArrayList;


public class Faction
{
	private String name;
	
	private Color color;
	
	private Queen queen;
	
	private ArrayList<Drone> drones = new ArrayList<Drone>();
	private ArrayList<Warrior> warriors = new ArrayList<Warrior>();
	private ArrayList<Region> forbiddenRegions = new ArrayList<Region>();
	private ArrayList<Assassin> assassins = new ArrayList<Assassin>();
	
	public Faction(String name, Color c)
	{
		this.name = name;
		this.color = c;
	}
	
	public Faction()
	{
		this("Default", new Color(248,245,225));
	}
	
	public void addQueen(Queen q)
	{
		queen = q;
	}
	
	public void addDrone(Drone d)
	{
		drones.add(d);
	}
	
	public void addWarrior(Warrior w)
	{
		warriors.add(w);
	}
	
	public void addAssassin(Assassin a)
	{
		assassins.add(a);
	}
	
	public void removeDrone(Drone d)
	{
		drones.remove(d);
	}
	
	public void removeWarrior(Warrior w)
	{
		warriors.remove(w);
	}
	
	public void removeAssassin(Assassin a)
	{
		assassins.add(a);
	}
	
	public void setForbidden(Region r)
	{
		forbiddenRegions.add(r);
	}
	
	public void setForbidden(ArrayList<Region> regions)
	{
		if(regions == null)
			return; 

		this.forbiddenRegions.remove(regions);
		this.forbiddenRegions.addAll(regions);
	}
	
	public ArrayList<Region> getForbiddenRegions()
	{
		return forbiddenRegions;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public int getDroneCount()
	{
		return drones.size();
	}
	
	public int getWarriorCount()
	{
		return warriors.size();
	}
	
	public Queen getQueen()
	{
		return queen;
	}
}
