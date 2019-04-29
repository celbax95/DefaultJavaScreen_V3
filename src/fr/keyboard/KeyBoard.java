package fr.keyboard;

import java.util.List;

public interface KeyBoard {

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

	static KeyBoard getInstance() {
		return KeyBoardManager.getInstance();
	}
}