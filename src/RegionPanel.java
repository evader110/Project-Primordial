import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;


public class RegionPanel extends InteriorPanel{
	//Draggable and smaller by default

	public static MigLayout layout = new MigLayout(
			"fillx", // Layout Constraints
			"[][]push[]push[]", // [][][][]Column constraints
			"");

	public static Color steelBlue = new Color(62, 181, 205);
	public Color backgroundColor = steelBlue;

	public static Rectangle defaultBounds = new Rectangle(200, 200, 300, 280);


	//Later we will have images of a dank font instead of actual jlabels
	//Text related stuff

	public static Point point1;
	public static Point point2;
	public static Point point3;
	public static Point point4;
	//If you add a point increase pointNumber
	public static int pointCount = 4;

	public static Point highestPoint;
	public static Point lowestPoint;
	public static Point leftmostPoint;
	public static Point rightmostPoint;

	public static Point[] points;

	public static ArrayList<JTextField> fields;
	public static ArrayList<JTextField> xFields;
	public static ArrayList<JTextField> yFields;

	public static JTextField massField;
	public static JTextField vxField;
	public static JTextField vyField;

	public static JTextField x1Txt;
	public static JTextField y1Txt;

	public static JTextField x2Txt;
	public static JTextField y2Txt;

	public static JTextField x3Txt;
	public static JTextField y3Txt;

	public static JTextField x4Txt;
	public static JTextField y4Txt;

	public static JButton createButton;
	public static JButton cancelButton;

	public RegionPanel(){
		this(defaultBounds, true);
	}
	public RegionPanel(Rectangle bounds)
	{
		this(bounds, true);
	}

	public RegionPanel(boolean draggable){
		this(defaultBounds, draggable);
	}

	public RegionPanel(Rectangle bounds, boolean draggable)
	{
		super(bounds, draggable);
		this.setBackground(backgroundColor);

		this.setLayout(layout);
		init();

		this.setOpaque(true);
		this.setFocusable(true);
	}

	public void init()
	{
		initTextFields();
		initButtons();
		addComponents();
	}

	public void addComponents()
	{
		//Remember pushx only takes place when you're actively resizing the container

		add(new JLabel("Mass:"), "gapleft 10");
		add(massField, "wrap, pushx, growx, spanx, gaptop 10, gapright 10");

		add(new JLabel("Velocity X:"), "gapleft 10");
		add(vxField, "wrap, pushx, growx, spanx, gapright 10");

		add(new JLabel("Velocity Y:"), "gapleft 10");
		add(vyField, "wrap, pushx, growx, spanx, gapright 10");

		for(JTextField xField: xFields)
		{	
			int index = xFields.indexOf(xField);
			JTextField yField = yFields.get(index);

			add(new JLabel("Point" + (index+1) + ":"), "gapleft 10");
			add(xField, "width 40%");
			add(new JLabel(","));
			add(yField, "wrap, growx, width 40%, gapright 10");
		}

		//deal with these two later im tired
		add(cancelButton, "dock south, gapy 5");
		add(createButton, "dock south");
	}

