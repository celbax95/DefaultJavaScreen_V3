package fr.state.game.elements.players;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.state.game.elements.onscreen.GameObject;
import fr.util.point.Point;

public abstract class Player extends GameObject {

	private static final long serialVersionUID = 1L;

	protected static final double MAX_SPEED = 10;

	protected Color color;

	protected Player(double sizeUnit) {
		super(sizeUnit);
		this.color = Color.RED;
	}

	protected Player(Point pos, Point size, double sizeUnit, double scale) {
		super(pos, size, sizeUnit, scale);
		this.color = Color.RED;
	}

	@Override
	public void applyForces(double dt) {
		this.applyForces(MAX_SPEED, dt);
	}

	@Override
	public void draw(Graphics2D g, double dt) {
		g.setColor(this.color);
		Point dtPos = this.getInterpolatedPos(dt);

		g.fillRect(dtPos.ix(), dtPos.iy(), this.size.ix(), this.size.iy());
	}

	public Color getColor() {
		return this.color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
}