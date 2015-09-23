package mazinator.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import mazinator.main.Displayable;
import mazinator.main.Point;

public final class Bullet implements Displayable {
	private static final ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	private Tank origin;
	private int deg;
	private Point loc;
	private int bounceCount;
	
	public Bullet (Tank origin, Point loc) {
		this.origin = origin;
		this.loc = loc;
		bounceCount = 0;
	}
	
	@Override
	public BufferedImage getImage () {
		BufferedImage image = new BufferedImage(5, 5, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = image.getGraphics();
		
		int code = origin.getCode();
		if (code == Tank.BLUE) {
			g.setColor(Color.BLUE);
		} else if (code == Tank.GREEN) {
			g.setColor(Color.GREEN);
		} else { // code == Tank.LAIKA
			g.setColor(Color.GRAY);
		}
		
		g.fillOval(0, 0, image.getWidth(), image.getHeight());
		
		return image;
	}
	
	public void update () {
		
	}
}