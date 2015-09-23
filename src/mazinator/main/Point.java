package mazinator.main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the point at which characters are in the maze.
 */
public class Point {
	private int x;
	private int y;
	
	/**
	 * Creates a new {@code Point}, with {@code x} and {@code y} set to {@code 0}.
	 */
	public Point () {
		x = 0;
		y = 0;
	}
	
	/**
	 * Creates a new {@code Point} with the given x- and y-coordinates.
	 * @param x
	 * @param y
	 * @throws IllegalArgumentException if {@code !(x >= 0 && x < 150 && y >= 0 && y < 20)}
	 */
	public Point (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates a new {@code Point} that is a copy of the point that is passed as a parameter.
	 * @param p The {@code Point} to be copied.
	 */
	public Point (Point p) {
		x = p.x;
		y = p.y;
	}
	
	public Point (java.awt.Point p) {
		x = (int) p.getX();
		y = (int) p.getY();
	}
	
	/**
	 * Creates a new {@code Point} object using a {@link String}, provided that the {@link String}
	 * is formatted as {@code (x, y)}. Otherwise, an {@link IllegalArgumentException} is thrown.
	 * @param s The {@link String} to be parsed.
	 * @throws IllegalArgumentException if the {@link String}'s format is invalid.
	 */
	public Point (String s) {
		if (s.matches("\\s*\\(\\s*\\d+\\s*,\\s*\\d+\\s*\\)\\s*")) {
			Matcher m = Pattern.compile("\\s*\\(\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\)\\s*").matcher(s);
			x = Integer.parseInt(m.group(1));
			y = Integer.parseInt(m.group(2));
		} else {
			throw new IllegalArgumentException("String format invalid.");
		}
	}
	
	/**
	 * @return The {@code x}-coordinate of this {@code Point}.
	 */
	public int getX () {
		return x;
	}
	
	/**
	 * @return The {@code y}-coordinate of this {@code Point}
	 */
	public int getY () {
		return y;
	}
	
	/**
	 * Increases the value of {@link x} by 1.
	 */
	public void incX () {
		x++;
	}
	
	/**
	 * Decreases the value of {@link x} by 1.
	 */
	public void decX () {
		x--;
	}
	
	/**
	 * Increases the value of {@link y} by 1.
	 */
	public void incY () {
		y++;
	}
	
	/**
	 * Decreases the value of {@link y} by 1.
	 */
	public void decY () {
		y--;
	}
	
	/**
	 * Increases the value of {@link x} by the given amount.
	 * @param n The amount to increase {@link x} by.
	 */
	public void incX (int n) {
		x += n;
	}
	
	/**
	 * Increases the value of {@link y} by the gien amount.
	 * @param n The amount to increase {@link y} by.
	 */
	public void incY (int n) {
		y += n;
	}
	
	/**
	 * Increases the values of {@link x} and {@link y} by the given amounts.
	 * @param x The amount to increase {@link x} by.
	 * @param y The amount to increase {@link y} by.
	 */
	public void translate (int x, int y) {
		this.x += x;
		this.y += y;
	}
	
	/**
	 * Translates this {@code Point} using the {@code x}- and {@code y}- values of the specified
	 * {@code Point}
	 * @param p The {@code Point} to be used in the translation.
	 */
	public void translate (Point p) {
		x += p.x;
		y += p.y;
	}
	
	public void translate (java.awt.Point p) {
		x += p.getX();
		y += p.getY();
	}
	
	public void setX (int n) {
		x = n;
	}
	
	public void setY (int n) {
		y = n;
	}
	
	public void set (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void set (Point p) {
		x = p.x;
		y = p.y;
	}
	
	public void set (java.awt.Point p) {
		x = p.x;
		y = p.y;
	}
	
	/**
	 * @return This {@code Point} object as a {@link java.awt.Point}.
	 */
	public java.awt.Point toPoint () {
		return new java.awt.Point(x, y);
	}
	
	@Override
	public Point clone () {
		return new Point(x, y);
	}
	
	@Override
	public boolean equals (Object o) {
		if (o instanceof Point) {
			Point p = (Point) o;
			return p.x == x && p.y == y;
		} else {
			return false;
		}
	}
	
	@Override
	public String toString () {
		return getClass().getName() + "[x=" + x + ",y=" + y + "]";
	}
}