package fr.state.menu;

import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.util.point.Point;

public interface Widget {

	void draw(Graphics2D g);

	Point getPos();

	boolean isVisible();

	void setPos(Point pos);

	void setVisible(boolean visible);

	void update(Input input);
}
