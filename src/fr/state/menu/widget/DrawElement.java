package fr.state.menu.widget;

import java.awt.Graphics2D;

import fr.util.point.Point;

public interface DrawElement {
	void draw(Graphics2D g, Point ref);

	Point getPos();

	Point getSize();

	boolean isLocked();

	void lock();

	void setPos(Point pos);
}
