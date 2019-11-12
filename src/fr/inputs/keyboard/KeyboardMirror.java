package fr.inputs.keyboard;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;

/**
 * Clavier
 */
public class KeyboardMirror implements Keyboard {

	public static final int KEYCODE_AMOUNT = 525;

	private Vector<Boolean> keys;

	public KeyboardMirror() {
		this.keys = new Vector<>();

		for (int i = 0; i < KEYCODE_AMOUNT; i++) {
			this.keys.add(false);
		}

		this.reset();
	}

	public List<Boolean> getKeys() {
		return new Vector<>(this.keys);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		this.keys.set(arg0.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		this.keys.set(arg0.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public void reset() {
		for (int i = 0; i < this.keys.size(); i++) {
			this.keys.set(0, false);
		}
	}
}
