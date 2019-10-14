package fr.state.game;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.util.point.Point;

public class Player {

	private static final Color COLOR = Color.RED;

	private Point pos, size;

	private Color color;

	public Player() {

		this.pos = new Point();
		this.size = new Point();

		this.color = COLOR;
	}

	public void draw(Graphics2D g) {
		g.setColor(this.color);
		g.fillRect(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy());
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * @return the pos
	 */
	public Point getPos() {
		return this.pos;
	}

	/**
	 * @return the size
	 */
	public Point getSize() {
		return this.size;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(Point pos) {
		this.pos = pos;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Point size) {
		this.size = size;
	}

	public void update(Input input) {
		System.out.println(input.keyboardEvents.get(0));
	}
}
