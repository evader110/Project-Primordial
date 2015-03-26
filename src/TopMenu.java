
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;


public class TopMenu extends JMenuBar
{
	private static final long serialVersionUID = 1L;

	
	static JMenu createMenu, optionsMenu;
	static JMenu factionMenu;

	static ButtonGroup factionButtons = new ButtonGroup();

	static JCheckBox hiveMindCheckBox;
	static JCheckBox pauseCheckBox;
	static JCheckBox foodCheckBox;
	static JCheckBox greenFactionCheckBox;
	static JRadioButton redButton;
	static JRadioButton blueButton;
	
	static JMenuItem droneButton;
	static JMenuItem foodButton;
	static JMenuItem resetButton;
	
	
	public TopMenu()
	{
		initButtons();
		initMenus();
	}
	
	public void initButtons()
	{
		initCreateButtons();
		initOptionsButtons();
	}
	
	public void initCreateButtons()
	{
		foodButton = new JMenuItem("Food");
		foodButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});		
		
		droneButton = new JMenuItem("Drone");
		droneButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
			}
		});		
		
		
		ActionListener factionListener = new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(e.getSource().equals(redButton))
				{
					//TODO: Wire this
					//Faction = red
				}
				else if(e.getSource().equals(blueButton))
				{
					//Faction = blue
				}
			}
		};

		redButton  = new JRadioButton("Red");
		blueButton  = new JRadioButton("Blue");
		redButton.addActionListener(factionListener);
		blueButton.addActionListener(factionListener);
	}
	
	public void initOptionsButtons()
	{		
		foodCheckBox = new JCheckBox("Spawn Food");
		foodCheckBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.toggleFoodSpawn();
			}
		});
		
		greenFactionCheckBox = new JCheckBox("Green Faction");
		greenFactionCheckBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.toggleGreenFaction();
			}
		});
		
		hiveMindCheckBox = new JCheckBox("Hive Mind");
		hiveMindCheckBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// TODO Wire this
				//GamePanel.toggleHiveMind();
				
				GamePanel.clearRegions();
			}
		});
		
		pauseCheckBox = new JCheckBox("Pause");
		pauseCheckBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.togglePause();
			}
		});
		
		resetButton = new JMenuItem("Reset");
		resetButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GamePanel.resetWorld();
			}
		});
	}
	
	public void initMenus()
	{
		initCreateMenu();
		initOptionsMenu();
		
		this.add(createMenu);
		this.add(optionsMenu);
	}
	
	public void initCreateMenu()
	{
		createMenu = new JMenu("Create");
		createMenu.add(foodButton);
		createMenu.addSeparator();
		createMenu.add(droneButton);
		
		initFactionMenu();
		
		createMenu.add(factionMenu);
	}

	public void initFactionMenu(){
		factionMenu = new JMenu("Faction");

		factionButtons.add(redButton);
		factionButtons.add(blueButton);

		redButton.setSelected(true);

		factionMenu.add(redButton);
		factionMenu.add(blueButton);
	}	
	
	public void initOptionsMenu(){
		optionsMenu = new JMenu("Options");	
		
		optionsMenu.add(pauseCheckBox);
		optionsMenu.add(foodCheckBox);
		optionsMenu.add(greenFactionCheckBox);
		optionsMenu.add(hiveMindCheckBox);
		optionsMenu.addSeparator();
		optionsMenu.add(resetButton);
	}
}
