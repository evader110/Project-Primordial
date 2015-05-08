import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;


public class SphereOfInfluence {
	
	private static final int QUEEN_VISION_DEFAULT = GamePanel.getPanelHeight()/2;
	private static final int DRONE_VISION_DEFAULT = 175;
	
	private int radius;
	private Queen queen;
	private Drone drone;
	private Graphics g;
	
	private List<Entity> entitiesSeen = new ArrayList<Entity>();
	
	public SphereOfInfluence(int radius, Queen queen)
	{
		if(radius == 0)
		{
			this.radius = QUEEN_VISION_DEFAULT;
		}
		else
		{
			this.radius = radius;
		}
		
		this.queen = queen;
	}
	public SphereOfInfluence(int radius, Drone drone)
	{
		this.radius = radius;
		this.drone = drone;
	}
	public void updateVision(Entity entity, Graphics g)
	{
		this.g = g;
		if(entity instanceof Queen)
		{
			g.drawOval((int)entity.getXPosition() - QUEEN_VISION_DEFAULT, 
					   (int)entity.getYPosition() - QUEEN_VISION_DEFAULT, 
				       QUEEN_VISION_DEFAULT*2, QUEEN_VISION_DEFAULT*2);
		}
		else if(entity instanceof Drone)
		{
			g.drawOval((int)entity.xPosition - DRONE_VISION_DEFAULT, 
					   (int)entity.yPosition - DRONE_VISION_DEFAULT, 
					   DRONE_VISION_DEFAULT*2, DRONE_VISION_DEFAULT*2);
		}
	}
	public List<Entity> getContents(Entity source)
	{
		ArrayList<Entity> entities = new ArrayList<Entity>(GamePanel.getEntities());
		for(Entity entity : entities)
		{
			if(entity.getDistanceFrom(source) <= source.getVisionRange())
				entitiesSeen.add(entity);
		}
		return entitiesSeen;	
	}
	
	public static int getRange(Entity entity)
	{
		if(entity instanceof Queen)
			return SphereOfInfluence.getDefaultQueenRange();
		else if(entity instanceof Drone)
			return SphereOfInfluence.getDefaultDroneRange();
		else
			return 0;
	}
	public static int getDefaultQueenRange()
	{
		return QUEEN_VISION_DEFAULT;
	}
	public static int getDefaultDroneRange()
	{
		return DRONE_VISION_DEFAULT;
	}
}
