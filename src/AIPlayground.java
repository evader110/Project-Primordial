
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;

/*
 * This is your runner class. Your main() method is located here.
 */

public class AIPlayground
{
	private static BorderLayout mainLayout = new BorderLayout();
	
	private static JFrame mainWindow = new JFrame("Template");
	
	private static TopMenu topMenu;
	private static GamePanel gamePanel;
	
	private static int windowWidth = 1280;
	private static int windowHeight = 720;
	
	final static Dimension windowSize = new Dimension(1280, 720);
	public static Rectangle windowRect = new Rectangle(0, 0, windowWidth, windowHeight);
	
	public static void main(String[] args)
	{
		initializeWindow();
		
		startGame();
	}
	
	public static void initializeWindow()	//Initializes a JFrame
	{
		mainWindow.setSize(windowWidth, windowHeight);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.setBackground(Color.WHITE);
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setLayout(mainLayout);
			
		initTopMenu();
		initGamePanel();
		
		
		mainWindow.setVisible(true);
	}
	
	public static void initTopMenu()
	{
		topMenu = new TopMenu();
		mainWindow.setJMenuBar(topMenu);
	}
	
	public static void initGamePanel()	//Creates a new GamePanel
	{
		gamePanel = new GamePanel(windowWidth, windowHeight);

		//Not sure if you want to use BorderLayout
		mainWindow.add(gamePanel);//, BorderLayout.CENTER);
		//mainWindow.pack();
	}
	
	//Change this as you add new stuff on the borders or whatever
	public static Rectangle getAvailableSpace()
	{
		return new Rectangle(mainWindow.getWidth(), mainWindow.getHeight() - 25); //(int)topMenu.getBounds().getHeight());	
	}
	
	public static void startGame()	//Starts the game
	{
		gamePanel.run();
	}
}
