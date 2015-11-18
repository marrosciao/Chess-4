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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class HelpPanel extends JPanel implements MouseListener,
		MouseMotionListener
{
	private boolean clickedEffect;
	private Image images[] = new Image[12];
	private int page;
	private Timer repaintTimer;
	private ChessMain parentFrame;
	private boolean soundOn;
	public HelpPanel(ChessMain frame)
	{
		
		soundOn = true;
		clickedEffect = false;
		repaintTimer = new Timer(10, new TimerEventHandler());
		repaintTimer.start();
		page = 0;
		parentFrame = frame;
		images[0] = new ImageIcon("img\\HelpScreens\\IndexScreen.png")
				.getImage();
		images[1] = new ImageIcon("img\\HelpScreens\\HowToPlay.jpg").getImage();
		images[2] = new ImageIcon("img\\HelpScreens\\KingHelp.jpg").getImage();
		images[3] = new ImageIcon("img\\HelpScreens\\QueenHelp.jpg").getImage();
		images[4] = new ImageIcon("img\\HelpScreens\\RookHelp.jpg").getImage();
		images[5] = new ImageIcon("img\\HelpScreens\\KnightHelp.jpg")
				.getImage();
		images[6] = new ImageIcon("img\\HelpScreens\\BishopHelp.jpg")
				.getImage();
		images[7] = new ImageIcon("img\\HelpScreens\\PawnHelp.jpg").getImage();
		images[8] = new ImageIcon("img\\HelpScreens\\CastlingHelp.jpg")
				.getImage();
		images[9] = new ImageIcon("img\\HelpScreens\\EnPassantHelp.jpg")
				.getImage();
		images[10] = new ImageIcon("img\\HelpScreens\\PawnPromoteHelp.jpg")
				.getImage();
		images[11] = new ImageIcon("img\\HelpScreens\\GameButtonsHelp.jpg")
				.getImage();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public static void playSoundRaw(String fileName) throws MalformedURLException, LineUnavailableException, UnsupportedAudioFileException, IOException
	{
	    File url = new File(fileName);
	    Clip clip = AudioSystem.getClip();

	    AudioInputStream ais = AudioSystem.
	        getAudioInputStream( url );
	    clip.open(ais);
	    clip.start();
	}
	public void playSound (String fileName)
	{
		try {
            playSoundRaw(fileName);
        } catch (MalformedURLException ex) {
            System.out.println(1);
        } catch (LineUnavailableException ex) {
        	System.out.println(2);
        } catch (UnsupportedAudioFileException ex) {
        	System.out.println(3);
        } catch (IOException ex) {
        	System.out.println(55);
        }
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
		System.out.println(page);
		g.drawImage(images[page], 0, 0, null);
	}

	
	public void toggleVolume()
	{
		soundOn = !soundOn;
	}
	
	/**
	 * Changes the mouse to a hand if it is over something clickable.
	 */
	public void mouseMoved(MouseEvent event)
	{
		Rectangle back;
		Point mousePoint = event.getPoint();
		if (page == 0)
		{
			boolean hover = false;
			// Left side of index.
			for (int page = 1; page <= 6; page++)
			{
				Rectangle levelRectangle = new Rectangle(20,
						20 + 112 * (page - 1), 470, 112);
				if (levelRectangle.contains(mousePoint))
				{
					clickedEffect = true;
					hover = true;
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
			}
			// Right side of index.
			for (int page = 1; page <= 5; page++)
			{
				Rectangle levelRectangle = new Rectangle(530,
						20 + 120 * (page - 1), 470, 112);
				if (levelRectangle.contains(mousePoint))
				{
					clickedEffect = true;
					hover = true;
					setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				}
			}
			back = new Rectangle(680, 610, 220, 100);
			if (back.contains(mousePoint))

			{
				clickedEffect = true;
				hover = true;
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			if (!hover)
			{
				setCursor(Cursor.getDefaultCursor());
				clickedEffect = false;
			}

		}
		else 
		{
			Rectangle left = new Rectangle(280, 580, 90, 110);
			Rectangle right = new Rectangle(620, 580, 90, 110);
			back = new Rectangle(790, 600, 210, 90);
			if (back.contains(mousePoint) || left.contains(mousePoint)
					|| (right.contains(mousePoint) && page != 11))
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
		
			
		repaint();

	}

	/**
	 * Handles the mouse pressing something in the help panel.
	 */
	public void mousePressed(MouseEvent event)
	{
		if (soundOn && clickedEffect)
			playSound("sounds\\clicked.wav");
		Rectangle back;
		Point mousePoint = event.getPoint();
		if (page == 0)
		{
			// Left side of index.
			for (int page = 1; page <= 6; page++)
			{
				Rectangle levelRectangle = new Rectangle(20,
						20 + 112 * (page - 1), 470, 112);
				if (levelRectangle.contains(mousePoint))
				{
					System.out.println(page);
					this.page = page;
				}
			}
			// Right side of index.
			for (int page = 1; page <= 5; page++)
			{
				Rectangle levelRectangle = new Rectangle(530,
						20 + 112 * (page - 1), 470, 112);
				if (levelRectangle.contains(mousePoint))
				{
					this.page = page + 6;
				}
			}
			back = new Rectangle(680, 610, 220, 100);
			if (back.contains(mousePoint))
			{
				
			{
				if (parentFrame.currentScreen == parentFrame.GAME)
				{
					parentFrame.switchTo(parentFrame.GAME, this);
				
				}
				else
				{
					parentFrame.switchTo(parentFrame.MENU, this);
					
				}
				
			}
			}

		}
		else
		{
			Rectangle left = new Rectangle(280, 580, 90, 110);
			Rectangle right = new Rectangle(620, 580, 90, 110);
			back = new Rectangle(790, 600, 210, 90);
			if (back.contains(mousePoint))
			{
				page =0;

			}
			else if (left.contains(mousePoint))
			{
				if (page != 0)
				{
					page--;
				}
			}
			else if (right.contains(mousePoint))
			{
				if (page != 11)
				{
					page++;
				}
			}
		}
		repaint();

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
		// TODO Auto-generated method stub

	}

}
