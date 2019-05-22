package fr.statepanel;

import javax.swing.JPanel;

/**
 * Les objets implementant cette interface sont utilises pour repeindre l'ecran
 *
 * @author Loic.MACE
 *
 */
public interface Repainter extends Runnable {

	/**
	 * Interrompt le thread du repainter
	 */
	void interrupt();

	/**
	 * Demande de repaint
	 */
	void repaint();

	/**
	 * Initialisation du panel a repaint
	 *
	 * @param panel : panel a repaint
	 */
	void setPanel(JPanel panel);

	/**
	 * Initialisation du rythme de repaint
	 *
	 * @param rate : rythme de repaint
	 */
	void setRate(int rate);

	/**
	 * Lance le Thread de repaint
	 */
	void start();
}