	public void initButtons()
	{
		createButton = new JButton("Create");
		createButton.setBorder(BorderFactory.createEmptyBorder());

		createButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				createEntity();
			}
		});

		cancelButton = new JButton("Cancel");
		cancelButton.setBorder(BorderFactory.createEmptyBorder());

		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				RegionPanel.this.selfDestruct();
			}
		});
	}

	public void initTextFields()
	{
		fields = new ArrayList<JTextField>();
		xFields = new ArrayList<JTextField>();
		yFields = new ArrayList<JTextField>();

		int fieldSize = 5;

		massField = new JTextField("10", fieldSize);
		fields.add(massField);

		vxField = new JTextField("0", fieldSize);
		fields.add(vxField);

		vyField = new JTextField("0", fieldSize);
		fields.add(vyField);

		x1Txt = new JTextField("550", fieldSize);
		y1Txt = new JTextField("200", fieldSize);
		xFields.add(x1Txt);
		yFields.add(y1Txt);

		x2Txt = new JTextField("650", fieldSize);
		y2Txt = new JTextField("200", fieldSize);
		xFields.add(x2Txt);
		yFields.add(y2Txt);

		x3Txt = new JTextField("650", fieldSize);
		y3Txt = new JTextField("300", fieldSize);
		xFields.add(x3Txt);
		yFields.add(y3Txt);

		x4Txt = new JTextField("550", fieldSize);
		y4Txt = new JTextField("300", fieldSize);
		xFields.add(x4Txt);
		yFields.add(y4Txt);

		fields.addAll(xFields);
		fields.addAll(yFields);

		for(JTextField field : fields)
		{	
			field.setHorizontalAlignment(JTextField.CENTER);
			field.setEditable(true);
			field.setFocusable(true);
			field.setEnabled(true);
			field.requestFocusInWindow();

			//Maybe requestFocus()?? also look into DocumentListener
		}
	}

	public static void initPoints()
	{
		points = new Point[pointCount];

		//If there's some sort of problem with abstract classes/methods or whatever, just change entity's type from DynamicEntity to Entity, that might work

		//Create the four points
		for(int index = 0; index < points.length; index++)
		{
			JTextField xField = xFields.get(index);
			JTextField yField = yFields.get(index);

			int x = Integer.parseInt(xField.getText());
			int y = Integer.parseInt(yField.getText());

			Point p = new Point(x, y);

			points[index] = p;
		}

		//remember to add extra if there are more points
		point1 = points[0];
		point2 = points[1];
		point3 = points[2];
		point4 = points[3];

		System.out.println("p1:" + point1);
		System.out.println("p2:" + point2);
		System.out.println("p3:" + point3);
		System.out.println("p4:" + point4);

		//Use point1 as a placeholder
		highestPoint = point1;
		lowestPoint = point1;
		leftmostPoint = point1;
		rightmostPoint = point1;

		//Figure out which points are the highest/lowest/whatever
		for(Point point : points)
		{
			if(point.getX() < leftmostPoint.getX())
				leftmostPoint = point;

			if(point.getX() > rightmostPoint.getX())
				rightmostPoint = point;

			if(point.getY() < highestPoint.getY())
				highestPoint = point;

			if(point.getY() > lowestPoint.getY())
				lowestPoint = point;
		}
	}

	public static void createEntity()
	{
		initPoints();

		//Width and Height
		double w = rightmostPoint.getX() - leftmostPoint.getX();
		double h = lowestPoint.getY() - highestPoint.getY();

		//Find the center by averaging left and right, top and down
		int cx = (int)(   leftmostPoint.getX() + rightmostPoint.getX()   ) / 2;
		int cy = (int)(   highestPoint.getY() + lowestPoint.getY()   ) / 2;
		Point centerPoint = new Point(cx, cy);

		//Use the input in the fields to get mass and velocity
		double mass = Double.parseDouble(massField.getText());
		double vx = Double.parseDouble(vxField.getText());
		double vy = Double.parseDouble(vyField.getText());

		//Make the Region
		/*
		Entity entity = new DynamicEntity(){
			@Override
			public void initVertices(){
				vertices = new GamePoint[] {new GamePoint(point1, centerPoint), new GamePoint(point2, centerPoint), new GamePoint(point3, centerPoint), new GamePoint(point4, centerPoint)};
				setCenter(centerPoint);
			}
		};*/

		System.out.println("Center Point:" + centerPoint);
	}

	public void selfDestruct()
	{
		BasePanel basePanel = (BasePanel)this.getParent();
		basePanel.remove(this);
	}

	public void initMouseAdapter()
	{
		mouseAdapter = new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent cursor){
				mouseX = cursor.getX();
				mouseY = cursor.getY();
				BasePanel basePanel = (BasePanel)RegionPanel.this.getParent();
				basePanel.moveToFront(RegionPanel.this);
			}

			public void mouseClicked(MouseEvent cursor){}
			public void mouseReleased(MouseEvent e){}
			public void mouseDragged(MouseEvent e){}
			public void mouseMoved(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
		};

		addMouseListener(mouseAdapter);
	}

	public static void main(String[] args)
	{
		int windowWidth = 600;
		int windowHeight = 600;
		
		final JFrame frame = new JFrame("frametitle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setMinimumSize(new Dimension(windowWidth, windowHeight));
		frame.setSize(new Dimension(windowWidth, windowHeight));
		frame.setLayout(null);

		final BasePanel basePanel = new BasePanel();
		frame.add(basePanel);

		class GameLabel extends JLabel
		{
			public GameLabel(String label, Rectangle bounds)
			{
				super(label);
				this.setBounds(bounds);
			}
		}

		JPanel backPanel = new JPanel();//same as gamePanel
		backPanel.setBounds(0,0, windowWidth, windowHeight);
		backPanel.setBackground(Color.BLUE);
		basePanel.add(backPanel, 0);

		JButton addButton = new JButton("Add");
		addButton.setBounds(10, 10, 100, 50);
		addButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				RegionPanel panel = new RegionPanel(new Rectangle(150,150,200,200), true);
				panel.setBackground(Color.WHITE);

				Random randy = new Random();
				int regionPanelLayer = 1;
				int randLayer = randy.nextInt(10);

				//panel.add(new GameLabel(Integer.toString(randLayer), new Rectangle(50,50,10,10)));

				basePanel.add(panel, regionPanelLayer);

				frame.repaint();
			}
		});

		JButton clearButton = new JButton("Clear");
		clearButton.setBounds(120, 10, 100, 50);
		clearButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				basePanel.clearAll();
			}
		});

		//maybe have the random layer shit
		//g.drawString(Integer.toString(avg), 75, 75);

		basePanel.add(addButton, 1);
		basePanel.add(clearButton, 1);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}

