package mazinator.main;

/**
 * Represents the four directions in the {@link Maze}. Basically an enum, with numerical values retained
 * for backwards compatibility (aka laziness)
 */
public final class Direction {
	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	
	private Direction () {} // prevent this class from being instantiated
}