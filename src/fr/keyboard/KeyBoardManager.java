package fr.keyboard;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Singleton Clavier
 */
public class KeyBoardManager implements KeyBoard {

	private static KeyBoardManager instance;

	static {
		instance = new KeyBoardManager();
	}

	private Object pressedSignal;

	private Object releasedSignal;

	private List<Integer> keys;

	private KeyBoardManager() {
		this.keys = new Vector<Integer>();
		this.pressedSignal = new Object();
		this.releasedSignal = new Object();
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
	}

	/**
	 * @return l'unique instance de KeyBoardManager
	 */
	protected static KeyBoardManager getInstance() {
		return instance;
	}
}
