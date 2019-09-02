package fr.util.collider;

import java.awt.Graphics2D;

import fr.util.point.Point;

public class AABB extends HitboxElement {

	Point ref;

	private Point vectMax;
	private Point vectMin;

	public AABB(Point ref, Point min, Point max) {

		this.ref = ref;

		this.vectMin = new Point(min).sub(ref);
		this.vectMax = new Point(max).sub(ref);
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(this.COLOR);

		Point mtm = this.minToMax();

		g.fillRect(this.min().ix(), this.min().iy(), mtm.ix(), mtm.iy());
	}

	public Point getCenter() {
		return this.min().add(this.getSize().div(2));
	}

	public double getHeight() {
		return this.getSize().y();
	}

	public Point getSize() {
		return this.max().sub(this.min());
	}

	@Override
	public int getType() {
		return 1;
	}

	/**
	 * @return the vectMinMax
	 */
	public Point getVectMinMax() {
		return this.vectMax;
	}

	public double getWidth() {
		return this.getSize().x();
	}

	public Point max() {
		return new Point(this.ref).add(this.vectMax);
	}

	public void max(Point max) {
		this.vectMax = new Point(max).sub(this.ref);
	}

	public Point min() {
		return new Point(this.ref).add(this.vectMin);
	}

	public void min(Point min) {
		this.vectMin = new Point(min).sub(this.ref);
	}

	private Point minToMax() {
		return this.max().sub(this.min());
	}

	/**
	 * @param vectMinMax the vectMinMax to set
	 */
	public void setVectMinMax(Point vectMinMax) {
		this.vectMax = vectMinMax;
	}

	@Override
	public String toString() {
		return "ref = " + this.ref + "\nmin = " + this.min() + "\nmax = " + this.max();
	}
}
