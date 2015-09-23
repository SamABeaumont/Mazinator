package mazinator.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO; // to load images for displaying individual cells
import javax.swing.JOptionPane; // for the error message

/**
 * A class to represent the cells in the {@link Maze}
 */
public class Cell implements Displayable {
	
	// holds images that are used for displaying different types of cells
	private static final class Images {
		private static BufferedImage OPEN;
		private static BufferedImage TOP_CLOSED;
		private static BufferedImage BOTTOM_CLOSED;
		private static BufferedImage LEFT_CLOSED;
		private static BufferedImage RIGHT_CLOSED;
		private static BufferedImage TOP_BOTTOM_CLOSED;
		private static BufferedImage TOP_LEFT_CLOSED;
		private static BufferedImage TOP_RIGHT_CLOSED;
		private static BufferedImage BOTTOM_LEFT_CLOSED;
		private static BufferedImage BOTTOM_RIGHT_CLOSED;
		private static BufferedImage LEFT_RIGHT_CLOSED;
		private static BufferedImage TOP_BOTTOM_LEFT_CLOSED;
		private static BufferedImage TOP_BOTTOM_RIGHT_CLOSED;
		private static BufferedImage TOP_LEFT_RIGHT_CLOSED;
		private static BufferedImage BOTTOM_LEFT_RIGHT_CLOSED;
		private static BufferedImage CLOSED;
		
