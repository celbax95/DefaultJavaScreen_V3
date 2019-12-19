package fr.state.game.elements.onscreen;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.inputs.Input;
import fr.state.game.elements.collider.Body;
import fr.state.game.elements.collider.Shape;
import fr.util.point.Point;

public abstract class GameObject implements Serializable {

	private static int idCounter = 0;

	private static final long serialVersionUID = 1L;
	protected double maxSpeed;

	protected final double sizeUnit;

	protected final int id;

	protected List<GOTag> tags;

	protected Body body;

	protected final Point pos;

	protected final Point size;

	protected final Point velocity;

	protected final Point forces;

	protected double scale;

	protected GameObject(double sizeUnit) {
		this.id = idCounter++;
		this.pos = new Point();
		this.size = new Point();
		this.forces = new Point();
		this.velocity = new Point();
		this.tags = new ArrayList<>();
		this.scale = 1;
		this.sizeUnit = sizeUnit;
		this.maxSpeed = -1;
		this.initBody();
	}

	protected GameObject(Point pos, Point size, double sizeUnit, double scale) {
		this(sizeUnit);
		this.scale = scale;
		this.pos.set(pos);
		this.size.set(size.clone().mult(this.getScaling()));
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

	public void applyForces() {
		this.velocity.add(this.forces);
		if (this.maxSpeed > 0) {
			if (this.velocity.length() >= this.maxSpeed) {
				this.velocity.trigNorm().mult(this.maxSpeed);
			}
		} else if (this.maxSpeed == 0) {
			this.resetVelocity();
		}
	}

	public abstract void draw(Graphics2D g, double dt);

	public Body getBody() {
		return this.body;
	}

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
		return this.pos.clone().add(this.velocity.clone().mult(dt));
	}

	public double getMaxSpeed() {
		return this.maxSpeed;
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

	protected abstract Shape getShape();

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

	public Point getVelocity() {
		return this.velocity;
	}

	public boolean hasTag(GOTag tag) {
		return this.tags.contains(tag);
	}

	public void initBody() {
		Shape shape = this.getShape();

		if (shape != null) {
			this.body = new Body(this, shape);
		} else {
			this.body = null;
		}
	}

	public void interractWith(GameObject other) {
	}

	public boolean isAffectedBy(GameObject other) {
		return true;
	}

	public void move(double dt) {
		this.pos.add(this.velocity.clone().mult(dt));
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

	public void resetVelocity() {
		this.velocity.set(0, 0);
	}

	/**
	 * @param forces the forces to set
	 */
	public void setForces(Point forces) {
		this.forces.set(forces);
	}

	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(Point pos) {
		this.pos.set(pos);
		this.body.update();
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
		this.size.set(size.mult(this.getScaling()));
	}

	/**
	 * @param sizeUnit is final !
	 */
	public void setSizeUnit(double sizeUnit) {
	}

	public void setVelocity(Point p) {
		this.velocity.set(p);
	}

	public void update(Input input, double dt) {
		if (this.body != null) {
			this.body.update();
		}
	}
}
