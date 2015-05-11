import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;

import javax.swing.JLayeredPane;


public class InteriorPanel extends JLayeredPane {
	//Designed to be used on a BasePanel

	boolean draggable = false;

	static int mouseX;
	static int mouseY;

	MouseAdapter mouseAdapter;
	MouseMotionAdapter mouseMotionAdapter;

	public static Rectangle windowRect = AIPlayground.windowRect;
	public static Rectangle defaultBounds = windowRect;

	public InteriorPanel(){
		this(defaultBounds, false);
	}

	public InteriorPanel(Rectangle bounds)
	{
		this(bounds, false);
	}

	public InteriorPanel(boolean draggable){
		this(defaultBounds, draggable);
	}

	public InteriorPanel(Rectangle bounds, boolean draggable)
	{
		super();

		this.setBounds(bounds);
		this.draggable = draggable;

		initMouse();

		this.setOpaque(true);

		this.setFocusable(true);
	}
	
	public void initMouse()
	{
		initMouseAdapter();
		if(draggable)
			initMouseMotionAdapter();
	}

	public void initMouseAdapter()
	{
		mouseAdapter = new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent cursor){
				mouseX = cursor.getX();
				mouseY = cursor.getY();
				BasePanel basePanel = (BasePanel)InteriorPanel.this.getParent();
				basePanel.moveToFront(InteriorPanel.this);
			}
			
			@Override
			public void mouseClicked(MouseEvent cursor){
				Random randy = new Random();
				setBackground(new Color(randy.nextInt(255), randy.nextInt(255), randy.nextInt(255)));
			}
			
			public void mouseReleased(MouseEvent e){}
			public void mouseDragged(MouseEvent e){}
			public void mouseMoved(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
		};

		addMouseListener(mouseAdapter);
	}

	public void initMouseMotionAdapter()
	{
		mouseMotionAdapter = new MouseMotionAdapter(){

			@Override
			public void mouseDragged(MouseEvent cursor){
				cursor.translatePoint(cursor.getComponent().getLocation().x - mouseX, cursor.getComponent().getLocation().y - mouseY);
				setLocation(cursor.getX(), cursor.getY());
			}

			public void mouseMoved(MouseEvent e){}
		};
		
		addMouseMotionListener(mouseMotionAdapter);
	}

	public void setDraggable()
	{	
		draggable = true;
		initMouseMotionAdapter();
	}
}