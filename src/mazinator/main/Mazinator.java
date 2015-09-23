package mazinator.main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/**
 * The main class. Handles input from the keyboard and displays the menus, instructions, and maze.
 */
public class Mazinator extends Sketchpad {
	public static final String[][] INSTRUCTIONS = {
		new String[] {
			"INSTRUCTIONS:",
			"",
			"To move up, press the up arrow key.",
			"To move down, press the down arrow key.",
			"To rotate left, press the left arrow key.",
			"To rotate right, press the right arrow key.",
			"To pause, press P.",
			"",
			"Enjoy."
		}
	};
	
	public static final Color COLOR = new Color(0, 255, 0);
	public static final Font BIG = new Font("Consolas", Font.PLAIN, 48);
	public static final Font MEDIUM = new Font("Consolas", Font.PLAIN, 24);
	public static final Font SMALL = new Font("Consolas", Font.PLAIN, 14);
	
	private State toDisplay;
	private Game game;
	private Maze maze;
	private Point loc;
	private Point prevLoc;
	private Button hovered;
	private int instructionNum;
	
	/**
	 * Creates a new game and initializes the maze.
	 */
	@Override
	public void setup (Graphics g) {
		setTitle("mazinator");
		setSize(400, 600);
		setResizable(false);
		setBackground(Color.BLACK);
		setColor(Color.WHITE);
		toDisplay = State.MENU;
		hovered = Button.NONE;
	}
	
	/**
	 * Displays the maze and indicates the player's location within the maze.
	 */
	@Override
	public void draw (Graphics g) {
		if (toDisplay == State.MENU) {
			showMenu(g);
		} else if (toDisplay == State.MAZE) {
			showMaze(g);
		} else if (toDisplay == State.PAUSED) {
			showPaused(g);
		} else { // toDisplay == State.INSTRUCTIONS
			showInstructions(g);
		}
	}
	
	/**
	 * Called if an uncaught exception is thrown at any point in the program.
	 * @param t The uncaught exception.
	 */
	@Override
	public void onError (Throwable t) {
		JOptionPane.showMessageDialog(null, // display a popup window indicating that an error occurred
				"An error occurred and this application needs to close.", "",
				JOptionPane.ERROR_MESSAGE);
		t.printStackTrace(); // print the stack trace of the uncaught exception
		System.exit(1); // exit the program
	}
	
