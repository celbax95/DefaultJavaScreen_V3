package fr.state.game.elements.players;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.util.point.Point;

public interface Player {

	void addForce(Point f);

	void applyForces(double dt);

	void draw(Graphics2D g, double dt);

	/**
	 * @return the color
	 */
	Color getColor();

	/**
	 * @return the pos
	 */
	Point getPos();

	/**
	 * @return the size
	 */
	Point getSize();

	void resetForces();

	/**
	 * @param color the color to set
	 */
	void setColor(Color color);

	/**
	 * @param pos the pos to set
	 */
	void setPos(Point pos);

	/**
	 * @param size the size to set
	 */
	void setSize(Point size);

	void setSizeUnit(double sizeUnit);

	void update(Input input);
}