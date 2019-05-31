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
	 * Dessine le widget grâce a l'objet Graphics2D
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

	boolean isUsingKeyboardPressedSignal();

	boolean isUsingKeyboardReleasedSignal();

	boolean isUsingMousePressedSignal();

	boolean isUsingMouseReleasedSignal();

	boolean isUsingMouseWheelSignal();

	boolean isVisible();

	void setKeyboardPressedSignal(Object keyboardPressedSignal);

	void setKeyboardReleasedSignal(Object keyboardReleasedSignal);

	void setMousePressedSignal(Object mousePressedSignal);

	void setMouseReleasedSignal(Object mouseReleasedSignal);

	void setMouseWheelSignal(Object mouseWheelSignal);

	void setPos(Point pos);

	/**
	 * Selectionne le widget
	 */
	void setSelected(boolean selected);

	void setVisible(boolean visible);
}
