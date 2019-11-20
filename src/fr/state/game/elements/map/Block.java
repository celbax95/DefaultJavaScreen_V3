package fr.state.game.elements.map;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.state.game.elements.onscreen.GameObject;
import fr.util.point.Point;

public class Block extends GameObject {

	private static final long serialVersionUID = 1L;

	private static final Color DEFAULT_COLOR = Color.BLACK;

	private Color color;

	protected Block(double sizeUnit) {
		super(sizeUnit);
		this.color = DEFAULT_COLOR;
	}

	protected Block(Point pos, Point size, double sizeUnit, double scale) {
		super(pos, size, sizeUnit, scale);
		this.color = DEFAULT_COLOR;
	}

	@Override
	public void draw(Graphics2D g, double dt) {
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
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public void update(Input input, double dt) {
	}
}
