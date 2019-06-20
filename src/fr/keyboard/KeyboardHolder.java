package fr.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardHolder implements KeyListener {

	private static KeyboardManager kb;

	static {
		kb = KeyboardManager.getInstance();
	}

	public KeyboardHolder() {
	}

	/**
	 * Gere l'acton a realiser lorsqu'une touche est pressee
	 *
	 * @param e : evennement de touche
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		kb.addKeyPressed(e.getKeyCode());
	}

	/**
	 * Gere l'acton a realiser lorsqu'une touche est relachee
	 *
	 * @param e : evennement de touche
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		kb.removeKeyPressed(e.getKeyCode());
	}

	/**
	 * Gere l'acton a realiser lorsqu'une touche est touche
	 *
	 * @param e : evennement de touche
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}
}
