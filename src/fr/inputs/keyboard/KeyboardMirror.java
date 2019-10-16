package fr.inputs.keyboard;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;

/**
 * Clavier
 */
public class KeyboardMirror implements Keyboard, Serializable {

	private static final long serialVersionUID = 1L;

	public static final int KEYCODE_AMOUNT = 525;

	private Vector<Boolean> keys;

	public KeyboardMirror() {
		this.keys = new Vector<>();
		this.keys.setSize(KEYCODE_AMOUNT);
		for (int i = 0, s = this.keys.size(); i < s; i++) {
			this.keys.set(i, false);
		}

		this.keys.setSize(KEYCODE_AMOUNT);

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
