package fr.util.collider;

import java.awt.Graphics2D;

import fr.util.point.Point;

public class Circle extends HitboxElement {

	private Point ref;

	private double rad;

	private Point vectCenter;

	public Circle(Point ref, Point center, double rad) {
		super();
		this.ref = ref;
		this.vectCenter = new Point(center).sub(ref);
		this.rad = rad;
	}

	@Override
	public void draw(Graphics2D g) {
		int radint = (int) this.rad;
		int radM2 = (int) (this.rad * 2);

		Point pos = new Point(this.ref).add(this.vectCenter);

		g.setColor(this.COLOR);
		g.fillOval(pos.ix() - radint, pos.iy() - radint, radM2, radM2);
	}

	public Point getCenter() {
		return new Point(this.ref).add(this.vectCenter);
	}

	@Override
	public int getType() {
		return 2;
	}

	public double rad() {
		return this.rad;
	}

	public void rad(double rad) {
		this.rad = rad;
	}

	public void setCenter(Point center) {
		this.vectCenter = new Point(center).sub(this.ref);
	}
}
