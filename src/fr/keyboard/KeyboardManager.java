package fr.keyboard;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Singleton Clavier
 */
public class KeyboardManager implements Keyboard {

	private static KeyboardManager instance;

	static {
		instance = new KeyboardManager();
	}

	private final Object pressedSignal;

	private final Object releasedSignal;

	private final List<Integer> keys;

	private Map<Integer, Object> priorityPressedKeys;

	private Map<Integer, Object> priorityReleasedKeys;

	private KeyboardManager() {
		this.keys = new Vector<>();
		this.pressedSignal = new Object();
		this.releasedSignal = new Object();
		this.priorityPressedKeys = new HashMap<>();
		this.priorityReleasedKeys = new HashMap<>();
	}

	/**
	 * Ajoute une touche a la liste des touches pressees
	 *
	 * @param key : la touche a ajouter
	 */
	public void addKeyPressed(int key) {

		if (!this.keys.contains(key)) {
			this.keys.add(key);
		}

		synchronized (this.pressedSignal) {
			this.pressedSignal.notifyAll();
		}

		synchronized (this.priorityPressedKeys) {
			for (Integer pKey : this.priorityPressedKeys.keySet()) {
				if (pKey == key) {
					Object signal = this.priorityPressedKeys.get(pKey);

					synchronized (signal) {
						signal.notifyAll();
					}

					break;
				}
			}
		}
	}

	@Override
	public void addPriorityKey(int key) {
		synchronized (this.priorityPressedKeys) {
			if (!this.priorityPressedKeys.containsKey(key)) {
				this.priorityPressedKeys.put(key, new Object());
				this.priorityReleasedKeys.put(key, new Object());
			} else
				throw new RuntimeException("La touche est deja priorisee");
		}
	}

	@Override
	public int getKey(int i) {
		return this.keys.get(i);
	}

	@Override
	public List<Integer> getKeys() {
		return new LinkedList<>(this.keys);
	}

	@Override
	public Object getPressedSignal() {
		return this.pressedSignal;
	}

	@Override
	public Object getPriorityPressedSignal(int key) {
		synchronized (this.priorityPressedKeys) {
			if (this.priorityPressedKeys.containsKey(key))
				return this.priorityPressedKeys.get(key);
			else
				throw new RuntimeException("La touche n'est pas priorisee");
		}
	}

	@Override
	public Object getPriorityReleasedSignal(int key) {
		synchronized (this.priorityPressedKeys) {
			if (this.priorityReleasedKeys.containsKey(key))
				return this.priorityReleasedKeys.get(key);
			else
				throw new RuntimeException("La touche n'est pas priorisee");
		}
	}

	@Override
	public Object getReleasedSignal() {
		return this.releasedSignal;
	}

	@Override
	public boolean isPressed() {
		return !this.keys.isEmpty();
	}

	@Override
	public boolean isPressed(int key) {
		return this.keys.contains(key);
	}

	/**
	 * Enleve un touche de la liste de touche pressee
	 *
	 * @param key : la touche a enlever
	 */
	public void removeKeyPressed(int key) {

		this.keys.remove((Object) key);

		synchronized (this.releasedSignal) {
			this.releasedSignal.notifyAll();
		}

		synchronized (this.priorityPressedKeys) {
			for (Integer pKey : this.priorityReleasedKeys.keySet()) {
				if (pKey == key) {
					Object signal = this.priorityReleasedKeys.get(pKey);

					synchronized (signal) {
						signal.notifyAll();
					}

					break;
				}
			}
		}

	}

	@Override
	public void removePriorityKey(int key) {
		synchronized (this.priorityPressedKeys) {
			this.priorityPressedKeys.remove(key);
			this.priorityReleasedKeys.remove(key);
		}
	}

	@Override
	public void resetPriority() {
		synchronized (this.priorityPressedKeys) {
			this.priorityPressedKeys = new HashMap<>();
			this.priorityReleasedKeys = new HashMap<>();
		}
	}

	/**
	 * @return l'unique instance de KeyBoardManager
	 */
	protected static KeyboardManager getInstance() {
		return instance;
	}
}
