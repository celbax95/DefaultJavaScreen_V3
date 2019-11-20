package fr.state.game.elements.onscreen;

import java.awt.Graphics2D;
import java.io.Serializable;

import fr.inputs.Input;
import fr.util.point.Point;

public abstract class GameObject implements Serializable {

	private static final long serialVersionUID = 1L;

	protected final double sizeUnit;

	protected Point pos;

	protected Point size;

	protected Point forces;

	protected double scale;

	protected GameObject(double sizeUnit) {
		this.pos = new Point();
		this.size = new Point();
		this.forces = new Point();
		this.scale = 1;
		this.sizeUnit = sizeUnit;
	}

	protected GameObject(Point pos, Point size, double sizeUnit, double scale) {
		this(sizeUnit);
		this.pos = pos;
		this.size = size;
		this.scale = scale;
	}

	public void addForces(Point... forces) {
		for (Point f : forces) {
			this.forces.add(f);
		}
	}

	public void applyForces(double dt) {
		this.pos.add(this.forces.clone().mult(dt));
	}

	public void applyForces(double maxSpeed, double dt) {

		maxSpeed *= this.getScaling();

		Point forces = this.forces.clone();

		// speed
		int tmpSpeed = (int) Math.round(forces.length());
		if (tmpSpeed > maxSpeed) {
			forces.mult(maxSpeed / tmpSpeed);
		}

		// move
		this.pos.add(forces.mult(dt));
	}

	public abstract void draw(Graphics2D g, double dt);

	/**
	 * @return the forces
	 */
	public Point getForces() {
		return this.forces;
	}

	public Point getInterpolatedPos(double dt) {
		return this.pos.clone().add(this.forces.clone().mult(dt));
	}

	/**
	 * @return the pos
	 */
	public Point getPos() {
		return this.pos;
	}

	/**
	 * @return the scale
	 */
	public double getScale() {
		return this.scale;
	}

	protected double getScaling() {
		return this.scale * this.sizeUnit;
	}

	/**
	 * @return the size
	 */
	public Point getSize() {
		return this.size;
	}

	/**
	 * @return the sizeUnit
	 */
	public double getSizeUnit() {
		return this.sizeUnit;
	}

	public void resetForces() {
		this.forces.set(0, 0);
	}

	/**
	 * @param forces the forces to set
	 */
	public void setForces(Point forces) {
		this.forces = forces;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(Point pos) {
		this.pos = pos;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(double scale) {
		this.scale = scale;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Point size) {
		this.size = size.mult(this.getScaling());
	}

	/**
	 * @param sizeUnit is final !
	 */
	public void setSizeUnit(double sizeUnit) {
	}

	public abstract void update(Input input, double dt);
}
