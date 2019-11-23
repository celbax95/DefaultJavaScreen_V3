package fr.state.game.elements.collider;

import fr.util.point.Point;

public abstract class Shape {

	Point pos;

	public abstract double getMass(double density);

	public abstract ShapesType getType();

	public void update(Point pos) {
		pos = pos.clone();
	}

}
