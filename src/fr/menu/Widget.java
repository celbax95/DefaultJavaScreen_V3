package fr.menu;

import java.awt.Graphics2D;

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

	/**
	 * Selectionne le widget
	 */
	void select();

	/**
	 *
	 * Initialise les signaux
	 *
	 * @param clickSignal     : signal emi lors d'un clic souris
	 * @param moveSignal      : signal emi lorsque la souris est bougee
	 * @param mouseWeelSignal : signal emi lorsque la molette est actionnee
	 * @param keyboardSignal  : signal emi lorsque le clavier est actionne
	 */
	void setSignals(Object clickSignal, Object moveSignal, Object mouseWeelSignal, Object keyboardSignal);
}
