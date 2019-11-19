package fr.state.game.elements.players;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

import fr.inputs.Input;
import fr.util.point.Point;

public class OtherPlayer implements Serializable, Player {

	private static final long serialVersionUID = 1L;

	private static final Color COLOR = Color.RED;

	private static final double ACCEL = 0.5, MAX_SPEED = 10;

	private final int id;

	private Point pos, size;

	private Point forces;

	private Color color;

	private double sizeUnit;

	public OtherPlayer(int id, double sizeUnit) {
		this.id = id;
		this.sizeUnit = sizeUnit;
		this.pos = new Point();
		this.size = new Point();
		this.forces = new Point();
		this.color = COLOR;
	}

	@Override
	public void addForce(Point f) {
		this.forces.add(f);
	}

	@Override
	public void applyForces(double dt) {

		// speed
		int tmpSpeed = (int) Math.round(this.forces.length());
		if (tmpSpeed > MAX_SPEED * this.sizeUnit) {
			this.forces.mult(MAX_SPEED * this.sizeUnit / tmpSpeed);
		}

		// move
		this.pos.add(this.forces.mult(dt));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.state.game.solo.Player#draw(java.awt.Graphics2D, double)
	 */
	@Override
	public void draw(Graphics2D g, double dt) {
		g.setColor(this.color);
		g.fillRect(this.pos.ix(), this.pos.iy(), this.size.ix(), this.size.iy());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.state.game.solo.Player#getColor()
	 */
	@Override
	public Color getColor() {
		return this.color;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.state.game.solo.Player#getPos()
	 */
	@Override
	public Point getPos() {
		return this.pos;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.state.game.solo.Player#getSize()
	 */
	@Override
	public Point getSize() {
		return this.size;
	}

	@Override
	public void resetForces() {
		this.forces.set(0, 0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.state.game.solo.Player#setColor(java.awt.Color)
	 */
	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.state.game.solo.Player#setPos(fr.util.point.Point)
	 */
	@Override
	public void setPos(Point pos) {
		this.pos.set(pos);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.state.game.solo.Player#setSize(fr.util.point.Point)
	 */
	@Override
	public void setSize(Point size) {
		this.size = size.clone().mult(this.sizeUnit);
	}

	@Override
	public void setSizeUnit(double sizeUnit) {
		this.sizeUnit = sizeUnit;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.state.game.solo.Player#update(fr.inputs.Input, double)
	 */
	@Override
	public void update(Input input) {

	}
}
