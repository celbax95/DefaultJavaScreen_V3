package fr.state.game.solo;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.inputs.Input;
import fr.util.point.Point;

public interface Player {

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

	void update(Input input, double dt);

}