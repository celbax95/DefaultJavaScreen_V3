package fr.state.game.elements.onscreen;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.inputs.Input;
import fr.util.point.Point;

public abstract class GameObject implements Serializable {

	private static int idCounter = 0;

	private static final long serialVersionUID = 1L;

	protected final double sizeUnit;

	protected final int id;

	protected List<GOTag> tags;

	protected Point pos;

	protected Point size;

	protected Point forces;

	protected double scale;

	protected GameObject(double sizeUnit) {
		this.id = idCounter++;
		this.pos = new Point();
		this.size = new Point();
		this.forces = new Point();
		this.tags = new ArrayList<>();
		this.scale = 1;
		this.sizeUnit = sizeUnit;
	}

	protected GameObject(Point pos, Point size, double sizeUnit, double scale) {
		this(sizeUnit);
		this.scale = scale;
		this.pos = pos;
		this.size = size.clone().mult(this.getScaling());
	}

	public void addForces(Point... forces) {
		for (Point f : forces) {
			this.forces.add(f);
		}
	}

	public void addTags(GOTag... tags) {
		for (GOTag tag : tags) {
			if (!this.tags.contains(tag)) {
				this.tags.add(tag);
			}
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

	/**
	 * @return the id
	 */
	public int getId() {
		return this.id;
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

	public List<GOTag> getTags() {
		List<GOTag> returnList = new ArrayList<>();
		for (GOTag tag : this.tags) {
			returnList.add(tag);
		}
		return returnList;
	}

	public boolean hasTag(GOTag tag) {
		return this.tags.contains(tag);
	}

	public void removeTag(GOTag tag) {
		int ind = -1;
		if ((ind = this.tags.indexOf(tag)) != -1) {
			this.tags.remove(ind);
		}
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
