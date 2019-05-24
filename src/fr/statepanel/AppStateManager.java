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

	// Objet permetant d'activer / desactiver les commandes clavier et souris
	private HardwareListner hardwareListner;

	private AppStateManager() {
		this.statable = null;
		this.hardwareListner = null;
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
					"Utilisez la methode init setStatable() pour initialiser l'element statable");

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
	 * Recupere l'objet hardwareListner
	 *
	 * @return : l'objet hardwareListner
	 */
	public HardwareListner getHardwareListner() {
		return this.hardwareListner;
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
	 * Initialise le singleton AppStateManager
	 *
	 * @param statable        : l'objet statable
	 * @param hardwareListner : l'objet hardwareListner
	 */
	public void init(Statable statable, HardwareListner hardwareListner) {
		this.statable = statable;
		this.hardwareListner = hardwareListner;
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
	 * Initialise l'objet HardwareListner
	 *
	 * @param hardwareListner : l'objet HardwareListner
	 */
	public void setHardwareListner(HardwareListner hardwareListner) {
		this.hardwareListner = hardwareListner;
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

	private boolean isHardwareListnerSet() throws IllegalStateException {
		if (this.hardwareListner == null)
			throw new IllegalStateException(
					"Utilisez la methode init ou setHardwareListner pour initialiser l'element hardwareListner");
		return true;
	}

	/**
	 * Gere l'etat d'activation du clavier
	 *
	 * @param activation : etat d'activation
	 * @throws RuntimeException : l'objet hardwareListner n'est pas intialise
	 */
	void setKeyboardEnabeled(boolean activation) throws RuntimeException {
		this.isHardwareListnerSet();
		this.hardwareListner.setKeyboardEnabeled(activation);
	}

	/**
	 * Gere l'etat d'activation des clics souris
	 *
	 * @param activation : etat d'activation
	 * @throws RuntimeException : l'objet hardwareListner n'est pas intialise
	 */
	void setMouseClicksEnabeled(boolean activation) throws RuntimeException {
		this.isHardwareListnerSet();
		this.hardwareListner.setMouseClicksEnabeled(activation);
	}

	/**
	 * Gere l'etat d'activation des mouvements de la souris
	 *
	 * @param activation : etat d'activation
	 * @throws RuntimeException : l'objet hardwareListner n'est pas intialise
	 */
	void setMouseMovesEnabeled(boolean activation) throws RuntimeException {
		this.isHardwareListnerSet();
		this.hardwareListner.setMouseMovesEnabeled(activation);
	}

	/**
	 * Gere l'etat d'activation de la molette
	 *
	 * @param activation : etat d'activation
	 * @throws RuntimeException : l'objet hardwareListner n'est pas intialise
	 */
	void setMouseWheelEnabeled(boolean activation) throws RuntimeException {
		this.isHardwareListnerSet();
		this.hardwareListner.setMouseWheelEnabeled(activation);
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