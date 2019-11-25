package fr.state.game.elements.collider;

import fr.util.point.Point;

public class AABB extends Shape {

	private ShapesType TYPE = ShapesType.AABB;

	private Point vectMin, vectMax;

	public AABB(Point ref, Point min, Point max) {
		super();
		this.pos = ref.clone();
		this.setMinMax(ref, min, max);
	}

	public Point getCenter() {
		return this.getMin().add(this.getSize().div(2));
	}

	public Point getLength() {
		Point min = this.getMin();
		Point max = this.getMax();

		return max.sub(min);
	}

	@Override
	public double getMass(double density) {
		Point length = this.getLength();
		double volume = length.x * length.y;

		return volume * density;
	}

	public Point getMax() {
		return this.pos.clone().add(this.vectMax);
	}

	public Point getMin() {
		return this.pos.clone().add(this.vectMin);
	}

	public Point getSize() {
		return this.getMax().sub(this.getMin());
	}

	@Override
	public ShapesType getType() {
		return this.TYPE;
	}

	public void setMax(Point max) {
		this.vectMax = max.clone().sub(this.pos);
	}

	public void setMin(Point min) {
		this.vectMin = min.clone().sub(this.pos);
	}

	public void setMinMax(Point ref, Point min, Point max) {
		min = min.clone();
		max = max.clone();
		this.vectMin = min.sub(ref);
		this.vectMax = max.sub(ref);
	}
}
