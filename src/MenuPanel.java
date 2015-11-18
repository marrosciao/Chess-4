import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The JComponent that comprises of the main menu and the
 * 
 * @author PeterZhu
 *
 */
public class MenuPanel extends JPanel implements MouseListener,
		MouseMotionListener, Serializable
{
	private boolean clickedEffect;
	private boolean inAbout;
	private ChessMain parentFrame;
	private Image mainMenuImage;
	private Image aboutImage;
	private Timer repaintTimer;
	private boolean soundOn;
	private BoardPanel board;

	public MenuPanel(ChessMain frame)
	{
		soundOn = true;
		clickedEffect = false;
		inAbout = false;
		repaintTimer = new Timer(10, new TimerEventHandler());
		repaintTimer.start();
		parentFrame = frame;
		mainMenuImage = new ImageIcon("img\\mainMenu.png").getImage();
		aboutImage = new ImageIcon("img\\about.png").getImage();
		board = frame.board;
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	private class TimerEventHandler implements ActionListener
	{

		public void actionPerformed(ActionEvent event)
		{
			repaint();
		}

	}

	public void paintComponent(Graphics g)
	{
		if (!inAbout)
			g.drawImage(mainMenuImage, 0, 0, null);
		else
			g.drawImage(aboutImage, 0, 0, null);
	}

	/**
	 * Changes the mouse to a hand if it is over something clickable.
	 */
	public void mouseMoved(MouseEvent event)
	{
		Point point = event.getPoint();
		if (!inAbout)
		{
			Rectangle gameButton = new Rectangle(710, 290, 230, 70);
			Rectangle helpButton = new Rectangle(710, 390, 230, 70);
			Rectangle aboutButton = new Rectangle(710, 500, 230, 70);
			Rectangle exitButton = new Rectangle(710, 615, 230, 70);
			if (gameButton.contains(point) || helpButton.contains(point)
					|| aboutButton.contains(point)
					|| exitButton.contains(point))
			{
				clickedEffect = true;
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			else
			{
				clickedEffect = false;
				setCursor(Cursor.getDefaultCursor());
			}
		}
		else
		{
			Rectangle backButton = new Rectangle(790, 600, 234, 120);
			if (backButton.contains(point))
			{
				clickedEffect = true;
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			else
			{
				clickedEffect = false;
				setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	public void toggleVolume()
	{
		soundOn = !soundOn;
	}

	/**
	 * Handles the mouse pressing something in the help panel.
	 */
	public void mousePressed(MouseEvent event)
	{
		if (soundOn && clickedEffect)
			playSound("sounds\\clicked.wav");
		if (!inAbout)
		{
			Rectangle gameButton = new Rectangle(710, 290, 230, 70);
			Rectangle helpButton = new Rectangle(710, 390, 230, 70);
			Rectangle aboutButton = new Rectangle(710, 500, 230, 70);
			Rectangle exitButton = new Rectangle(710, 615, 230, 70);
			// If game is clicked, start the game.
			if (gameButton.contains(event.getPoint()))
			{
				int choice = 0;
				String[] options = new String[2];
				int difficulty = -2;
				// If the user chooses to continue.
				if (choice == 0)
				{
					options[0] = "Single Player";
					options[1] = "Two Player";
					choice = JOptionPane.showOptionDialog(parentFrame,
							"Choose the game mode.", "Game Mode",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[0]);
					if (choice != -1)
					{
						int side;

						if (choice == 0)
						{
							String[] sides = { "Black", "White" };
							side = JOptionPane.showOptionDialog(parentFrame,
									"Choose your side.", "Side Select",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE, null, sides,
									sides[0]);
							options = new String[3];
							options[0] = "Easy";
							options[1] = "Medium";
							options[2] = "Hard";
							difficulty = JOptionPane.showOptionDialog(
									parentFrame, "Choose the Difficulty",
									"Difficulty", JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE, null,
									options, options[0]);
							if (side == -1)
								side = -2;

						}
						else
							side = -1;
						if (side != -2 && difficulty != -1)
						{

							parentFrame.setOptions(choice, side, difficulty);
							parentFrame.switchTo(parentFrame.GAME, this);
						}
					}
				}
			}
			else if (helpButton.contains(event.getPoint()))
			{
				parentFrame.switchTo(parentFrame.HELP, this);
			}
			else if (aboutButton.contains(event.getPoint()))
			{
				inAbout = true;
			}
			// add your exit button thing here idk what it even is supposed to
			// do.
			else if (exitButton.contains(event.getPoint()))
			{
				// board.writeToFile("SavedGame.dat");
				parentFrame.dispatchEvent(new WindowEvent(parentFrame,
						WindowEvent.WINDOW_CLOSING));
			}

		}
		else
		{
			Rectangle backButton = new Rectangle(790, 600, 234, 120);
			if (backButton.contains(event.getPoint()))
			{
				inAbout = false;
			}

		}
		repaint();

	}

	public static void playSoundRaw(String fileName)
			throws MalformedURLException, LineUnavailableException,
			UnsupportedAudioFileException, IOException
	{
		File url = new File(fileName);
		Clip clip = AudioSystem.getClip();

		AudioInputStream ais = AudioSystem.getAudioInputStream(url);
		clip.open(ais);
		clip.start();
	}

	public void playSound(String fileName)
	{
		try
		{
			playSoundRaw(fileName);
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

	public void mouseDragged(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseClicked(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0)
	{

	}

}
