import java.awt.Color;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Arrays;

//Destroy this, its about to get dank in here

//Uses a blacklist to forbid certain factions from entering
public class FactionRegion extends Region
{
	private ArrayList<Faction> knownFactions = new ArrayList<Faction>(); //Factions this region knows about
	
	private ArrayList<Faction> forbiddenFactions = new ArrayList<Faction>(); //Factions that can't enter this region
	
	
	//fix these constructors to take in a fac and allow that fac
	public FactionRegion()
	{
		this(null, null, defaultColor);
	}
	
	public FactionRegion(Area area)
	{
		this(area, new ArrayList<Faction>(), new ArrayList<Faction>(), defaultColor);
	}
	
	public FactionRegion(Faction allowedFac, ArrayList<Faction> knownFacs)
	{
		this(null, new ArrayList<Faction>(), knownFacs, allowedFac.getColor());
	}
	
	public FactionRegion(Area area, Faction allowedFac)
	{
		this(area, new ArrayList<Faction>(), new ArrayList<Faction>(Arrays.asList(allowedFac)), allowedFac.getColor());
	}
	
	public FactionRegion(Area area, ArrayList<Faction> knownFacs)
	{
		this(area, knownFacs, new ArrayList<Faction>(), defaultColor);
	}
	
	public FactionRegion(Area area, ArrayList<Faction> knownFacs, Color color)
	{
		this(area, knownFacs, new ArrayList<Faction>(), color);
	}
	
	public FactionRegion(Area area, ArrayList<Faction> knownFacs, Faction allowedFac, Color color)
	{
		this(area, knownFacs, new ArrayList<Faction>(Arrays.asList(allowedFac)), color);
	}	
	
	public FactionRegion(Area area, ArrayList<Faction> knownFacs, ArrayList<Faction> allowedFacs, Color color)
	{
		super(area, color);	
		this.knownFactions = knownFacs;
		
		this.knownFactions.remove(allowedFacs);
		this.knownFactions.addAll(allowedFacs);
		
		this.setUncrossable();
		this.allowFactions(allowedFacs);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setKnownFacitons(ArrayList<Faction> factions)
	{
		this.knownFactions = factions;
	}
	
	public void addKnownFaction(Faction f)
	{
		knownFactions.add(f);
	}
	
	public void addKnownFactions(ArrayList<Faction> factions)
	{
		if(factions == null || factions.isEmpty())
			return; 

		this.knownFactions.remove(factions);
		this.knownFactions.addAll(factions);
	}
	
	public void removeKnownFaction(Faction f)
	{
		knownFactions.remove(f);
	}
	
	public void removeKnownFactions(ArrayList<Faction> factions)
	{
		knownFactions.remove(factions);
	}
	
	public void allowAll()
	{
		super.allowAll();
		//allowToEnter(this);
	}
	
	public void setUncrossable()
	{
		super.setUncrossable(true);
		this.forbidFactions(knownFactions);
	}
	
	public void allowFaction(Faction f)
	{
		forbiddenFactions.remove(f);
	}
	
	public void allowFactions(ArrayList<Faction> factions)
	{
		forbiddenFactions.remove(factions);
	}
	
	public void forbidFaction(Faction f)
	{
		forbiddenFactions.add(f);
	}

	public void forbidFactions(ArrayList<Faction> factions)
	{
		if(factions == null || factions.isEmpty())
			return; 

		this.forbiddenFactions.remove(factions);
		this.forbiddenFactions.addAll(factions);
	}
		
}
