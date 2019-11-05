package fr.statepanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestion des etats
 *
 * @author Loic.MACE
 *
 */
public class AppStateManager {

	// Map associant un nom a un etat
	private Map<String, IAppState> states;

	public AppStateManager() {
		this.states = new HashMap<>();
	}

	/**
	 * Ajoute un etat a la map d'etats Le nom de l'etat est recuperee via
	 * l'interface IAppState
	 *
	 * @param appState : etat a ajouter
	 *
	 * @see IAppState
	 */
	public void addState(IAppState... appState) {
		for (final IAppState element : appState) {
			this.states.put(element.getName().toLowerCase(), element);
		}
	}

	/**
	 * Vide la map d'etats
	 */
	public void empty() {
		this.states = new HashMap<>();
	}

	/**
	 * Recupere un etat
	 *
	 * @param name : nom de l'etat dans la map
	 * @return : l'etat recupere / null si l'etat n'existe pas dans la map d'etats
	 */
	public IAppState getState(String name) {

		if (!this.states.containsKey(name))
			throw new IllegalArgumentException("Le state \"" + name + "\" est inconnu");

		return this.states.get(name.toLowerCase());
	}

	/**
	 * Recupere la map d'etat
	 *
	 * @return la map d'etat
	 */
	public Map<String, IAppState> getStates() {
		return this.states;
	}

	/**
	 * Test l'etat vide de la map d'etats
	 *
	 * @return : true si la map est vide
	 */
	public boolean isEmpty() {
		return this.states.isEmpty();
	}

	/**
	 * Retire un etat de la map d'etat
	 *
	 * @param name : nom de l'etat a retirer
	 */
	public void removeState(String name) {
		this.states.remove(name.toLowerCase());
	}

	/**
	 * Initialise la map d'etat
	 *
	 * @param states : map d'etats préremplie
	 */
	public void setStates(HashMap<String, IAppState> states) {
		this.states = states;
	}
}