package fr.mouse;

import fr.util.point.Point;

/**
 * Interface de communication avec la package mouse
 *
 * @author Loic.MACE
 *
 */
public interface Mouse {
	/**
	 * Recupere l'etat des boutons de la souris
	 *
	 * 0 : left / 1 : middle / 2 : right
	 *
	 * @return l'etat des boutons de la souris
	 */
	boolean[] getButtons();

	/**
	 * Recupere le signal emit en cas de mouvement de la souris
	 *
	 * @return : un Object sur lequel est appele notifyAll() quand la souris est
	 *         deplacee
	 */
	Object getMovedSignal();

	/**
	 * Recupere la postition du pointeur de la souris
	 *
	 * @return : la position de la souris
	 */
	Point getPos();

	/**
	 * Recupere le signal emit en cas de pression d'un bouton de la souris
	 *
	 * @return : un Object sur lequel est appele notifyAll() quand un bouton de la
	 *         souris est presse
	 */
	Object getPressedSignal();

	/**
	 * Recupere le signal emit en cas de relachement d'un bouton de la souris
	 *
	 * @return : un Object sur lequel est appele notifyAll() quand un bouton de la
	 *         souris est relache
	 */
	Object getReleasedSignal();

	/**
	 * Recupere le mouvement de la souris vers le bas
	 *
	 * @return : 0 : Pas de mouvement / 1 : un cran vers le bas
	 */
	int getWheelDown();

	/**
	 * Recupere le signal emit en cas de mouvement de la molette de la souris
	 *
	 * @return : un Object sur lequel est appele notifyAll() quand la molette de la
	 *         souris est actionnee
	 */
	Object getWheelSignal();

	/**
	 * Recupere le mouvement de la souris vers le haut
	 *
	 * @return : 0 : Pas de mouvement / 1 : un cran vers le haut
	 */
	int getWheelUp();

	/**
	 * Arrete tous les Threads en cours
	 */
	void interruptThreads();

	/**
	 * Recupere l'etat de pression du clic gauche de la souris
	 *
	 * @return l'etat de pression du clic gauche de la souris
	 */
	boolean isLeftClickPressed();

	/**
	 * Recupere l'etat de pression du clic molette de la souris
	 *
	 * @return l'etat de pression du clic molette de la souris
	 */
	boolean isMiddleClickPressed();

	/**
	 * Recupere l'etat de mouvement du pointeur de la souris
	 *
	 * @return l'etat de mouvement du pointeur de la souris
	 */
	boolean isMoving();

	/**
	 * Recupere l'etat de pression de la souris
	 *
	 * @return true si au moins 1 bouton est presse
	 */
	boolean isPressed();

	/**
	 * Recupere l'etat de pression du clic droit de la souris
	 *
	 * @return l'etat de pression du clic doit de la souris
	 */
	boolean isRightClickPressed();

	static Mouse getMouse() {
		return MouseManager.getInstance();
	}
}