//DO NOT DELETE this is how you add a listener to BasePanel to handle dragging, but it doesn't work and I don't wanna fix it
//The problem was that I was using fucking mouseadapter and not mousemotionadapter for mouseDragged(), it was so easy the whole time FUUUCK

/*OG VERSION
 public void initMouseAdapter()
	{
		mouseAdapter = new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent cursor)
			{
				selected = null;

				mouseX = cursor.getX();
				mouseY = cursor.getY();

				Component c =  BasePanel.this.findComponentAt(mouseX, mouseY);

				if(!(c instanceof InteriorPanel)) return;

				selected = (InteriorPanel)c;
				System.out.println("from main listener: " + selected.getWidth());
				moveToFront(selected);

				BasePanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}

			@Override
			public void mouseDragged(MouseEvent cursor)
			{
				if (selected == null) return;

				cursor.translatePoint(cursor.getComponent().getLocation().x - mouseX, cursor.getComponent().getLocation().y - mouseY);
				selected.setLocation(cursor.getX(), cursor.getY());
			}

			public void mouseReleased(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
			public void mouseMoved(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		};
	}
 */


/*
public void initMouseAdapter()
{
	mouseAdapter = new MouseAdapter(){
		//Pick up
		@Override
		public void mousePressed(MouseEvent e)
	    {
	        selected = null;
	        Component c =  cont.findComponentAt(e.getX(), e.getY());

	        if(!(c instanceof InteriorPanel)) return;

	        Point parentLocation = c.getParent().getLocation();
	        xAdjustment = parentLocation.x - e.getX();
	        yAdjustment = parentLocation.y - e.getY();
	        selected = (InteriorPanel)c;
	        selected.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);

	        BasePanel.this.moveToFront(selected);
	        BasePanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	    }

	    //Move
		@Override
	    public void mouseDragged(MouseEvent me)
	    {
	        if (selected == null) return;

	        //  The drag location should be within the bounds of the chess board

	        int posX = me.getX() + xAdjustment;
	        int xMax = BasePanel.this.getWidth() - selected.getWidth();
	        posX = Math.min(posX, xMax);
	        posX = Math.max(posX, 0);

	        int posY = me.getY() + yAdjustment;
	        int yMax = BasePanel.this.getHeight() - selected.getHeight();
	        posY = Math.min(posY, yMax);
	        posY = Math.max(posY, 0);

	        selected.setLocation(posX, posY);
	     }

	    //Drop
	    @Override
	    public void mouseReleased(MouseEvent e)
	    {
	        //this.setCursor(null);

	        if (selected == null) return;


	        //Make sure the chess piece is no longer painted on the layered pane

	        //chessPiece.setVisible(false);
	        //layeredPane.remove(chessPiece);
	        //chessPiece.setVisible(true);


	        //  The drop location should be within the bounds of the chess board

	        int xMax = BasePanel.this.getWidth() - selected.getWidth();
	        int x = Math.min(e.getX(), xMax);
	        x = Math.max(x, 0);

	        int yMax = BasePanel.this.getHeight() - selected.getHeight();
	        int y = Math.min(e.getY(), yMax);
	        y = Math.max(y, 0);

	        Component c =  cont.findComponentAt(x, y);

	        if (c instanceof JLabel)
	        {
	            Container parent = c.getParent();
	            parent.remove(0);
	            parent.add(selected);
	            parent.validate();
	        }
	        else
	        {
	            Container parent = (Container)c;
	            parent.add(selected);
	            parent.validate();
	        }
	    }

	    public void mouseClicked(MouseEvent e) {}
	    public void mouseMoved(MouseEvent e) {}
	    public void mouseEntered(MouseEvent e) {}
	    public void mouseExited(MouseEvent e) {}
	};

	addMouseListener(mouseAdapter);
}*/