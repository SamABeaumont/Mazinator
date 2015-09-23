package mazinator.main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import mazinator.tank.Blue;
import mazinator.tank.Green;
import mazinator.tank.Laika;
import mazinator.tank.Tank;

/**
 * A model of the game that is currently taking place.
 */
public final class Game implements Displayable {
	private Mazinator mazinator;
	private Maze maze;
	private Blue blue;
	private Green green;
	private Laika laika;
	private Point loc;
	private Point prevLoc;
	
	/**
	 * Creates a new {@code Game} object, initalizing all fields to {@code null}. For testing purposes only.
	 */
	@SuppressWarnings("unused")
	private Game () {
		this(null);
	}
	
	/**
	 * Creates a new {@code Game} object, initializing all fields to {@code null} with the exception of
	 * the field {@code mazinator}. For testing purposes only.
	 * @param mazinator The window (a {@link Mazinator} object) that this game is being displayed in.
	 */
	private Game (Mazinator mazinator) {
		this(mazinator, null, null, null);
	}
	
	/**
	 * Creates a new {@code Game object}, initializing all fields
	 * @param mazinator
	 * @param blue
	 * @param green
	 * @param laika
	 */
	public Game (Mazinator mazinator, Blue blue, Green green, Laika laika) {
		if (mazinator == null) {
			throw new IllegalArgumentException("A reference to the graphics window must be included"
					+ " in the call of this constructor.");
		} else if (blue == null && green == null && laika == null) {
			throw new IllegalArgumentException("It takes (at least) two to tango.");
		}
		
		this.mazinator = mazinator;
		maze = new Maze(); // initialize the maze
		this.blue = blue;
		this.green = green;
		this.laika = laika;
	}
	
	public void update () {
		Tank.update();
	}
	
	@Override
	public BufferedImage getImage () {
		BufferedImage image = new BufferedImage(Maze.WIDTH * 25 + 80, Maze.LENGTH * 25 + 80,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = image.getGraphics();
		return image;
	}
	
	@Override
	public String toString () {
		return getClass().getName();
	}
}