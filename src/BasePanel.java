import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;


public class BasePanel extends JLayeredPane{

	//public static ArrayList<Component> components = new ArrayList<Component>();

	static Rectangle windowRect = AIPlayground.windowRect;

	public BasePanel()
	{
		super();
		this.setBounds(windowRect);
		this.setPreferredSize(AIPlayground.windowSize);

		//initMouseAdapter();	

		this.setBackground(Color.GREEN);
		this.setOpaque(true);
	}

	public int getTotalComponents()
	{
		int total = 0;
		for(int i = 0; i < highestLayer(); i++)
			total += getComponentCountInLayer(i);
		return total;
	}

	public Component add(Component c, int layer)
	{
		setLayer(c, layer);
		add(c);

		repaint();
		return c;
	}

	public Component addOnTop(Component c)
	{
		add(c, highestLayer() + 1);

		repaint();
		return c;
	}

	public void clearLayer(int layer)
	{
		for(Component c : getComponentsInLayer(layer))
			remove(c);
		repaint();
	}

	public void clearUnder(int max)
	{
		for(int layer = 0; layer < max; layer++)
			clearLayer(layer);
		repaint();
	}

	public void clearOver(int min)
	{
		for(int layer = min + 1; layer <= highestLayer(); layer++)
			clearLayer(layer);
		repaint();
	}

	//clearBetween() does not include the min and max, only what's between them
	public void clearBetween(int min, int max)
	{
		for(int layer = min + 1; layer < max; layer++)
			clearLayer(layer);
	}

	public void clearAll()
	{
		for(int layer = 0; layer <= highestLayer(); layer++)
			clearLayer(layer);
		repaint();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//g.drawString("Panels: " + components.size(), 40, 100);
	}

	public static void main(String[] args)
	{
		final JFrame frame = new JFrame("frametitle");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setMinimumSize(new Dimension(400,400));
		frame.setSize(new Dimension(400,400));
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

		JPanel backPanel = new JPanel();
		backPanel.setBounds(0,0,400,400);
		backPanel.setBackground(Color.BLUE);
		basePanel.add(backPanel, 0);

		JButton addButton = new JButton("Add");
		addButton.setBounds(10, 10, 100, 50);
		addButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				InteriorPanel panel = new InteriorPanel(new Rectangle(150,150,200,200), true);
				panel.setBackground(Color.WHITE);

				Random randy = new Random();
				int layer = randy.nextInt(10);

				panel.add(new GameLabel(Integer.toString(layer), new Rectangle(50,50,10,10)));

				basePanel.add(panel, layer);

				//components.add(panel);
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