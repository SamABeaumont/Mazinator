package mazinator.tank;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import mazinator.main.Displayable;
import mazinator.main.Game;
import mazinator.main.Maze;
import mazinator.main.Point;

public abstract class Tank implements Displayable {	
	public static final int LAIKA = 0;
	public static final int BLUE = 1;
	public static final int GREEN = 2;
	
	private static final Tank[] tanks = new Tank[4];
	private static final ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	private final class Data {
		private Point loc; // "center" of the tank
		private Point tlCornerLoc; // top left corner
		private Point trCornerLoc; // top right corner
		private Point blCornerLoc; // bottom left corner
		private Point brCornerLoc; // bottom right corner
		private Point prevLoc;
		private int deg;
		
		public Point getLocation () {
			return loc;
		}
		
		public Point getTlCornerLoc () {
			return tlCornerLoc;
		}
		
		public Point getTrCornerLoc () {
			return trCornerLoc;
		}
		
		public Point getBlCornerLoc () {
			return blCornerLoc;
		}
		
		public Point getBrCornerLoc () {
			return brCornerLoc;
		}
		
		public Point getPreviousLocation () {
			return prevLoc;
		}
		
		public int getDegrees () {
			return deg;
		}
		
		public void move (boolean forward) {
			
		}
		
		public void rotate (boolean left) {
			if (left) {
				deg = (deg + 5) % 360;
			} else if (deg == 0) {
				deg = 360;
			} else {
				deg -= 5;
			}
		}
		
		public String toString () {
			return getClass().getName() + "[loc=" + loc + ",prevLoc=" + prevLoc + ",deg="+ deg + "]";
		}
	}
	
	private Game game;
	private Maze maze;
	private int code;
	private Data data;
	private boolean destroyed;
	
	@SuppressWarnings("unused")
	private Tank () {}
	
	public Tank (Game game) {
		this.game = game;
		if (code < 0 && code >= 4) {
			throw new IllegalArgumentException("Tank code must be one of the constants LAIKA, RED, "
					+ "GREEN, or WHITE.");
		} else if (tanks[code] != null) {
			throw new IllegalStateException("Two instances of the same tank cannot exist "
					+ "simultaneosly.");
		}
		
		tanks[code] = this;
	}
	
	protected final void setCode (int code) {
		if (code < 0 || code >= 4) {
			throw new IllegalArgumentException("Tank code must be one of the constants LAIKA, RED, "
					+ "GREEN, or WHITE.");
		}
		
		this.code = code;
	}
	
	public static final boolean isCreated (int code) {
		if (code < 0 && code >= 4) {
			throw new IllegalArgumentException("Tank code must be one of the constants LAIKA, RED, "
					+ "GREEN, or WHITE.");
		}
		
		return tanks[code] != null;
	}
	
	public static final void update () {
		
	}
	
	public static final void add (Bullet bullet) {
		bullets.add(bullet);
	}
	
	protected final int getCode () {
		return code;
	}
	
	public final Point getLocation () {
		return data.getLocation();
	}
	
	public final Point getPreviousLocation () {
		return data.getPreviousLocation();
	}
	
	protected abstract void move ();
	
	protected final void rotate (boolean left) {
		data.rotate(left);
	}
	
	protected final void moveForward () {
		
	}
	
	protected final void moveBackward() {
		
	}
	
	private final void destroy () {
		
	}
	
	@Override
	public final BufferedImage getImage () {
		BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		// fill in code for drawing the tank here
		return img;
	}
	
	@Override
	public String toString () {
		return getClass().getName() + "[tanks=" + Arrays.toString(tanks) + ",game=" + game
				+ ",code=" + code + ",deg=" + data.getDegrees() + ",destroyed=" + destroyed + "]";
	}
}