package fr.statepanel;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Interface representant un etat
 *
 * @author Loic.MACE
 *
 */
public interface IAppState {
	/**
	 * Methode appelee lors d'un repaint
	 *
	 * @param g : Objet servant a afficher
	 * @throws StateRequest : changement d'etat
	 */
	void draw(Graphics2D g) throws StateRequest;

	/**
	 * Recupere la couleur de fond
	 *
	 * @return : la couleur de l'etat
	 */
	Color getBackgroundColor();

	/**
	 * Recupere le nom de l'etat
	 *
	 * @return : le nom de l'etat
	 */
	String getName();

	/**
	 * Recupere le rythme de repaint
	 *
	 * @return le rythme de repaint
	 */
	int getRepaintRate();

	/**
	 * Change l'etat actif de l'etat
	 *
	 * @param active : true si actif
	 */
	void setActive(boolean active);

	/**
	 * Initialise le repainter
	 *
	 * @param repainter : nouveau repainter
	 */
	void setRepainter(Repainter repainter);
}