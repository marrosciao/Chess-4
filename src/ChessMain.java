import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ChessMain extends JFrame
{
	private boolean snowOn;
	protected int colour;
	protected int noOfPlayers;
	private HelpPanel help;
	protected BoardPanel board;
	private MenuPanel menu;
	private final static String GAME_NAME = "Chess";
	private final static int GAME_WIDTH = 1024;
	private final static int GAME_HEIGHT = 745;
	protected int currentScreen;
	protected final int MENU = 20;
	protected final int GAME = 21;
	protected final int HELP = 1;
	private Clip backgroundMusic;
	protected boolean inHelp;

	private boolean backMusicOn;// private Image logoIcon;

	// private Image mainImage;

	ChessMain() throws LineUnavailableException, UnsupportedAudioFileException,
			IOException
	{
		super(GAME_NAME);
		File url = new File("sounds\\music.wav");
		Clip clip = AudioSystem.getClip();

		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		clip.open(ais);
		backgroundMusic = clip;

		snowOn = true;
		backMusicOn = true;
		setIconImage(new ImageIcon("img\\Logo.png").getImage());
		currentScreen = MENU;
		this.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		this.setVisible(true);
		this.setResizable(false);
		setLayout(new BorderLayout());
		menu = new MenuPanel(this);
		help = new HelpPanel(this);
		board = new BoardPanel(this);
		// board.inGame=false;
		add(menu, BorderLayout.CENTER);
		menu.requestFocusInWindow();
		//addWindowListener(new CloseWindow(this));
	}

	public static void main(String[] args) throws LineUnavailableException,
			UnsupportedAudioFileException, IOException
	{

		ChessMain frame = new ChessMain();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.playSound("sounds\\music.wav", true);
	}

	public boolean isSoundOn()
	{
		return backMusicOn;
	}

	public boolean isSnowOn()
	{
		return snowOn;
	}

	public void toggleSnow()
	{
		snowOn = !snowOn;
	}

	public void playSoundRaw(String fileName, boolean musicOn)
			throws MalformedURLException, LineUnavailableException,
			UnsupportedAudioFileException, IOException
	{

		if (musicOn)
		{
			backgroundMusic.loop(-1);
		}
		else
		{
			backgroundMusic.stop();
		}
	}

	public void playSound(String fileName, boolean musicOn)
	{
		try
		{
			playSoundRaw(fileName, musicOn);
		}
		catch (MalformedURLException ex)
		{
			System.out.println(1);
		}
		catch (LineUnavailableException ex)
		{
			System.out.println(2);
		}
		catch (UnsupportedAudioFileException ex)
		{
			System.out.println(3);
		}
		catch (IOException ex)
		{
			System.out.println(55);
		}
	}

	public void toggleAllVolume()
	{
		menu.toggleVolume();
		help.toggleVolume();
		backMusicOn = !backMusicOn;
		this.playSound("sounds\\music.wav", backMusicOn);
	}

	public void setOptions(int noOfPlayers, int colour, int difficulty)
	{
		this.noOfPlayers = noOfPlayers + 1;
		if (colour == 0)
		{
			this.colour = 1;
		}
		else if (colour == 1)
		{
			this.colour = -1;
		}
		board.setOptions(this.noOfPlayers == 1, this.colour, difficulty);

	}

	public void switchTo(int screen, JPanel previousPanel)
	{
		if (previousPanel instanceof HelpPanel)
			inHelp = false;
		remove(previousPanel);
		if (screen == MENU)
		{

			add(menu, BorderLayout.CENTER);
			menu.revalidate();
			menu.requestFocusInWindow();
			currentScreen = MENU;
		}
		else if (screen == GAME)
		{
			if (previousPanel instanceof MenuPanel)
				board.newGame();
			add(board, BorderLayout.CENTER);
			board.revalidate();
			board.requestFocusInWindow();
			currentScreen = GAME;

		}
		else if (screen == HELP)
		{
			add(help, BorderLayout.CENTER);
			help.revalidate();
			help.requestFocusInWindow();
			inHelp = true;
		}

	}

//	class CloseWindow extends WindowAdapter
//	{
//		JFrame frame;
//
//		CloseWindow(JFrame frame)
//		{
//			super();
//			this.frame = frame;
//		}
//
//		public void windowClosing(WindowEvent event)
//		{
//			
//
//		}
//	}

}