		static {
			try {
				OPEN = ImageIO.read(new File("OPEN.gif"));
				TOP_CLOSED = ImageIO.read(new File("TOP_CLOSED.gif"));
				BOTTOM_CLOSED = ImageIO.read(new File("BOTTOM_CLOSED.gif"));
				LEFT_CLOSED = ImageIO.read(new File("LEFT_CLOSED.gif"));
				RIGHT_CLOSED = ImageIO.read(new File("RIGHT_CLOSED.gif"));
				TOP_BOTTOM_CLOSED = ImageIO.read(new File("TOP_BOTTOM_CLOSED.gif"));
				TOP_LEFT_CLOSED = ImageIO.read(new File("TOP_LEFT_CLOSED.gif"));
				TOP_RIGHT_CLOSED = ImageIO.read(new File("TOP_RIGHT_CLOSED.gif"));
				BOTTOM_LEFT_CLOSED = ImageIO.read(new File("BOTTOM_LEFT_CLOSED.gif"));
				BOTTOM_RIGHT_CLOSED = ImageIO.read(new File("BOTTOM_RIGHT_CLOSED.gif"));
				LEFT_RIGHT_CLOSED = ImageIO.read(new File("LEFT_RIGHT_CLOSED.gif"));
				TOP_BOTTOM_LEFT_CLOSED = ImageIO.read(new File("TOP_BOTTOM_LEFT_CLOSED.gif"));
				TOP_BOTTOM_RIGHT_CLOSED = ImageIO.read(new File("TOP_BOTTOM_RIGHT_CLOSED.gif"));
				TOP_LEFT_RIGHT_CLOSED = ImageIO.read(new File("TOP_LEFT_RIGHT_CLOSED.gif"));
				BOTTOM_LEFT_RIGHT_CLOSED = ImageIO.read(new File("BOTTOM_LEFT_RIGHT_CLOSED.gif"));
				CLOSED = ImageIO.read(new File("CLOSED.gif"));
			} catch (IOException e) { // alert the user that the images cannot be loaded and exit the program
				JOptionPane.showMessageDialog(null, "Unable to load image files to display maze.", "",
						JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		private Images () {} // prevent this class from being instantiated
		
		/**
		 * Returns the {@link BufferedImage} used to display the specified {@link Cell}
		 * @param c The {@link Cell}
		 * @return
		 */
		public static BufferedImage getImage (Cell c) {
			boolean[] open = new boolean[] {
				c.isOpen(Direction.UP), c.isOpen(Direction.DOWN), c.isOpen(Direction.LEFT),
				c.isOpen(Direction.RIGHT)
			};
			
			// select the correct image to be displayed, depending on which cell walls are open
			if (open[0] && open[1] && open[2] && open[3]) {
				return OPEN;
			} else if (!open[0] && open[1] && open[2] && open[3]) {
				return TOP_CLOSED;
			} else if (open[0] && !open[1] && open[2] && open[3]) {
				return BOTTOM_CLOSED;
			} else if (open[0] && open[1] && !open[2] && open[3]) {
				return LEFT_CLOSED;
			} else if (open[0] && open[1] && open[2] && !open[3]) {
				return RIGHT_CLOSED;
			} else if (!open[0] && !open[1] && open[2] && open[3]) {
				return TOP_BOTTOM_CLOSED;
			} else if (!open[0] && open[1] && !open[2] && open[3]) {
				return TOP_LEFT_CLOSED;
			} else if (!open[0] && open[1] && open[2] && !open[3]) {
				return TOP_RIGHT_CLOSED;
			} else if (open[0] && !open[1] && !open[2] && open[3]) {
				return BOTTOM_LEFT_CLOSED;
			} else if (open[0] && !open[1] && open[2] && !open[3]) {
				return BOTTOM_RIGHT_CLOSED;
			} else if (open[0] && open[1] && !open[2] && !open[3]) {
				return LEFT_RIGHT_CLOSED;
			} else if (!open[0] && !open[1] && !open[2] && open[3]) {
				return TOP_BOTTOM_LEFT_CLOSED;
			} else if (!open[0] && !open[1] && open[2] && !open[3]) {
				return TOP_BOTTOM_RIGHT_CLOSED;
			} else if (!open[0] && open[1] && !open[2] && !open[3]) {
				return TOP_LEFT_RIGHT_CLOSED;
			} else if (open[0] && !open[1] && !open[2] && !open[3]) {
				return BOTTOM_LEFT_RIGHT_CLOSED;
			} else {
				return CLOSED;
			}
		}
	}
	
	private boolean[] open;
	
	/**
	 * Creates a new, completely closed {@code Cell}.
	 */
	public Cell () {
		open = new boolean[] {false, false, false, false};
	}
	
	/**
	 * Creates a new {@code Cell},
	 * @param open
	 */
	public Cell (boolean[] open) {
		if (open.length == 4) {
			this.open = open;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Indicates whether or not a given part of the {@code Cell} has a wall.
	 * @param wall One of the four constants {@link Direction#TOP}, {@link Direction#BOTTOM},
	 * 			{@link Direction#LEFT}, or {@link Direction#RIGHT}
	 * @return {@code true} if no wall exists at the specified location in the {@code Cell}, {@code false} otherwise.
	 */
	public boolean isOpen (int wall) {
		if (wall >= 0 && wall < 4) {
			return open[wall];
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * @return {@code true} if all sides of the {@code Cell} are walled off, {@code false} otherwise.
	 */
	public boolean isClosed () {
		return !open[Direction.UP] && !open[Direction.DOWN] && !open[Direction.LEFT]
				&& !open[Direction.RIGHT];
	}
	
	/**
	 * Removes a given wall from the {@code Cell}
	 * @param wall One of the four constants {@link Direction#TOP}, {@link Direction#BOTTOM},
	 * 			{@link Direction#LEFT}, or {@link Direction#RIGHT}
	 */
	public void removeWall (int wall) {
		if (wall >= 0 && wall < 4) {
			open[wall] = true;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * @return The {@link BufferedImage} that is used to display this {@code Cell}.
	 */
	@Override
	public BufferedImage getImage () {
		return Images.getImage(this);
	}
	
	/**
	 * @return a copy of this {@code Cell}.
	 */
	public Cell clone () {
		return new Cell(Arrays.copyOf(open, 4));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Cell} object.
	 */
	public String toString () {
		return getClass().getName() + "[open=" + Arrays.toString(open) + "]";
	}
}