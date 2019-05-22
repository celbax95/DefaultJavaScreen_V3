package fr.statepanel;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton de gestion des etats
 *
 * @author Loic.MACE
 *
 */
public class AppStateManager {

	// Instance singleton
	private static AppStateManager instance;

	static {
		instance = new AppStateManager();
	}

	// Map associant un nom a un etat
	private Map<String, IAppState> states;

	// Objet sur lequel on applique les etats
	private Statable statable;

	private AppStateManager() {
		this.statable = null;
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
	 * Applique le state associe au nom "name" a l'objet statable
	 *
	 * @param name : nom de l'etat dans la amp
	 * @throws RuntimeException : si le l'objet statable n'est pas initialise ou si
	 *                          l'etat n'est pas present dans la map
	 */
	public void applyState(String name) throws RuntimeException {
		if (this.statable == null)
			throw new IllegalStateException(
					"Utilisez la methode setStatable() pour initialiser l'element statable");

		if (!this.states.containsKey(name))
			throw new IllegalArgumentException("Le state \"" + name + "\" est inconnu");

		this.statable.setState(this.states.get(name));
	}

	/**
	 * Vide la map d'etats
	 */
	public void empty() {
		this.states = new HashMap<>();
	}

	/**
	 * Recupere l'objet statable
	 *
	 * @return : l'objet statable
	 */
	public Statable getStatable() {
		return this.statable;
	}

	/**
	 * Recupere un etat
	 *
	 * @param name : nom de l'etat dans la map
	 * @return : l'etat recupere / null si l'etat n'existe pas dans la map d'etats
	 */
	public IAppState getState(String name) {
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
	 * Initialise l'objet Statable
	 *
	 * @param statable : l'objet Statable
	 */
	public void setStatable(Statable statable) {
		this.statable = statable;
	}

	/**
	 * Initialise la map d'etat
	 *
	 * @param states : map d'etats préremplie
	 */
	public void setStates(HashMap<String, IAppState> states) {
		this.states = states;
	}

	/**
	 * Accesseur du singleton
	 *
	 * @return : le singleton AppStateManager
	 */
	public static AppStateManager getInstance() {
		return instance;
	}
}