	/**
	 * Displays the home menu.
	 * @param g The {@link Graphics} object with which the menu will be drawn.
	 */
	private void showMenu (Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), 160);
		g.setColor(COLOR);
		g.setFont(BIG);
		g.drawString("mazinator", 80, 100);
		g.setFont(MEDIUM);
		if (hovered == Button.FIRST) {
			g.fillRect(100, 160, 160, 65);
			g.setColor(Color.BLACK);
		} else {
			g.setColor(Color.BLACK);
			g.fillRect(100, 160, 200, 65);
			g.setColor(COLOR);
		}
		g.drawString("play game", 120, 200);
		g.setColor(COLOR);
		if (hovered == Button.SECOND) {
			g.fillRect(70, 260, 210, 60);
			g.setColor(Color.BLACK);
		} else {
			g.setColor(Color.BLACK);
			g.fillRect(70, 260, 210, 60);
			g.setColor(COLOR);
		}
		g.drawString("instructions", 100, 300);
		g.setColor(Color.BLACK);
		g.fillRect(140, 370, 110, 50);

	}
	
	/**
	 * Displays the maze and any objects within it.
	 * @param g The {@link Graphics} object with which the maze and associated objects will be drawn.
	 */
	private void showMaze (Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(maze.getImage(), 0, 0, null);
		g.setColor(Color.WHITE);
		g.fillOval(loc.getY() * 25 + 47, (Maze.LENGTH * 25 + 67) - (loc.getX() * 25 + 40), 10, 10);
		g.setColor(Color.BLACK);
		if (prevLoc != null) { // draw a black circle over the previous location so that there won't
			g.fillOval(prevLoc.getY() * 25 + 47, // be a white circle everywhere where the player went
					(Maze.LENGTH * 25 + 67) - (prevLoc.getX() * 25 + 40), 10, 10);
		}
	}
	
	/**
	 * Displays the menu when the game has been paused.
	 * @param g The {@link Graphics} object that the menu will be drawn with.
	 */
	private void showPaused (Graphics g) {
		g.drawImage(maze.getImage(), 0, 0, null);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(COLOR);
		if (toDisplay == State.INSTRUCTIONS_FROM_PAUSED) {
			showInstructions(g);
		} else {
			g.setFont(BIG);
			g.drawString("game paused", 50, 100);
			g.setFont(MEDIUM);
			if (hovered == Button.FIRST) {
				g.setColor(COLOR);
				g.fillRect(100, 160, 200, 65);
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.BLACK);
				g.fillRect(100, 160, 200, 65);
				g.setColor(COLOR);
			}
			g.drawString("instructions", 120, 200);
			if (hovered == Button.SECOND) {
				g.setColor(COLOR);
				g.fillRect(105, 270, 185, 50);
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.BLACK);
				g.fillRect(105, 270, 185, 50);
				g.setColor(COLOR);
			}
			g.drawString("resume game", 125, 300);
			if (hovered == Button.THIRD) {
				g.setColor(COLOR);
				g.fillRect(140, 370, 110, 50);
				g.setColor(Color.BLACK);
			} else {
				g.setColor(Color.BLACK);
				g.fillRect(140, 370, 110, 50);
				g.setColor(COLOR);
			}
			g.drawString("quit", 170, 400);
		}
	}
	
	/**
	 * Prints the instructions to the window.
	 * @param g The {@lik Graphics} object to print the instructions to the screen with.
	 */
	private void showInstructions (Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(COLOR);
		g.setFont(SMALL);
		for (int i = 0; i < INSTRUCTIONS[instructionNum].length; i++) {
			g.drawString(INSTRUCTIONS[instructionNum][i], 20, i * 20 + 20);
		}
		g.drawString("Press any key to continue...", 20, getHeight() - 20);
	}
	
	/**
	 * Handles input from the arrow keys, moving the character's icon onscreen if appropriate.
	 * @param e The {@link KeyEvent} to be used.
	 */
	@Override
	public void keyPressed (KeyEvent e) {
		if (toDisplay == State.MAZE) {
			int c = e.getKeyCode();
			prevLoc = loc.clone();
			if (c == KeyEvent.VK_UP || c == KeyEvent.VK_DOWN || c == KeyEvent.VK_LEFT
					|| c == KeyEvent.VK_RIGHT) { 
				if (loc.getX() >= 0 && loc.getX() < Maze.LENGTH) {
					if (c == KeyEvent.VK_UP && maze.get(loc).isOpen(Direction.UP)) {
						loc.incX(); // move up
					} else if (c == KeyEvent.VK_DOWN && maze.get(loc).isOpen(Direction.DOWN)) {
						loc.decX(); // move down
					} else if (c == KeyEvent.VK_LEFT && maze.get(loc).isOpen(Direction.LEFT)) {
						loc.decY(); // move left
					} else if (c == KeyEvent.VK_RIGHT && maze.get(loc).isOpen(Direction.RIGHT)) {
						loc.incY(); // move right
					} else { // set prevLoc to null so black circle won't be drawn over white circle
						prevLoc = null;
					}
				} else if (loc.getX() == Maze.LENGTH) {
					if (c == KeyEvent.VK_DOWN) {
						loc.decX();
					} else {
						prevLoc = null;
					}
				} else { // loc.getX() == -1
					if (c == KeyEvent.VK_UP) {
						loc.incX();
					} else {
						prevLoc = null;
					}
				}
			} else {
				if (c == KeyEvent.VK_P) {
					toDisplay = State.PAUSED;
				}
			}
		} else if (toDisplay == State.INSTRUCTIONS) {
			if (instructionNum != INSTRUCTIONS.length - 1) {
				instructionNum++;
			} else {
				toDisplay = State.MENU;
			}
		} else if (toDisplay == State.INSTRUCTIONS_FROM_PAUSED) {
			if (instructionNum != INSTRUCTIONS.length - 1) {
				instructionNum++;
			} else {
				toDisplay = State.PAUSED;
			}
		}
	}
	
	/**
	 * Handles mouse movements in relation to menu items.
	 * @param e The {@link MouseEvent} that occurred.
	 */
	@Override
	public void mouseMoved (MouseEvent e) {
		if (toDisplay == State.MENU) { // the menu is currently being displayed
			int x = e.getX();
			int y = e.getY();
			if (x >= 100 && x <= 260 && y >= 160 && y <= 225) { // the user is hovering over the "play game" button
				hovered = Button.FIRST;
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else if (x >= 70 && x <= 280 && y >= 260 && y <= 320) { // the user is hovering over the "instructions" button
				hovered = Button.SECOND;
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else { // the user is not hovering over any button
				hovered = Button.NONE;
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		} else if (toDisplay == State.PAUSED) {
			int x = e.getX();
			int y = e.getY();
			if (x >= 100 && x <= 260 && y >= 160 && y <= 235) {
				hovered = Button.FIRST;
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else if (x >= 105 && x <= 290 && y >= 270 && y <= 320) {
				hovered = Button.SECOND;
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else if (x >= 140 && x <= 250 && y >= 370 && y <= 420) {
				hovered = Button.THIRD;
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			} else {
				hovered = Button.NONE;
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		} else {
			hovered = Button.NONE;
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	/**
	 * Handles mouse clicks, transporting the user to a menu if appropriate
	 * @param e The {@link MouseEvent} that occurred.
	 */
	@Override
	public void mouseClicked (MouseEvent e) {
		if (toDisplay == State.MENU) {
			int x = e.getX();
			int y = e.getY();
			if (x >= 100 && x <= 260 && y >= 160 && y <= 225) {
				maze = new Maze();
				loc = maze.getStart();
				loc.decX();
				toDisplay = State.MAZE;
			} else if (x >= 70 && x <= 280 && y >= 260 && y <= 320) {
				toDisplay = State.INSTRUCTIONS;
			}
		} else if (toDisplay == State.PAUSED) {
			int x = e.getX();
			int y = e.getY();
			if (x >= 100 && x <= 260 && y >= 160 && y <= 235) {
				toDisplay = State.INSTRUCTIONS_FROM_PAUSED;
			} else if (x >= 105 && x <= 290 && y >= 270 && y <= 320) {
				toDisplay = State.MAZE;
			} else if (x >= 140 && x <= 250 && y >= 370 && y <= 420) {
				loc = null;
				maze = null;
				toDisplay = State.MENU;
			}
		}
	}
	
	@Override
	public String toString () {
		String s = super.toString();
		Matcher m = Pattern.compile("(.+\\..+\\[)(.+)").matcher(s);
		if (m.find()) { // in other words, if (true) {
			String s1 = m.group(1);
			String s2 = m.group(2);
			s = s1 + "toDisplay=" + toDisplay + ",maze=" + maze + ",loc=" + loc + ",prevLoc="
						+ prevLoc + ",hovered=" + hovered + ",instructionNum=" + instructionNum + s2;
		}
		
		return s;
	}
	
	@SuppressWarnings("unused")
	public static void main (String[] args) {
		Mazinator g = new Mazinator();
		System.out.println(g);
	}
	
	private static final long serialVersionUID = 7112920686269800507L;
}