
public abstract class Actor extends Entity //They're like entities, but they act
{
	public Actor()
	{
		super();
	}
	
	public abstract void act();
	
	//Override this in actors that have factions, maybe make an abstract that extends this called FactionActors idk
	public boolean canCross(Region r)
	{
		return true;
	}
}
