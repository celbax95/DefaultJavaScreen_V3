package fr.menu;

import java.awt.Graphics2D;

import fr.util.point.Point;

/**
 * Un widget est un element affiche par le menu
 *
 * @author Loic.MACE
 *
 */
public interface Widget {

	/**
	 * Dessine le widget gr�ce a l'objet Graphics2D
	 *
	 * @param g : l'objet Graphics2D
	 */
	void draw(Graphics2D g);

	/**
	 * Recupere l'etat selectionne du widget
	 *
	 * @return l'etat selectionne du widget
	 */
	boolean isSelected();

	boolean isUsingKeyboardPressed();

	boolean isUsingKeyboardReleased();

	boolean isUsingMouseMoved();

	boolean isUsingMousePressed();

	boolean isUsingMouseReleased();

	boolean isUsingMouseWheel();

	boolean isVisible();

	boolean setKeyboardPressedUsage(boolean active);

	boolean setKeyboardReleasedUsage(boolean active);

	boolean setMouseMovedUsage(boolean active);

	boolean setMousePressedUsage(boolean active);

	boolean setMouseReleasedUsage(boolean active);

	boolean setMouseWheelUsage(boolean active);

	void setPos(Point pos);

	/**
	 * Selectionne le widget
	 */
	void setSelected(boolean selected);

	void setVisible(boolean visible);
}
