package fr.statepanel;

import java.awt.Graphics2D;
import java.util.Map;

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
	 */
	void draw(Graphics2D g);

	/**
	 * Recupere le nom de l'etat
	 *
	 * @return : le nom de l'etat
	 */
	String getName();

	void setInitData(Map<String, Object> data);

	void start(StatePanel panel);

	void stop();

	void update();
}