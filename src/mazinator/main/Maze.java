package mazinator.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Stack;

/**
 * Represents the maze that is navigated by various characters in the game.
 */
public class Maze implements Displayable {
	public static final int LENGTH = 20;
	public static final int WIDTH = 13;
	
	private Cell[][] maze = new Cell[LENGTH][WIDTH]; // to be phased out eventually
	private boolean[][] pixels = new boolean[LENGTH * 25][WIDTH * 25];
	
	private Point start;
	
	/**
	 * Initializes a new maze object, randomly generating the maze by calling
	 * {@link Maze#generate() generate()}.
	 */
	public Maze () {
		generate();
		toPixels();
	}
	
	/**
	 * Randomly generates a maze using a recursive backtracking algorithm.
	 */
	private void generate () {
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				maze[i][j] = new Cell();
			}
		}
		
		Random r = new Random();
		Stack<Cell> cells = new Stack<Cell>();
		int x = 0;
		int y = r.nextInt(WIDTH); // randomly choose a cell from the bottom row to start with
		start = new Point(x, y);
		cells.push(maze[x][y]);
		cells.peek().removeWall(Direction.DOWN);

		boolean[] adjacent = hasAdjacent(x, y);
		int cell = chooseCell(adjacent); // pick a cell to go to
		cells.peek().removeWall(cell); // remove the wall adjacent to that cell
		int from;
		if (cell == Direction.UP) {
			x++;
			from = Direction.DOWN;
		} else if (cell == Direction.LEFT) {
			y--;
			from = Direction.RIGHT;
		} else { // cell == Direction.RIGHT
			y++;
			from = Direction.LEFT;
		}
		generate(cells, x, y, from);
		
		// remove the top wall from a random cell in the top row
		maze[LENGTH - 1][r.nextInt(WIDTH)].removeWall(Direction.UP); 
		
		// loop through the maze and randomly remove some walls, to make it more easily navigable
		for (int i = LENGTH - 2; i >= 1; i--) {
			for (int j = 1; j < WIDTH - 1; j++) {
				if (r.nextInt(5) == 0) {
					int rand = r.nextInt(4);
					maze[i][j].removeWall(rand);
					if (rand == Direction.UP) {
						maze[i + 1][j].removeWall(Direction.DOWN);
					} else if (rand == Direction.DOWN) {
						maze[i - 1][j].removeWall(Direction.UP);
					} else if (rand == Direction.LEFT) {
						maze[i][j - 1].removeWall(Direction.RIGHT);
					} else { // rand == Direction.RIGHT
						maze[i][j + 1].removeWall(Direction.LEFT);
					}
				}
			}
		}
	}
	
	/**
	 * Randomly generates the maze using a recursive backtracking algorithm.
	 * @param cells
	 * @param x
	 * @param y
	 * @param from
	 */
	private void generate (Stack<Cell> cells, int x, int y, int from) {
		if (from < 0 || from >= 4) {
			throw new IllegalArgumentException("from must be a valid Direction.");
		}
		
		cells.push(maze[x][y]);
		cells.peek().removeWall(from);
		boolean[] adjacent = hasAdjacent(x, y);
		if (adjacent[4]) {
			int cell = chooseCell(adjacent);
			cells.peek().removeWall(cell);
			int x2 = x;
			int y2 = y;
			if (cell == Direction.UP) {
				x2++;
				from = Direction.DOWN;
			} else if (cell == Direction.DOWN) {
				x2--;
				from = Direction.UP;
			} else if (cell == Direction.LEFT) {
				y2--;
				from = Direction.RIGHT;
			} else { // cell == Direction.RIGHT
				y2++;
				from = Direction.LEFT;
			}
			generate(cells, x2, y2, from);
			adjacent = hasAdjacent(x, y);
			if (adjacent[4]) {
				cell = chooseCell(adjacent);
				cells.peek().removeWall(cell);
				int from2;
				int x3 = x;
				int y3 = y;
				if (cell == Direction.UP) {
					x3++;
					from2 = Direction.DOWN;
				} else if (cell == Direction.DOWN) {
					x3--;
					from2 = Direction.UP;
				} else if (cell == Direction.LEFT) {
					y3--;
					from2 = Direction.RIGHT;
				} else { // cell == Direction.RIGHT
					y3++;
					from2 = Direction.LEFT;
				}
				generate(cells, x3, y3, from2);
				adjacent = hasAdjacent(x, y);
				if (adjacent[4]) {
					cell = chooseCell(adjacent);
					cells.peek().removeWall(cell);
					int from3;
					int x4 = x;
					int y4 = y;
					if (cell == Direction.UP) {
						x4++;
						from3 = Direction.DOWN;
					} else if (cell == Direction.DOWN) {
						x4--;
						from3 = Direction.UP;
					} else if (cell == Direction.LEFT) {
						y4--;
						from3 = Direction.RIGHT;
					} else { // cell == Direction.RIGHT
						y4++;
						from3 = Direction.LEFT;
					}
					generate(cells, x4, y4, from3);
				}
			}
		}
		
		cells.pop();
	}
	
	/**
	 * Converts a two-dimensional array of {@link Cell}s into a two-dimensional array of
	 * {@code boolean}s representing which pixels on the screen are occupied by walls and which
	 * aren't.
	 */
	private void toPixels () {
		for (int row = 0; row < LENGTH; row++) {
			for (int col = 0; col < LENGTH; col++) {				
				boolean[] walls = {
						maze[row][col].isOpen(Direction.UP),
						maze[row][col].isOpen(Direction.DOWN),
						maze[row][col].isOpen(Direction.LEFT),
						maze[row][col].isOpen(Direction.RIGHT)
				};
				
				if (walls[Direction.UP]) {
					for (int pcol = col * 25 + 1; pcol < col * 25 + 24; pcol++) {
						pixels[row * 25][pcol] = true;
					}
				}
				
				if (walls[Direction.DOWN]) {
					for (int pcol = col * 25 + 1; pcol < col * 25 + 24; pcol++) {
						pixels[row * 25 + 24][pcol] = true;
					}
				}
				
				if (walls[Direction.LEFT]) {
					for (int prow = row * 25 + 1; prow < row * 25 + 24; prow++) {
						pixels[prow][col * 25] = true;
					}
				}
				
				if (walls[Direction.RIGHT]) {
					for (int prow = row * 25 + 1; prow < row * 25 + 24; prow++) {
						pixels[prow][col * 25 + 24] = true;
					}
				}
			}
		}
	}
	
	/**
	 * Returns an array of booleans containing information about the cells that are adjacent
	 * to the current cell. Supposing that the array is called {@code arr}, the values in
	 * {@code arr} are arranged so that:
	 * <ul>
	 * 		<li>
	 * 			If {@code arr[}{@link Cell#TOP}{@code ]} is {@code true}, then the {@link Cell}
	 * 			that is adjacent to the specified {@link Cell} and closer to the end of the maze
	 * 			exists and is closed.
	 * 		</li>
	 * 		<li>
	 * 			If {@code arr[}{@link Cell#BOTTOM}{@code ]} is {@code true}, then the {@link Cell}
	 * 			that is adjacent to the specified {@link Cell} and closer to the beginning of the
	 * 			maze exists and is closed.
	 * 		</li>
	 * 		<li>
	 * 			If {@code arr[}{@link Cell#LEFT}{@code ]} is {@code true}, then the {@link Cell}
	 * 			that is adjacent to the specified {@link Cell} and closer to the left side of the
	 * 			maze exists and is closed.
	 * 		</li>
	 * 		<li>
	 * 			If {@code arr[}{@link Cell#RIGHT}{@code ]} is {@code true}, then the {@link Cell}
	 * 			that is adjacent to the specified {@link Cell} and closer to the right side of the
	 * 			maze exists and is closed.
	 * 		</li>
	 * 		<li>
	 * 			If {@code arr[4]} is {@code true}, then the expression
	 * 			{@code arr[}{@link Cell#TOP}{@code ] || arr[}{@link Cell#BOTTOM}{@code ] || arr[}
	 * 			{@link Cell#LEFT}{@code ] || arr[}{@link Cell#RIGHT}{@code ]} will evaluate to
	 * 			{@code true}.
	 * 		</li>
	 * </ul>
	 * A {@link Cell} that is closed is defined as a {@link Cell} that has walls on four sides.
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean[] hasAdjacent (int x, int y) {
		boolean[] adjacent = new boolean[] {false, false, false, false, false};
		if (x != LENGTH - 1) {
			if (maze[x + 1][y].isClosed()) {
				adjacent[Direction.UP] = true;
			}
		}
		
		if (x != 0) {
			if (maze[x - 1][y].isClosed()) {
				adjacent[Direction.DOWN] = true;
			}
		}
		
		if (y != 0) {
			if (maze[x][y - 1].isClosed()) {
				adjacent[Direction.LEFT] = true;
			}
		}
		
		if (y != WIDTH - 1) {
			if (maze[x][y + 1].isClosed()) {
				adjacent[Direction.RIGHT] = true;
			}
		}
		
		adjacent[4] = adjacent[Direction.UP] || adjacent[Direction.DOWN] || adjacent[Direction.LEFT]
				|| adjacent[Direction.RIGHT];
		
		return adjacent;
	}

	/**
	 * Randomly picks an index in an array of {@code boolean}s with a length of five, in such a way
	 * that {@code index > 0 && index < 5} and {@code array[index]} is {@code true}.
	 * @param adjacent The array that the index is to be chosen from.
	 * @return The chosen index.
	 */
	private int chooseCell (boolean[] adjacent) {
		if (adjacent.length != 5) {
			throw new IllegalArgumentException();
		}
		
		Random r = new Random();
		if (adjacent[Direction.UP] && !adjacent[Direction.DOWN] && !adjacent[Direction.LEFT]
				&& !adjacent[Direction.RIGHT]) {
			return Direction.UP;
		} else if (!adjacent[Direction.UP] && adjacent[Direction.DOWN] && !adjacent[Direction.LEFT]
				&& !adjacent[Direction.RIGHT]) {
			return Direction.DOWN;
		} else if (!adjacent[Direction.UP] && !adjacent[Direction.DOWN] && adjacent[Direction.LEFT]
				&& !adjacent[Direction.RIGHT]) {
			return Direction.LEFT;
		} else if (!adjacent[Direction.UP] && !adjacent[Direction.DOWN] && !adjacent[Direction.LEFT]
				&& adjacent[Direction.RIGHT]) {
			return Direction.RIGHT;
		} else if (adjacent[Direction.UP] && adjacent[Direction.DOWN] && !adjacent[Direction.LEFT]
				&& !adjacent[Direction.RIGHT]) {
			return r.nextInt(2);
		} else if (adjacent[Direction.UP] && !adjacent[Direction.DOWN] && adjacent[Direction.LEFT]
				&& !adjacent[Direction.RIGHT]) {
			int n = r.nextInt(2);
			if (n == 0) {
				return Direction.UP;
			} else {
				return Direction.LEFT;
			}
		} else if (adjacent[Direction.UP] && !adjacent[Direction.DOWN] && !adjacent[Direction.LEFT]
				&& adjacent[Direction.RIGHT]) {
			int n = r.nextInt(2);
			if (n == 0) {
				return Direction.UP;
			} else {
				return Direction.RIGHT;
			}
		} else if (!adjacent[Direction.UP] && adjacent[Direction.DOWN] && adjacent[Direction.LEFT]
				&& !adjacent[Direction.RIGHT]) {
			return r.nextInt(2) + 1;
		} else if (!adjacent[Direction.UP] && adjacent[Direction.DOWN] && !adjacent[Direction.LEFT]
				&& adjacent[Direction.RIGHT]) {
			int n = r.nextInt(2);
			if (n == 0) {
				return Direction.DOWN;
			} else {
				return Direction.RIGHT;
			}
		} else if (!adjacent[Direction.UP] && !adjacent[Direction.DOWN] && adjacent[Direction.LEFT]
				&& adjacent[Direction.RIGHT]) {
			return r.nextInt(2) + 2;
		} else if (adjacent[Direction.UP] && adjacent[Direction.DOWN] && adjacent[Direction.LEFT]
				&& !adjacent[Direction.RIGHT]) {
			return r.nextInt(3);
		} else if (adjacent[Direction.UP] && !adjacent[Direction.DOWN] && adjacent[Direction.LEFT]
				&& adjacent[Direction.RIGHT]) {
			int n = r.nextInt(3);
			if (n == 0) {
				return Direction.UP;
			} else {
				return n + 1;
			}
		} else if (adjacent[Direction.UP] && adjacent[Direction.DOWN] && !adjacent[Direction.LEFT]
				&& adjacent[Direction.RIGHT]) {
			int n = r.nextInt(3);
			if (n == 2) {
				return Direction.DOWN;
			} else {
				return n;
			}
		} else if (!adjacent[Direction.UP] && adjacent[Direction.DOWN] && adjacent[Direction.LEFT]
				&& adjacent[Direction.RIGHT]) {
			return r.nextInt(3) + 1;
		} else { // adjacent[Direction.UP] && adjacent[Direction.DOWN] && adjacent[Direction.LEFT] 
				 // && adjacent[Direction.RIGHT]
			return r.nextInt(4);
		}
	}
	
	/**
	 * @return The coordinates of the cell that is at the entry point of the maze.
	 */
	public Point getStart () {
		return start.clone();
	}
	
	public Cell get (Point p) {
		return get(p.getX(), p.getY());
	}
	
	public Cell get (int x, int y) {
		return maze[x][y].clone();
	}
	
	public boolean isOpen (java.awt.Point p) {
		return isOpen(new Point(p));
	}
	
	public boolean isOpen (Point p) {
		return pixels[p.getX()][p.getY()];
	}
	
	/**
	 * Draws this {@code Maze} object using a {@link Graphics} object.
	 * @param g The {@link Graphics} object to be drawn with.
	 */
	@Override
	public BufferedImage getImage () {
		BufferedImage img = new BufferedImage(WIDTH * 25 + 80, LENGTH * 25 + 80,
				BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		/*for (int i = LENGTH - 1; i >= 0; i--) {
			for (int j = 0; j < WIDTH; j++) {
				g.drawImage(maze[i][j].getImage(), j * 25 + 40, (LENGTH * 25 + 60) - (i * 25 + 40),
						null);
			}
		}*/
		
		for (int i = pixels.length - 1; i >= 0; i--) {
			for (int j = 0; j < pixels[0].length; j++) {
				if (pixels[i][j]) {
					g.setColor(Mazinator.COLOR);
				} else {
					g.setColor(Color.BLACK);
				}
				
				
			}
		}
		
		return img;
	}
}