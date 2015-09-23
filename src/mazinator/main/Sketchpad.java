package mazinator.main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class Sketchpad extends JPanel implements Runnable {
	private Graphics g;
	private JFrame f;
	private Color background;
	private Color color;
	private KeyEvent lastKeyEvent;
	private MouseEvent lastMouseEvent;
	
	/**
	 * Initializes and runs a new {@code Sketchpad} object
	 */
	public Sketchpad () {
		try {
			f = new JFrame();
			f.addKeyListener(new KeyListener() {
				public void keyPressed (KeyEvent e) {
					lastKeyEvent = e;
					Sketchpad.this.keyPressed(e);
				}
				
				public void keyReleased (KeyEvent e) {
					lastKeyEvent = e;
					Sketchpad.this.keyReleased(e);
				}
				
				public void keyTyped (KeyEvent e) {
					lastKeyEvent = e;
					Sketchpad.this.keyTyped(e);
				}
			});
			
			f.addMouseListener(new MouseListener() {
				public void mouseClicked (MouseEvent e) {
					lastMouseEvent = e;
					Sketchpad.this.mouseClicked(e);
				}
				
				public void mousePressed (MouseEvent e) {
					lastMouseEvent = e;
					Sketchpad.this.mousePressed(e);
				}
				
				public void mouseReleased (MouseEvent e) {
					lastMouseEvent = e;
					Sketchpad.this.mouseReleased(e);
				}
				
				public void mouseEntered (MouseEvent e) {
					lastMouseEvent = e;
					Sketchpad.this.mouseEntered(e);
				}
				
				public void mouseExited (MouseEvent e) {
					lastMouseEvent = e;
					Sketchpad.this.mouseExited(e);
				}
			});
			
			f.addMouseMotionListener(new MouseMotionListener() {
				public void mouseMoved (MouseEvent e) {
					lastMouseEvent = e;
					Sketchpad.this.mouseMoved(e);
				}
				
				public void mouseDragged (MouseEvent e) {
					lastMouseEvent = e;
					Sketchpad.this.mouseDragged(e);
				}
			});
			
			f.addMouseWheelListener(new MouseWheelListener() {
				public void mouseWheelMoved(MouseWheelEvent e) {
					lastMouseEvent = e;
					Sketchpad.this.mouseWheelMoved(e);
				}
			});
			
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setDoubleBuffered(true); // doesn't work on Windows
			setVisible(true);
			f.add(this);
			f.setVisible(true);
			g = f.getGraphics();
			setup(g);
			
			Thread t = new Thread(this);
			t.start();
		} catch (Throwable t) { // an error occurred, display and error message and exit the program
			onError(t);
		}
	}
	
	/**
	 * @return The last {@link KeyEvent} that has occurred, or {@code null} if no {@link KeyEvent}
	 * 			has occurred since this method was last called.
	 */
	public final KeyEvent getLastKeyEvent () {
		KeyEvent e = lastKeyEvent;
		lastKeyEvent = null;
		return e;
	}
	
	/**
	 * @return The last {@link MouseEvent} that has occurred, or {@code null} if no {@link MouseEvent}
	 * 			has occurred since this method was last called.
	 */
	public final MouseEvent getLastMouseEvent () {
		MouseEvent e = lastMouseEvent;
		lastMouseEvent = null;
		return e;
	}
	
	/**
	 * Called automatically by {@link Thread#start()}.
	 */
	@Override
	public final void run () {
		while (true); // display the window until it's closed without messing with the graphics
	}
	
	/**
	 * Called automatically.
	 */
	@Override
	public final void paint (Graphics g) { // so that subclasses don't mess with the graphics
		super.paint(g);
	}
	
	/**
	 * Called automatically. Calls {@link Sketchpad#draw(Graphics) draw(Graphics)}.
	 */
	@Override
	public final void paintComponent (Graphics g) {
		f.setBackground(background);
		g.setColor(color);
		draw(g);
		repaint();
	}
	
	/**
	 * Called by {@link Sketchpad#paintComponent(Graphics) paintComponent(Graphics)}.
	 */
	@Override
	public final void repaint () {
		super.repaint();
	}
	
	/**
	 * Called by the {@link Sketchpad#Sketchpad() Sketchpad()} constructor.
	 * Unlike {@link Sketchpad#draw(Graphics2D) draw(Graphics2D)}, it is only called once,
	 * and should therefore be used for setup code, such as setting the location and
	 * size of the frame, customizing the {@link Graphics2D} object, etc.
	 * @param g The {@link Graphics2D} object to be used in this instance of {@link Sketchpad}.
	 */
	public abstract void setup (Graphics g);
	
	/**
	 * Sets the title of the frame.
	 * @param title The title.
	 */
	public final void setTitle (String title) {
		f.setTitle(title);
	}
	
	/**
	 * Sets the size of the frame.
	 * @param width The width of the frame.
	 * @param height The height of the frame.
	 */
	@Override
	public final void setSize (int width, int height) {
		f.setSize(width, height);
	}
	
	/**
	 * Sets the window as either resizable or non-resizable by calling {@link JFrame#setResizeable(boolean) JFrame.setResizeable(b)}.
	 * @param b The {@code boolean} value that indicates whether or not the frame will be resizable.
	 */
	public final void setResizable (boolean b) {
		f.setResizable(b);
	}
	
	/**
	 * Sets the background to the specified {@link Color}
	 * @param background The {@link Color} to be used as a background.
	 */
	@Override
	public final void setBackground (Color background) {
		this.background = background;
	}
	
	/**
	 * Sets the initial {@link Color} to the specified {@link Color}
	 * @param color The {@link Color} to be set.
	 */
	public final void setColor (Color color) {
		this.color = color;
	}
	
	/**
	 * Sets the mouse pointer to be displayed as the specified {@link Cursor} object.
	 * @param c The {@link Cursor} to be set.
	 */
	@Override
	public final void setCursor (Cursor c) {
		super.setCursor(c);
	}
	
	/**
	 * @return The background that is currently in use. 
	 */
	@Override
	public final Color getBackground () {
		return background;
	}
	
	/**
	 * @return The {@link Color} that was set using {@link Sketchpad#setColor(Color) setColor(Color)}
	 */
	public final Color getColor () {
		return color;
	}
	
	/**
	 * Called automatically inside a loop until the user closes the window
	 * that this {@code Sketchpad} represents.
	 * @param g The {@link Graphics2D} object that is used in this method.
	 */
	public abstract void draw (Graphics g);
	
	/**
	 * Called whenever a {@link KeyEvent#KEY_PRESSED} event occurs.
	 * @param e The {@link KeyEvent} object containing information about the event.
	 */
	public void keyPressed (KeyEvent e) {}
	
	/**
	 * Called whenever a {@link KeyEvent#KEY_RELEASED} event occurs.
	 * @param e The {@link KeyEvent} object containing information about the event.
	 */
	public void keyReleased (KeyEvent e) {}
	
	/**
	 * Called whenever a {@link KeyEvent#KEY_TYPED} event occurs.
	 * @param e The {@link KeyEvent} object containing information about the event.
	 */
	public void keyTyped (KeyEvent e) {}
	
	/**
	 * Called whenever a {@link MouseEvent#MOUSE_DRAGGED} event occurs.
	 * @param e The {@link MouseEvent} object containing information about the event.
	 */
	public void mouseDragged (MouseEvent e) {}
	
	/**
	 * Called whenever a {@link MouseEvent#MOUSE_MOVED} event occurs.
	 * @param e The {@link MouseEvent} object containing information about the event.
	 */
	public void mouseMoved (MouseEvent e) {}
	
	/**
	 * Called whenever a {@link MouseEvent#MOUSE_CLICKED} event occurs.
	 * @param e The {@link MouseEvent} object containing information about the event.
	 */
	public void mouseClicked (MouseEvent e) {}
	
	/**
	 * Called whenever a {@link MouseEvent#MOUSE_PRESSED} event occurs.
	 * @param e The {@link MouseEvent} object containing information about the event.
	 */
	public void mousePressed (MouseEvent e) {}
	
	/**
	 * Called whenever a {@link MouseEvent#MOUSE_RELEASED} event occurs.
	 * @param e The {@link MouseEvent} object containing information about the event.
	 */
	public void mouseReleased (MouseEvent e) {}
	
	/**
	 * Called whenever a {@link MouseEvent#MOUSE_ENTERED} event occurs.
	 * @param e The {@link MouseEvent} object containing information about the event.
	 */
	public void mouseEntered (MouseEvent e) {}
	
	/**
	 * Called whenever a {@link MouseEvent#MOUSE_EXITED} event occurs.
	 * @param e The {@link MouseEvent} object containing information about the event.
	 */
	public void mouseExited (MouseEvent e) {}
	
	/**
	 * Called whenever a {@link MouseEvent#MOUSE_WHEEL_MOVED} event occurs.
	 * @param e The {@link MouseEvent} object containing information about the event.
	 */
	public void mouseWheelMoved (MouseEvent e) {}
	
	/**
	 * Called if an uncaught exception is thrown at any point during the execution of the program.
	 * Can be overridden by subclasses.
	 * @param t The uncaught exception that has been thrown.
	 */
	public void onError (Throwable t) {}
	
	/**
	 * Returns a {@link String} representation of this {@code Sketchpad} object.
	 */
	@Override
	public String toString () {
		String s = super.toString();
		Matcher m = Pattern.compile("(.+\\..+\\[)(.+)").matcher(s);
		if (m.find()) { // in other words, if (true) {
			String s1 = m.group(1);
			String s2 = m.group(2);
			s = s1 + "g=" + g + ",f=" + f + ",background=" + background + ",color=" + color
					+ ",lastKeyEvent=" + lastKeyEvent + ",lastMouseEvent=" + lastMouseEvent + s2;
		}
		
		return s;
	}
	
	private static final long serialVersionUID = 2961584927355320071L;
}