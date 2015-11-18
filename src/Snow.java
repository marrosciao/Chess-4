import java.awt.Color;
import java.awt.Graphics;


public class Snow
{
	private int y;
	private int x;
	private int time;
	public Snow (int xStart)
	{
		y = -10;
		x = xStart;
		time = 0;
	}
	public void draw(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillOval(x, y, 5,5);
		if (time == 0)
			y++;
		time++;
		if (time == 2)
			time =0;
	}
	
	public boolean canRemove()
	{
		return (y > 720);
	}
}
