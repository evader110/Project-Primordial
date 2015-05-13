import java.awt.Point;
import java.util.ArrayList;


public abstract class Actor extends Entity //They're like entities, but they act
{ //If we add unit types we should probably make them interfaces
	//Actually you would basically have to if you wanted to have units that were multiple types
	//Or just make the unit type classes static

	private static ArrayList<Region> knownRegions; //Prolly gonna get rid of this and change drone logic/GamePanel

	public Actor()
	{
		super();
	}

	public abstract void act();

	//Override this in actors that have factions, maybe make an abstract that extends this called FactionActors idk
	//^thats actually valid again
	//Just have a check: If this actor has a faction then check if the faction can cross
	//If not just check the types it implements
	//OR just have the faction check in this and put type checks in the actual class, cuz u know what it implements
	public boolean canCross(Region r)
	{
		if(r.isUncrossable())
			return false;
		if(r.allows(this)) //The way this is set up now is if the region doesn't know about the actor it can't cross
			return true;
		//if this can cross any region(not yet implemented), return true
		return false;
	}

	public boolean canCross(Point p)
	{
		for(Region r : knownRegions)
		{
			if(r.getBounds().contains(p));
			return false;
		}
		return true;
	}

	public boolean canCross(int x, int y)
	{
		return canCross(new Point(x,y));
	}

	public boolean isInForbiddenRegion()
	{
		for(Region r : knownRegions)
			return this.getBounds().intersects(r.getBounds());
		return false;
	}
}
