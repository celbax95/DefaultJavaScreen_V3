package fr.statepanel;

import java.util.HashMap;

public class AppStateManager {

	private static AppStateManager instance;

	static {
		instance = new AppStateManager();
	}

	private HashMap<String, IAppState> states;

	private Statable statable;

	/* CONSTRUCTORS */

	private AppStateManager() {
		this.statable = null;
		this.states = new HashMap<>();
	}

	/* GETTERS & SETTERS */

	public void addState(IAppState... appState) {
		for (final IAppState element : appState) {
			this.states.put(element.getName().toLowerCase(), element);
		}
	}

	public void applyState(String name) throws RuntimeException {
		if (this.statable == null)
			throw new IllegalStateException(
					"Utilisez la methode setStatable() pour initialiser l'element statable");

		if (!this.states.containsKey(name))
			throw new IllegalArgumentException("Le state \"" + name + "\" est inconnu");

		this.statable.setState(this.states.get(name));
	}

	public void empty() {
		this.states = new HashMap<>();
	}

	public Statable getStatable() {
		return this.statable;
	}

	/* METHODS */

	public IAppState getState(String name) {
		return this.states.get(name.toLowerCase());
	}

	public HashMap<String, IAppState> getStates() {
		return this.states;
	}

	public boolean isEmpty() {
		return this.states.isEmpty();
	}

	public void removeState(String name) {
		this.states.remove(name.toLowerCase());
	}

	// Statable
	public void setStatable(Statable statable) {
		this.statable = statable;
	}

	// States
	public void setStates(HashMap<String, IAppState> states) {
		this.states = states;
	}

	public static AppStateManager getInstance() {
		return instance;
	}
}