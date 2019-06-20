package fr.keyboard;

import java.util.List;

public interface Keyboard {

	/**
	 * Ajoute une touche prioritaire
	 *
	 * @param key : touche
	 */
	void addPriorityKey(int key);

	/**
	 * @param i : l'indice de la touche recherche
	 * @return la touche a l'indice i
	 */
	int getKey(int i);

	/**
	 * @return une nouvelle instance de la liste des touches
	 */
	List<Integer> getKeys();

	/**
	 * Objet notifie quand une touche est pressee
	 */
	Object getPressedSignal();

	/**
	 * Recupere le signal d'une touche priorisee pressee
	 *
	 * @param key : la touche
	 * @return le signal d'une touche priorisee pressee
	 */
	Object getPriorityPressedSignal(int key);

	/**
	 * Recupere le signal d'une touche priorisee relachee
	 *
	 * @param key : la touche
	 * @return le signal d'une touche priorisee relachee
	 */
	Object getPriorityReleasedSignal(int key);

	/**
	 * Objet notifie quand une touche est relachee
	 */
	Object getReleasedSignal();

	/**
	 * @return true si une touche est pressee et false sinon
	 */
	boolean isPressed();

	/**
	 * @param key : la cle (identifiant) d'une touche
	 * @return true si une touche donnée est pressee et false sinon
	 */
	boolean isPressed(int key);

	/**
	 * Retire une touche priorisee
	 *
	 * @param key : touche
	 */
	void removePriorityKey(int key);

	/**
	 * Retire la priorite de toutes les touches
	 */
	void resetPriority();

	static Keyboard getInstance() {
		return KeyboardManager.getInstance();
	}
}