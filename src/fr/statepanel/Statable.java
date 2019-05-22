package fr.statepanel;

/**
 * Des etats peuvent être attribuees aux objets implementant cette interface
 *
 * @author Loic.MACE
 *
 */
public interface Statable {

	/**
	 * Initialise l'etat de l'objet implementant Statable
	 *
	 * @param appState : etat
	 */
	void setState(IAppState appState);
}